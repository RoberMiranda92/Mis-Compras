/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubu.miscompras.rest;

import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import com.ubu.miscompras.contract.ProductContract;
import com.ubu.miscompras.exceptions.MisComprasException;
import com.ubu.miscompras.process.ImageProcess;
import com.ubu.miscompras.process.ProductProcess;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
    private String textoFinal;
    
    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "prueba";
    }

    /**
     * Este metodo Post que guarda la imagen en el servidor y obtiene sus
     * productos empaquetandolo en un JSON.
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

        saveFile(fileInputStream, filePath);
        istream.close();

        ImageProcess image = new ImageProcess(filePath);
        ArrayList<File> productos = image.getProductsFileFromImage();
        
        ProductProcess p = new ProductProcess(productos);
        StringBuilder response = new StringBuilder();
        try {
            textoFinal = p.getText();
            String[] splitProducts = textoFinal.split("\n\n");
            response.append("{");
            for (int i = 0; i < splitProducts.length; i++) {
                response.append("\"producto" + i + "\":");
                response.append(ProductContract.buildJSON(splitProducts[i]));
                if (i != splitProducts.length - 1) {
                    response.append(",");
                }
            }
            response.append("}");
            return Response.status(Response.Status.OK).entity(response.toString()).build();

        } catch (MisComprasException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

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

    /*private String htmlCode() {

     String code2 = this.getClass().getProtectionDomain().getCodeSource().toString();
     String code = "<!DOCTYPE html>\n"
     + "<html>\n"
     + "<body>\n"
     + "<table>"
     + "<tr>"
     + "<td><h2>Imagen original.</h2></td>"
     + "<td><h2>Imagen Gris.</h2></td>"
     + "</tr>"
     + "<tr>"
     + "<td><img src=\"/misCompras/images/" + file.getName()
     + "\" alt=\"Mountain View\"></td>"
     + "<td><img src=\"/misCompras/images/" + Utils.filename(file) + "_GRAY"
     + Utils.extension(file) + "\" alt=\"Mountain View\"></td>"
     + "</tr>"
     + "<tr>"
     + "<td><h2>Imagen Binarizada.</h2></td>"
     + "<td><h2>Recta Imagen.</h2></td>"
     + "</tr>"
     + "<tr>"
     + "<td><img src=\"/misCompras/images/" + Utils.filename(file) + "_BINARIZADA"
     + Utils.extension(file) + "\" alt=\"Mountain View\"></td>"
     + "<td><img src=\"/misCompras/images/" + Utils.filename(file) + "_RECTA"
     + Utils.extension(file) + "\" alt=\"Mountain View\"></td>"
     + "</tr>"
     + "<tr>"
     + "<td><h2>Deskewing.</h2></td>"
     + "<td><h2>DILATE.</h2></td>"
     + "</tr>"
     + "<tr>"
     + "<td><img src=\"/misCompras/images/" + Utils.filename(file) + "_DESKEWING"
     + Utils.extension(file) + "\" alt=\"Mountain View\"></td>"
     + "<td><img src=\"/misCompras/images/" + Utils.filename(file) + "_DILATE"
     + Utils.extension(file) + "\" alt=\"Mountain View\"></td>"
     + "</tr>"
     + "<tr>"
     + "<td><h2>Crop.</h2></td>"
     + "<td><h2>Salida.</h2></td>"
     + "</tr>"
     + "<tr>"
     + "<td><img src=\"/misCompras/images/" + Utils.filename(file) + "_CROP"
     + Utils.extension(file) + "\" alt=\"Mountain View\"></td>"
     + "<td><p>" + textoFinal + "</p></td>"
     + "</tr>"
     + "</table>"
     + "</body>"
     + "</html>";
     return code;
     }*/
}
