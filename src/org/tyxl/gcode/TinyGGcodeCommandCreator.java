/*
 * Minor change to the command creator - create TinyG commands.
 */

/*
    Copywrite 2013 Will Winder

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
package org.tyxl.gcode;

import org.tyxl.gcode.GcodeCommandCreator;
import org.tyxl.types.GcodeCommand;
import org.tyxl.types.TinyGGcodeCommand;

/**
 *
 * @author wwinder
 */
public class TinyGGcodeCommandCreator extends GcodeCommandCreator {
    @Override
    public GcodeCommand createCommand(String commandString) {
        TinyGGcodeCommand command = new TinyGGcodeCommand(commandString);
        command.setCommandNumber(this.numCommands++);
        return command;
    }
}
