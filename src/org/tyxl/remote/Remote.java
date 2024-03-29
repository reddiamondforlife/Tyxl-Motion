package org.tyxl.remote;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.vecmath.Point3d;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

import com.google.gson.Gson;

import org.tyxl.controller.MainWindowAPI;
import org.tyxl.i18n.Localization;
import org.tyxl.listeners.ControllerListener;
import org.tyxl.types.GcodeCommand;
import java.net.URL;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

/**
 * This class will launch a local webserver which will provide a simple pendant interface
 * @author bobj
 *
 */
      
@MultipartConfig
public class Remote implements ControllerListener {
    
    private static final Logger logger = Logger.getLogger(Remote.class.getName());
	private MainWindowAPI mainWindow;
	private Server server = null;
	private int port = 8082;
	private SystemStateBean systemState = new SystemStateBean();
	
	public Remote(MainWindowAPI mainWindow) {
		this.mainWindow = mainWindow;
	}

public Resource getBaseResource(){
		try {
                        URL res = getClass().getResource("/resources/remote");
                        return Resource.newResource(res);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * Launches the local web server.
	 * @return the url for the pendant interface
	 */
	public List<RemoteURLBean> start(){
            server = new Server(port);

            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirectoriesListed(false);
            resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
            resourceHandler.setBaseResource(getBaseResource());
            resourceHandler.setDirectoriesListed(true);

            ContextHandler sendGcodeContext = new ContextHandler();
            sendGcodeContext.setContextPath("/sendGcode");
            sendGcodeContext.setBaseResource(getBaseResource());
            sendGcodeContext.setClassLoader(Thread.currentThread().getContextClassLoader());
            sendGcodeContext.setHandler(new SendGcodeHandler());

            ContextHandler adjustManualLocationContext = new ContextHandler();
            adjustManualLocationContext.setContextPath("/adjustManualLocation");
            adjustManualLocationContext.setBaseResource(getBaseResource());
            adjustManualLocationContext.setClassLoader(Thread.currentThread().getContextClassLoader());
            adjustManualLocationContext.setHandler(new AdjustManualLocationHandler());

            ContextHandler getSystemStateContext = new ContextHandler();
            getSystemStateContext.setContextPath("/getSystemState");
            getSystemStateContext.setBaseResource(getBaseResource());
            getSystemStateContext.setClassLoader(Thread.currentThread().getContextClassLoader());
            getSystemStateContext.setHandler(new GetSystemStateHandler());

            ContextHandler configContext = new ContextHandler();
            configContext.setContextPath("/config");
            configContext.setBaseResource(getBaseResource());
            configContext.setClassLoader(Thread.currentThread().getContextClassLoader());
            configContext.setHandler(new ConfigHandler());
            configContext.setInitParameter("cacheControl", "max-age=0, public");

           ContextHandler uploadContext = new ContextHandler();
            uploadContext.setContextPath("/upload");
            uploadContext.setBaseResource(getBaseResource());
            uploadContext.setClassLoader(Thread.currentThread().getContextClassLoader());
            uploadContext.setHandler(new UploadHandler());
            
            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[] {uploadContext, configContext, sendGcodeContext, adjustManualLocationContext, getSystemStateContext, resourceHandler, new DefaultHandler()});

            server.setHandler(handlers);

            try {
                    server.start();
            } catch (Exception e) {
                    throw new RuntimeException(e);
            }

            return getUrlList();
	}
	
	/**
	 * Unfortunately, this is not as simple as it seems... since you can have multiple addresses and some of those may not be available via wireless
	 * 
	 * @return
	 */
	public List<RemoteURLBean> getUrlList(){
		List<RemoteURLBean> out = new ArrayList<>();
		
		Enumeration<NetworkInterface> networkInterfaceEnum;
		try {
			networkInterfaceEnum = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
	    while(networkInterfaceEnum.hasMoreElements())
	    {
	        NetworkInterface networkInterface = networkInterfaceEnum.nextElement();

	        Enumeration<InetAddress> addressEnum = networkInterface.getInetAddresses();
	        while(addressEnum.hasMoreElements())
	        {
	            InetAddress addr = addressEnum.nextElement();
	            String hostAddress = addr.getHostAddress();
	           if(!hostAddress.contains(":") && 
	        		   !hostAddress.equals("127.0.0.1")){
	        	   String url = "http://"+hostAddress+":"+port;
	        	   System.out.println("Listening on: "+url);
	           }
	        }
	    }
	    
	    return out;
	}

	public class SendGcodeHandler extends AbstractHandler{
		@Override
		public void handle(String target,Request baseRequest,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
			baseRequest.setHandled(true);
			String gCode = baseRequest.getParameter("gCode");
			
			try {
				if(isManualControlEnabled()){
						switch (gCode) {
						case "$H":
							mainWindow.getController().performHomingCycle();
							break;
						case "$X":
							mainWindow.getController().killAlarmLock();
							break;
						case "$C":
							mainWindow.getController().toggleCheckMode();
							break;
						case "RESET_ZERO":
							mainWindow.resetCoordinatesButtonActionPerformed();
							break;
						case "RETURN_TO_ZERO":
							mainWindow.returnToZeroButtonActionPerformed();
							break;
						case "SEND_FILE":
							mainWindow.sendButtonActionPerformed();
							break;
						default:
							mainWindow.sendGcodeCommand(gCode);
							break;
						}
				} else {
					switch (gCode) {
					case "PAUSE_RESUME_FILE":
						mainWindow.pauseButtonActionPerformed();
						break;
					case "CANCEL_FILE":
						mainWindow.cancelButtonActionPerformed();
						break;
					default:
						break;
					}
					
				}
			} catch (Exception e) {
	            e.printStackTrace();
	            logger.warning(Localization.getString("SendGcodeHandler"));
			}

			response.getWriter().print(getSystemStateJson());
		}
	}
	
	public class GetSystemStateHandler extends AbstractHandler{
		@Override
		public void handle(String target,Request baseRequest,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
			baseRequest.setHandled(true);
			mainWindow.updateSystemState(systemState);
			response.getWriter().print(getSystemStateJson());
		}
	}
	
	public String getSystemStateJson(){
		return new Gson().toJson(systemState);
	}
	public class ConfigHandler extends AbstractHandler{
		@Override
		public void handle(String target,Request baseRequest,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
			baseRequest.setHandled(true);
			response.setContentType("application/json");
			response.getWriter().print(new Gson().toJson(mainWindow.getSettings().getRemoteConfig()));
		}
	}
        public class UploadHandler extends AbstractHandler{
		@Override
		public void handle(String target,Request baseRequest,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
			
                       Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
                       String filename = getFilename(filePart);
                       System.out.println(filename);
                       baseRequest.setHandled(true);
		}
	}

        private static String getFilename(Part part) {
    for (String cd : part.getHeader("content-disposition").split(";")) {
        if (cd.trim().startsWith("filename")) {
            String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
        }
    }
    return null;
}

	

	public class AdjustManualLocationHandler extends AbstractHandler{
		@Override
		public void handle(String target,Request baseRequest,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
			baseRequest.setHandled(true);
			
			if(isManualControlEnabled()){
				int dirX = parseInt(baseRequest.getParameter("dirX"));
				int dirY = parseInt(baseRequest.getParameter("dirY"));
				int dirZ = parseInt(baseRequest.getParameter("dirZ"));
				double stepSize = parseDouble(baseRequest.getParameter("stepSize"));
				
				mainWindow.adjustManualLocation(dirX, dirY, dirZ, stepSize);
			}

			response.getWriter().print(systemState.getControlState().name());
		}
	}
	
	public int parseInt(String string){
		int out = 0;
		
		try {
			out = Integer.parseInt(string);
		} catch (Exception e) {
			// nothing to do
		}
		return out;
	}
	
	public double parseDouble(String string){
		double out = 0.0;
		try {
			out = Double.parseDouble(string);
		} catch (Exception e) {
			// nothing to do
		}
		return out;
	}
	
	

	public void stop(){
		try {
			if(server!=null){
				server.stop();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isManualControlEnabled() {
        switch (systemState.getControlState()) {
        case COMM_DISCONNECTED:
        	return false;
        case COMM_IDLE:
        	return true;
        case COMM_SENDING:
        	return false;
        default:
        	return true;
        }
	}

	public MainWindowAPI getMainWindow() {
		return mainWindow;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public void setMainWindow(MainWindowAPI mainWindow) {
		this.mainWindow = mainWindow;
	}

	@Override
	public void fileStreamComplete(String filename, boolean success) {
	}

	@Override
	public void commandQueued(GcodeCommand command) {
	}

	@Override
	public void commandSent(GcodeCommand command) {
	}

	@Override
	public void commandComplete(GcodeCommand command) {
	}

	@Override
	public void commandComment(String comment) {
	}

	@Override
	public void messageForConsole(String msg, Boolean verbose) {
	}

	@Override
	public void statusStringListener(String state, Point3d machineCoord, Point3d workCoord) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postProcessData(int numRows) {
	}

	public SystemStateBean getSystemState() {
		return systemState;
	}

	public void setSystemState(SystemStateBean systemState) {
		this.systemState = systemState;
	}
}
