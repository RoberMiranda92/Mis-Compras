/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubu.miscompras.images;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Roberto
 */
public class ImageProcess {
    private final File imageFile;
    private ImagePlus imagePlus;
    private ImageProcessor imageProcessor;
    private ImageConverter imageConverter;
    private BufferedImage image;
    
    private ImageProcess (File imageFile){
        this.imageFile = imageFile;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException ex) {
            Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        imagePlus = new ImagePlus(imageFile.getAbsolutePath());
        imageProcessor = imagePlus.getProcessor();
        imageConverter= new ImageConverter(imagePlus);
    }

    public ImageProcess(String imagePath) {
       this.imageFile = new File(imagePath);
        imagePlus = new ImagePlus(imagePath);
        imageProcessor = imagePlus.getProcessor();
        imageConverter= new ImageConverter(imagePlus);
       
    }
    
    public void start(){
        imageToGray();
        imageBinarice();
        imageErode();
        imageDilate();
       
    }
    
    
    private void imageToGray(){
        /**imageConverter.convertToGray8();
        imagePlus.updateAndDraw();
        FileSaver fs = new FileSaver(imagePlus);
        String path = imageFile.getParent()+File.separator+filename(imageFile)+"_GRAY"+extension(imageFile);
        fs.saveAsJpeg(path);*/
        
        try {
         System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
         File input = imageFile;
         BufferedImage image = ImageIO.read(input);	

         byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
         Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
         mat.put(0, 0, data);

         Mat mat1 = new Mat(image.getHeight(),image.getWidth(),CvType.CV_8UC1);
         Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);

         byte[] data1 = new byte[mat1.rows() * mat1.cols() * (int)(mat1.elemSize())];
         mat1.get(0, 0, data1);
         BufferedImage image1 = new BufferedImage(mat1.cols(),mat1.rows(), BufferedImage.TYPE_BYTE_GRAY);
         image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);

         File ouptut = new File(imageFile.getParent()+File.separator+filename(imageFile)+"_GRAY"+extension(imageFile));
         ImageIO.write(image1, "jpg", ouptut);
         
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
        
   
    } 
    
    private void imageBinarice(){
        imageProcessor=imagePlus.getProcessor();
        imageProcessor.autoThreshold();
        imagePlus.updateAndDraw();
        FileSaver fs = new FileSaver(imagePlus);
        String path = imageFile.getParent()+File.separator+filename(imageFile)+"_BINARICE"+extension(imageFile);
        fs.saveAsJpeg(path);
    } 
    
    private void imageErode(){
        imageProcessor=imagePlus.getProcessor();
        imageProcessor.erode();
        imagePlus.updateAndDraw();
        FileSaver fs = new FileSaver(imagePlus);
        String path = imageFile.getParent()+File.separator+filename(imageFile)+"_ERODE"+extension(imageFile);
        fs.saveAsJpeg(path);
    }
    
    private void imageDilate(){
        imageProcessor=imagePlus.getProcessor();
        imageProcessor.dilate();
        imagePlus.updateAndDraw();
        FileSaver fs = new FileSaver(imagePlus);
        String path = imageFile.getParent()+File.separator+filename(imageFile)+"_DILATE"+extension(imageFile);
        fs.saveAsJpeg(path);
    } 
    

    
    
    
    public String extension(File file) {
    int dot = file.getName().lastIndexOf(".");
    return file.getName().substring(dot);
  }

  public String filename(File file) {
    int dot = file.getName().lastIndexOf(".");
    return file.getName().substring(0, dot);
  }

    

}
