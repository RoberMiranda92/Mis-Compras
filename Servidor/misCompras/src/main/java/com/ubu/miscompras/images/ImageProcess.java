/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubu.miscompras.images;

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
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Roberto
 */
public class ImageProcess {

    private File imageFile;
    private BufferedImage image;
    private Mat mat;

    private ImageProcess(File imageFile) {
        this.imageFile = imageFile;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException ex) {
            Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ImageProcess(String imagePath) {
        this.imageFile = new File(imagePath);

    }

    public void start() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        imageToGray();
        deskewing();

    }

    private void imageToGray() {

        try {

            File input = imageFile;
            BufferedImage image = ImageIO.read(input);
            byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
            mat.put(0, 0, data);

            Mat mat1 = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC1);
            Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);

            byte[] data1 = new byte[mat1.rows() * mat1.cols() * (int) (mat1.elemSize())];
            mat1.get(0, 0, data1);
            BufferedImage image1 = new BufferedImage(mat1.cols(), mat1.rows(), BufferedImage.TYPE_BYTE_GRAY);
            image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);

            File ouptut = new File(imageFile.getParent() + File.separator + filename(imageFile) + "_GRAY" + extension(imageFile));
            ImageIO.write(image1, "jpg", ouptut);
            imageFile = ouptut;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void deskewing() {

        try {
            File input = imageFile;
            BufferedImage image = ImageIO.read(input);
            byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            Mat src = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC1);
            src.put(0, 0, data);

            Mat binaryImage = new Mat();

            Imgproc.threshold(src, binaryImage, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
             
            String fileBinario = imageFile.getParent() + File.separator + filename(imageFile)
                    + "_BINARIZADA" + extension(imageFile);
            Imgcodecs.imwrite(fileBinario, binaryImage);
            Size size = binaryImage.size();

            Mat lines = new Mat();
            //100
            int threshold = 100;
            double minLineSize = size.width;
            //20
            int lineGap = 20;
            double angle = 0;
 
            Imgproc.HoughLinesP(binaryImage, lines, 1, Math.PI / 180, threshold,
                    minLineSize / 2.f, lineGap);

            for (int x = 0; x < lines.cols(); x++) {

                /**
                 * double[] vec = lines.get(0, x); double[] val = new double[4];
                 *
                 * val[0] = 0; val[1] = ((float) vec[1] - vec[3]) / (vec[0] -
                 * vec[2]) * -vec[0] + vec[1]; val[2] = src.cols(); val[3] =
                 * ((float) vec[1] - vec[3]) / (vec[0] - vec[2]) * (src.cols() -
                 * vec[2]) + vec[3];
                 *
                 * lines.put(0, x, val);
                 */
                double[] vec = lines.get(0, x);
                double x1 = vec[0],
                        y1 = vec[1],
                        x2 = vec[2],
                        y2 = vec[3];
                Point start = new Point(x1, y1);
                Point end = new Point(x2, y2);

                Imgproc.line(mat, start, end, new Scalar(0, 0, 255), 5);
                angle += Math.atan2(y2 - y1, x2 - x1);
            }
            if(angle!=0)
                angle /= lines.cols();
            String fileTemp = imageFile.getParent() + File.separator + filename(imageFile)
                    + "_RECTA" + extension(imageFile);
            Imgcodecs.imwrite(fileTemp, mat);
            Point center = new Point(src.width() / 2, src.height() / 2);
          
            Mat rotImage = Imgproc.getRotationMatrix2D(center, angle * 180 / Math.PI, 1);

            Imgproc.warpAffine(src, src, rotImage, src.size(), Imgproc.INTER_CUBIC);

            String file = imageFile.getParent() + File.separator + filename(imageFile)
                    + "_DESKEWING"+ extension(imageFile);
            Imgcodecs.imwrite(file, src);

        } catch (IOException ex) {
            Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
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
