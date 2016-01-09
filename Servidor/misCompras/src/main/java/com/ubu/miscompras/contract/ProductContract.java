/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
