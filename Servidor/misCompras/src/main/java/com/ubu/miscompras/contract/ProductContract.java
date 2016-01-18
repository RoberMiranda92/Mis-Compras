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
package com.ubu.miscompras.contract;

import org.json.JSONObject;

/**
 *
 * @author Roberto
 */
public class ProductContract {

    private static final String NOMBRE = "nombre";
    private static final String CANTIDAD = "cantidad";
    private static final String PRECIO = "precio";
    private static final String TOTAL = "total";
    private static int TICKET_COLUMS=4;
    
    /**
     * Este metodo devuelve una linea de producto convertida en JSON.
     * 
     * @param lineaProducto
     * @return JSON de la linea de producto.
     */
    public static JSONObject buildJSON(String lineaProducto) {

        String[] splitProducto = splitProductLine(lineaProducto);

        JSONObject producto = new JSONObject();
        
        producto.put(TOTAL, splitProducto[3]);
        producto.put(PRECIO, splitProducto[2]);
        producto.put(NOMBRE, splitProducto[1]);
        producto.put(CANTIDAD, splitProducto[0]);
       
        return producto;
    }
    /**
     * 
     * @param productLine
     * @return 
     */
    private static String[] splitProductLine(String productLine) {
        String[] line = new String[TICKET_COLUMS];
        String[] split = productLine.split(" ");
        line[0] = split[0];
        line[1] = "";
        for (int i = 0; i < split.length - 3; i++) {
            if (i > 0) {
                line[1] += " ";
            }
            line[1] = line[1] + split[i + 1];
        }
        line[2] = split[split.length - 2];
        line[3] = split[split.length - 1];

        return line;
    }
}
