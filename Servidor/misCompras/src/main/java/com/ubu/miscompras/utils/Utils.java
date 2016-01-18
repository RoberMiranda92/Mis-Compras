/*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.ubu.miscompras.utils;

import java.io.File;

/**
 * Clase de utilidades.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class Utils {
    
    public static String DICCIONARY_FILE_NAME="Diccionario.txt";
    public static String CSV_FILE_NAME = "Ocurrencias.csv";
    
    /**
     * Este método devuelve la extensión de un fichero.
     *
     * @param file fichero.
     * @return extensión del fichero
     */
    public static String getFileExtension(File file) {
        int dot = file.getName().lastIndexOf(".");
        return file.getName().substring(dot);
    }

    /**
     * Este método devuelve el nombre de un fichero sin extensión.
     *
     * @param file fichero.
     * @return nombre del fichero.
     */
    public static String getFileName(File file) {
        int dot = file.getName().lastIndexOf(".");
        return file.getName().substring(0, dot);
    }
    
    public String getTargetDirectory(){
        
        File classPath = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        String path = classPath.getAbsolutePath();
         while (!path.endsWith("target")) {
            int index;
            index = path.lastIndexOf(File.separator);
            path = path.substring(0, index);
        }
         return path;
    }
}
