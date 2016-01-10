package com.ubu.miscompras.utils;

import java.io.File;

/**
 * Clase de utilidades.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class Utils {
    
    public static String RUTA_DICCIONARIO= "diccionarios" + File.separator + "Diccionario.txt";
    public static String RUTA_CSV = "diccionarios" + File.separator + "Ocurrencias.csv";
    
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
