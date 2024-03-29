/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyxl.uielements;

import gnu.io.CommPortIdentifier;
import java.util.List;
import org.tyxl.controller.CommUtils;
import org.tyxl.controller.FirmwareUtils;
import org.tyxl.controller.MainWindow;
import org.tyxl.i18n.Localization;

/**
 *
 * @author Daniel
 */
public class SerialSettingsDialog extends javax.swing.JDialog {

    MainWindow mw;

    /**
     * Creates new form SerialSettingsDialog
     */
    public SerialSettingsDialog(java.awt.Frame parent, boolean modal, MainWindow mw) {
        super(parent, modal);
        this.mw = mw;
        initComponents();
        initProgram();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        connection = new javax.swing.JPanel();
        port = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        firmwareComboBox = new javax.swing.JComboBox();
        baudrateSelectionComboBox = new javax.swing.JComboBox();
        commPortComboBox = new javax.swing.JComboBox();
        refreshButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        connection.setBorder(javax.swing.BorderFactory.createTitledBorder("Connection"));

        port.setText("Port:");

        jLabel1.setText("BAUD:");

        jLabel2.setText("Firmware:");

        firmwareComboBox.setEditable(true);

        baudrateSelectionComboBox.setMaximumRowCount(6);
        baudrateSelectionComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2400", "4800", "9600", "19200", "38400", "57600", "115200" }));
        baudrateSelectionComboBox.setSelectedIndex(6);

        commPortComboBox.setEditable(true);

        refreshButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/refresh.gif"))); // NOI18N

        javax.swing.GroupLayout connectionLayout = new javax.swing.GroupLayout(connection);
        connection.setLayout(connectionLayout);
        connectionLayout.setHorizontalGroup(
            connectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionLayout.createSequentialGroup()
                .addGroup(connectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(port))
                .addGap(18, 18, 18)
                .addGroup(connectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(commPortComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(connectionLayout.createSequentialGroup()
                        .addComponent(baudrateSelectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshButton))
                    .addComponent(firmwareComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        connectionLayout.setVerticalGroup(
            connectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(connectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(port)
                    .addComponent(commPortComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(connectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addGroup(connectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(baudrateSelectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(refreshButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(connectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(firmwareComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(connection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(saveButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void initProgram() {
        loadPortSelector();
        loadFirmwareSelector();
    }

    private void loadPortSelector() {
        commPortComboBox.removeAllItems();

        List<CommPortIdentifier> portList = CommUtils.getSerialPortList();

        if (portList.size() < 1) {
            MainWindow.displayErrorDialog("No Serial communication was found, please connect your TYXL and restart the program.");
            System.exit(1);
        } else {
            java.util.Iterator<CommPortIdentifier> portIter = portList.iterator();

            while (portIter.hasNext()) {
                CommPortIdentifier portIdentifier = portIter.next();
                commPortComboBox.addItem(portIdentifier.getName());
            }

            commPortComboBox.setSelectedIndex(0);
        }
    }

    private void loadFirmwareSelector() {
        firmwareComboBox.removeAllItems();
        List<String> firmwareList = FirmwareUtils.getFirmwareList();

        if (firmwareList.size() < 1) {
            MainWindow.displayErrorDialog(Localization.getString("mainWindow.error.noFirmware"));
        } else {
            java.util.Iterator<String> iter = firmwareList.iterator();
            while (iter.hasNext()) {
                firmwareComboBox.addItem(iter.next());
            }
        }
    }
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        mw.closeCommConnection();
        mw.updateControlsForState(MainWindow.ControlState.COMM_DISCONNECTED);
        mw.openSerial();
        this.setVisible(false);
    }//GEN-LAST:event_saveButtonActionPerformed
    public static String getFirmware() {
        return firmwareComboBox.getSelectedItem().toString();
    }

    public static String getComPort() {
        return commPortComboBox.getSelectedItem().toString();
    }

    public static int getPortRate() {
        return Integer.parseInt(baudrateSelectionComboBox.getSelectedItem().toString());
    }

    public static void setComPort(String comPort) {
        commPortComboBox.setSelectedItem(comPort);
    }

    public static void setPortRate(String portRate) {
        baudrateSelectionComboBox.setSelectedItem(portRate);
    }

    public static void setFirmware(String firmware) {
        firmwareComboBox.setSelectedItem(firmware);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JComboBox baudrateSelectionComboBox;
    private static javax.swing.JComboBox commPortComboBox;
    private javax.swing.JPanel connection;
    private static javax.swing.JComboBox firmwareComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel port;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}
