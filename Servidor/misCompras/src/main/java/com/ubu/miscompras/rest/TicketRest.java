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
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import org.json.JSONArray;

/**
 *
 * @author Roberto
 *
 */
@Path("/file")
public class TicketRest {
    @Context ServletContext context;
    private final HashMap<String, Integer> nWords = new HashMap<String, Integer>();
    private static final String SERVER_UPLOAD_LOCATION_FOLDER = "//WEB-INF//images//";
    private static final String SERVER_DICIONARY_LOCATION_FOLDER = "//WEB-INF//diccionarios//";
    private String textoFinal;
    Logger logger = Logger.getLogger(getClass().getName());

    
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
      
        

        FormDataBodyPart filePart = body;
        ContentDisposition headerOfFilePart = filePart.getContentDisposition();
        InputStream fileInputStream = istream;
        
        String file = headerOfFilePart.getFileName();
        String[] tokens = file.split("\\.(?=[^\\.]+$)");
        
        String imageFilePath =context.getRealPath(SERVER_UPLOAD_LOCATION_FOLDER)+File.separator+tokens[0]+File.separator + file; 
        
        saveFile(fileInputStream, imageFilePath);
        istream.close();

        ImageProcess image = new ImageProcess(imageFilePath);
        ArrayList<File> productos = image.getProductsFileFromImage();
        
        String diccionarioFilePath= context.getRealPath(SERVER_DICIONARY_LOCATION_FOLDER)+File.separator + Utils.DICCIONARY_FILE_NAME;
        String csvFilePath= context.getRealPath(SERVER_DICIONARY_LOCATION_FOLDER)+File.separator + Utils.CSV_FILE_NAME;
      
        
        ProductProcess p = new ProductProcess(productos,diccionarioFilePath,csvFilePath);
        JSONArray array = new JSONArray();
        try {
            textoFinal = p.getText();
            String[] splitProducts = textoFinal.split("\n\n");
            for (String splitProduct : splitProducts) {
                array.put(ProductContract.buildJSON(splitProduct));
            }
            return Response.status(Response.Status.OK).entity(array.toString()).build();

        } catch (MisComprasException ex) {
            logger.severe(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }  
    
    
    
    /**
     * 
     * @param data
     * @return 
     */
    @POST
    @Path("/diccionario")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response saveDiccionary(String data){
        
        Writer fileWriter = null;
        BufferedWriter bufferedWriter = null;
         try {
             String path = context.getRealPath(SERVER_DICIONARY_LOCATION_FOLDER)+File.separator + Utils.DICCIONARY_FILE_NAME;
            File diccionario = new File(path);
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
                        bufferedWriter.write(word+System.getProperty("line.separator"));
                    }
                }
            }
            in.close();
            bufferedWriter.close();
            return Response.status(Response.Status.OK).entity("OK").build();
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
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

        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        }
    }
}
