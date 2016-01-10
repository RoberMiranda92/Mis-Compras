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
import com.ubu.miscompras.utils.Utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;

/**
 *
 * @author Roberto
 *
 */
@Path("/file")
public class TicketRest {
    
    private final HashMap<String, Integer> nWords = new HashMap<String, Integer>();
    private final String ruta = Utils.RUTA_DICCIONARIO;
    private static final String SERVER_UPLOAD_LOCATION_FOLDER = "\\images\\";
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

        String filePath = getTargetFolder() + SERVER_UPLOAD_LOCATION_FOLDER + headerOfFilePart.getFileName();

        saveFile(fileInputStream, filePath);
        istream.close();

        ImageProcess image = new ImageProcess(filePath);
        ArrayList<File> productos = image.getProductsFileFromImage();
        
        ProductProcess p = new ProductProcess(productos);
        JSONArray array = new JSONArray();
        try {
            textoFinal = p.getText();
            String[] splitProducts = textoFinal.split("\n\n");
            for (int i = 0; i < splitProducts.length; i++) {
                array.put(ProductContract.buildJSON(splitProducts[i]));
            }
            return Response.status(Response.Status.OK).entity(array.toString()).build();

        } catch (MisComprasException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }  
    
    
    /**
     * Este metodo Post que guarda una lista de nombres de producto en el 
     * fichero de diccionario.
     *
     * @param body
     * @param istream
     * @return
     */
    @POST
    @Path("/diccionario")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response saveDiccionary(String data){
        
        Writer fileWriter = null;
        BufferedWriter bufferedWriter = null;
         try {
            File diccionario = new File(getTargetFolder() + File.separator + ruta);
            if (!diccionario.exists()) {
                diccionario.createNewFile();
            }

            BufferedReader in = new BufferedReader(new FileReader(diccionario.getAbsolutePath()));
            Pattern p = Pattern.compile("\\w+");
            for (String temp = ""; temp != null; temp = in.readLine()) {
                Matcher m = p.matcher(temp);
                while (m.find()) {
                    nWords.put((temp = m.group()), nWords.containsKey(temp) ? nWords.get(temp) + 1 : 1);
                }
            }
            fileWriter = new FileWriter(diccionario,true);
            bufferedWriter = new BufferedWriter(fileWriter);

            
            
            String[] lines = data.split(",");
            
            for(String line : lines){
                String[] words = line.split(" ");
                for(String word : words){
                    
                    if(!nWords.containsKey(word)){
                        bufferedWriter.write(line+System.getProperty("line.separator"));
                    }
                }
            }
            in.close();
            bufferedWriter.close();
            return Response.status(Response.Status.OK).entity("OK").build();
        } catch (IOException ex) {
            Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
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
        
        File f = new File (serverLocation);
        
        File parent = f.getParentFile();
        
        if(!parent.exists())
            parent.mkdirs();
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

    private String getTargetFolder(){
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
