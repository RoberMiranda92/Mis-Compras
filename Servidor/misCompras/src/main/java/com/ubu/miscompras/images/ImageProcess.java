/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubu.miscompras.images;

import com.ubu.miscompras.utils.Utils;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

/**
 *
 * @author Roberto
 */
public class ImageProcess {

    private File imageFile;
    private BufferedImage image;
    private Mat mat;
    private File imagenOriginal;

    private ImageProcess(File imageFile) {
        this.imagenOriginal = imageFile;
        this.imageFile = imageFile;

    }

    public ImageProcess(String imagePath) {
        this.imageFile = new File(imagePath);
        this.imagenOriginal = new File(imagePath);

    }

    public void start() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        imageToGray();
        deskewing();
        cropImage();

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

            File ouptut = new File(imagenOriginal.getParent() + File.separator + Utils.filename(imagenOriginal) + "_GRAY" + Utils.extension(imagenOriginal));
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

            String fileBinario = imagenOriginal.getParent() + File.separator + Utils.filename(imagenOriginal)
                    + "_BINARIZADA" + Utils.extension(imagenOriginal);
            Imgcodecs.imwrite(fileBinario, binaryImage);
            Size size = binaryImage.size();

            Mat lines = new Mat();
            //100
            int threshold = 100;
            double minLineSize = size.width / 10.f;
            //20
            int lineGap = 120;
            double angle = 0;

            Imgproc.HoughLinesP(binaryImage, lines, 1, Math.PI / 180, threshold,
                    minLineSize, lineGap);

            for (int x = 0; x < lines.cols(); x++) {
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
            if (angle != 0) {
                angle /= lines.cols();
            }
            String fileTemp = imagenOriginal.getParent() + File.separator + Utils.filename(imagenOriginal)
                    + "_RECTA" + Utils.extension(imagenOriginal);
            Imgcodecs.imwrite(fileTemp, mat);
            Point center = new Point(src.width() / 2, src.height() / 2);

            Mat rotImage = Imgproc.getRotationMatrix2D(center, angle * 180 / Math.PI, 1);

            Imgproc.warpAffine(src, src, rotImage, src.size(), Imgproc.INTER_CUBIC, Core.BORDER_CONSTANT, new Scalar(255, 0, 0));

            String file = imagenOriginal.getParent() + File.separator + Utils.filename(imagenOriginal)
                    + "_DESKEWING" + Utils.extension(imagenOriginal);
            Imgcodecs.imwrite(file, src);
            imageFile = new File(file);

        } catch (IOException ex) {
            Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cropImage() {
        try {
            File input = imageFile;
            BufferedImage image = ImageIO.read(input);
            byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            Mat src = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC1);
            src.put(0, 0, data);

            Mat binaryImage = new Mat();

            Imgproc.threshold(src, binaryImage, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

            String fileBinario = imagenOriginal.getParent() + File.separator + Utils.filename(imagenOriginal)
                    + "_BINARIZADA2" + Utils.extension(imagenOriginal);
            Imgcodecs.imwrite(fileBinario, binaryImage);

            Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(binaryImage.width(), 3));
            Mat dilate = new Mat();
            Imgproc.dilate(binaryImage, dilate, element);

            String fileDilate = imagenOriginal.getParent() + File.separator + Utils.filename(imagenOriginal)
                    + "_DILATE" + Utils.extension(imagenOriginal);
            Imgcodecs.imwrite(fileDilate, dilate);
            Mat mask = Mat.zeros(binaryImage.size(), CvType.CV_8UC1);
            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

            Mat hierarchy = new Mat();

            Imgproc.findContours(dilate, contours, hierarchy,
                    Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE,
                    new Point(0, 0));

            for (int i = 0; i < contours.size(); i++) {
                Rect rect = Imgproc.boundingRect(contours.get(i));
                Mat maskROI = new Mat(mask, rect);
                maskROI.setTo(new Scalar(0, 0, 0), maskROI);

                Imgproc.drawContours(mask, contours, i,
                        new Scalar(255, 255, 255), Core.FILLED);

                double r = (double) Core.countNonZero(maskROI)
                        / (rect.width * rect.height);

                if (r > .60 && (rect.height > 15 && rect.width > 10)) {
                    Imgproc.rectangle(binaryImage, new Point(rect.x, rect.y),
                            new Point(rect.x + rect.width, rect.y + rect.height),
                            new Scalar(255, 0, 0), 1);
                }

            }
            String fileCrop = imagenOriginal.getParent() + File.separator + Utils.filename(imagenOriginal)
                    + "_CROP" + Utils.extension(imagenOriginal);
            Imgcodecs.imwrite(fileCrop, binaryImage);

        } catch (IOException ex) {
            Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
