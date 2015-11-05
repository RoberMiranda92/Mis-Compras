/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubu.miscompras.rest;

import com.google.gson.Gson;

import com.ubu.miscompras.model.Imagen;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Roberto
 */
@Path("/nuevo")
public class TicketRest {
    
    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "prueba";
    }
    
    @POST
    @Path("/ticket")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String test(String incomingData) {
            
          Gson gson = new Gson();
          Imagen imagen = gson.fromJson(incomingData, Imagen.class);
          System.out.println(imagen.getNombre());
                      
          byte[]decodedImage = Base64.decodeBase64(imagen.getData().getBytes());
          
          FileOutputStream imageOutFile = null;
        try {
            imageOutFile = new FileOutputStream("Y:\\Desktop\\"+imagen.getNombre());
            imageOutFile.write(decodedImage);
            imageOutFile.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TicketRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TicketRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "prueba";
    }
    
}
