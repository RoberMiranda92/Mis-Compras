/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubu.miscompras.rest;

import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import com.ubu.miscompras.images.ImageProcess;
import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Roberto
 *
 */
@Path("/file")
public class TicketRest {

    private static final String SERVER_UPLOAD_LOCATION_FOLDER = "\\Y:\\Desktop\\TrabajoFinaldeGrado\\Servidor\\misCompras\\target\\misCompras-1.0-SNAPSHOT\\images\\";
    private static File file;

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "prueba";
    }

    /**
     * metodo Post que guarda la imagen en el servidor.
     *
     * @param body
     * @param istream
     * @return
     */
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("file") FormDataBodyPart body, @FormDataParam("file") InputStream istream) throws IOException {
        System.out.print(System.getProperty("java.library.path"));
        FormDataBodyPart filePart = body;
        ContentDisposition headerOfFilePart = filePart.getContentDisposition();
        InputStream fileInputStream = istream;

        String filePath = SERVER_UPLOAD_LOCATION_FOLDER + headerOfFilePart.getFileName();

        //Guarda el fichero en el Servidor.
        saveFile(fileInputStream, filePath);
        istream.close();
        file = new File(filePath);
        String output = htmlCode();
        ImageProcess i = new ImageProcess(filePath);
        i.start();
        return Response.status(200).entity(output).build();

    }

    /**
     * Guarda el impuStream en la ruta Especificada.
     *
     * @param uploadedInputStream del Fichero
     * @param serverLocation ruta donde se guarda el fichero
     */
    private void saveFile(InputStream uploadedInputStream, String serverLocation) {
        try {
            OutputStream outpuStream = new FileOutputStream(new File(
                    serverLocation));
            int read = 0;
            byte[] bytes = new byte[1024];
            outpuStream = new FileOutputStream(new File(serverLocation));

            while ((read = uploadedInputStream.read(bytes)) != -1) {
                outpuStream.write(bytes, 0, read);
            }
            outpuStream.flush();
            outpuStream.close();
            uploadedInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String htmlCode() {
        
        String code2= this.getClass().getProtectionDomain().getCodeSource().toString();
        String code = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<body>\n"
                + "\n"
                + "\n"+"<h1>File saved to server location using FormDataMultiPart : " + file.getAbsolutePath() + "</h1>"
                + "<h2>Spectacular Mountain</h2>\n"
                + "<img src=\"/misCompras/images/" +file.getName()+ "\" alt=\"Mountain View\" style=\"width:304px;height:228px;\">\n"
                + "<img src=\"/misCompras/images/" +file.getName()+ "\" alt=\"Mountain View\" style=\"width:304px;height:228px;\">\n"
                + "\n"
                + "</body>\n"
                + "</html>";
        return code;
    }
}
