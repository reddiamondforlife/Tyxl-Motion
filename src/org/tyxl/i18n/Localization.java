/*
 * Localization messages.
 *
 * Created on Dec 15 2013
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
package org.tyxl.i18n;

import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author wwinder
 */
public class Localization {
    public final static DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
    static {dfs.setDecimalSeparator('.');}

    private static ResourceBundle bundle = null;

    public static void initialize(String language, String region) {
        Locale locale = new Locale(language, region);
        bundle = ResourceBundle.getBundle("resources.MessagesBundle", locale);
    }
    
    public static String getString(String id) {
        if (bundle == null) {
            Localization.initialize("en", "US");
        }
        return bundle.getString(id);
    }
}
