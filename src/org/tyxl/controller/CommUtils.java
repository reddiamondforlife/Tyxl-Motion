/*
 * Collection of useful Comm related utilities.
 */

/*
    Copywrite 2012-2013 Will Winder

    This file is part of Universal Gcode Sender (UGS).

    UGS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    UGS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with UGS.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.tyxl.controller;

import org.tyxl.types.GcodeCommand;
import gnu.io.CommPortIdentifier;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wwinder
 */
public class CommUtils {
    /** 
     * Generates a list of available serial ports.
     */
    public static java.util.List<CommPortIdentifier> getSerialPortList() {
        int type = CommPortIdentifier.PORT_SERIAL;
        
        java.util.Enumeration<CommPortIdentifier> portEnum = 
                CommPortIdentifier.getPortIdentifiers();
        java.util.List<CommPortIdentifier> returnList =
                new java.util.ArrayList<CommPortIdentifier>();
        
        while ( portEnum.hasMoreElements() ) 
        {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            if (portIdentifier.getPortType() == type) {
                returnList.add(portIdentifier);
            }
        }
        return returnList;
    }
    
    /** 
     * Checks if there is enough room in the GRBL buffer for nextCommand.
     */
    static protected Boolean checkRoomInBuffer(int sentBuffer, String nextCommand) {
        if (nextCommand == null ) {
            return false;
        }
        
        int characters = sentBuffer + nextCommand.length() + 1;
        return characters <= GrblUtils.GRBL_RX_BUFFER_SIZE;
    }
    
    /** 
     * Checks if there is enough room in the GRBL buffer for nextCommand.
     */
    static protected Boolean checkRoomInBuffer(List<GcodeCommand> list, GcodeCommand nextCommand) {
        String command = nextCommand.getCommandString();
        int characters = getSizeOfBuffer(list);
        // TODO: Carefully trace the newlines in commands and make sure
        //       the GRBL_RX_BUFFER_SIZE is honored.
        //       For now add a safety character to each command.
        characters += command.length() + 1;
        return characters <= GrblUtils.GRBL_RX_BUFFER_SIZE;
    }
    
    /**
     * Returns the number of characters in the list of GcodeCommands and adds
     * the length of the list representing one newline per command.
     * 
     * Synchronized because this list is frequently modified through events,
     * especially at the beginning of a file transfer.
     */
    static synchronized protected int getSizeOfBuffer(List<GcodeCommand> list) {
        int characters = 0;
        GcodeCommand command;
        // Number of characters in list.
        Iterator<GcodeCommand> iter = list.iterator();
        while (iter.hasNext()) {
            command = iter.next();
            String next = command.getCommandString();
            // TODO: Carefully trace the newlines in commands and make sure
            //       the GRBL_RX_BUFFER_SIZE is honored.
            //       For now add a safety character to each command.
            characters += next.length() + 1;
        }
        return characters;
    }
}
