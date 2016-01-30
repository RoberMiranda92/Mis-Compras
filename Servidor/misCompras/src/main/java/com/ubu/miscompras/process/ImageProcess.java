/**
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
package com.ubu.miscompras.process;

import com.ubu.miscompras.utils.Utils;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Classe que realiza todo el tratamiento de un fichero de imagen, la cual
 * contiene un ticket y la separa en ficheros de imagen de cada linea de
 * producto.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class ImageProcess {

    private final File imagenOriginal;
    private ArrayList<File> files;
    private Mat original;
    final Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Constructor de la clase
     *
     * @param imagePath path del archivo que contiene la imágen para analizar.
     */
    public ImageProcess(String imagePath) {
        this.files = new ArrayList<>();
        this.imagenOriginal = new File(imagePath);

    }

    /**
     * Devuelve una lista de archivos que contienen la imagen de cada linea de
     * producto de una imagen de ticket.
     *
     *
     * @return lista de archivos
     */
    public ArrayList<File> getProductsFileFromImage() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        try {
            File input = imagenOriginal;
            BufferedImage image;
            image = ImageIO.read(input);

            byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            original = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
            original.put(0,
                    0, data);

            Mat grayImage = imageToGray(original);
            grayImage = smoothing(grayImage);
            Mat deskewImage = deskewingImage(grayImage);
            files = cropImage(deskewImage);
            files = binarizaTion(files);

            Collections.reverse(files);
            return files;
        } catch (IOException ex) {
             logger.severe(ex.getMessage());
        }
        return files;
    }

    /**
     * Este método transforma una imagen en color a escala de grises.
     *
     * @param src imagen en color.
     * @return imagen en escala de grises.
     */
    private Mat imageToGray(Mat src) {

        Mat grayImage = new Mat(src.height(), src.width(), CvType.CV_8UC1);
        Imgproc.cvtColor(src, grayImage, Imgproc.COLOR_RGB2GRAY);

        /* String output = imagenOriginal.getParent() + File.separator + Utils.filename(imagenOriginal)
         + "_GRAY" + Utils.extension(imagenOriginal);
         Imgcodecs.imwrite(output, grayImage);*/
        return grayImage;

    }

    /**
     * Este método elimina ruido del archivo de imagen.
     *
     * @param src mat de la imagen
     * @return mat de imagen suavizado.
     */
    private Mat smoothing(Mat src) {

        Mat suavizado = new Mat();
        Imgproc.medianBlur(src, suavizado, 3);

        String output = imagenOriginal.getParent() + File.separator + Utils.getFileName(imagenOriginal)
                + "_GRAY" + Utils.getFileExtension(imagenOriginal);
        Imgcodecs.imwrite(output, suavizado);

        return suavizado;
    }

    /**
     * Este método a partir de una imagen es escala de grises, endereza la
     * imagen los grados que esta esté inclinada
     *
     * @param src imagen a enderezar en escala de grises
     * @return imagen correctamente enderezada.
     */
    private Mat deskewingImage(Mat src) {

        Mat binaryImage = new Mat();

        Imgproc.threshold(src, binaryImage, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

        /*String fileBinario = imagenOriginal.getParent() + File.separator + Utils.filename(imagenOriginal)
         + "_BINARIZADA" + Utils.extension(imagenOriginal);
         Imgcodecs.imwrite(fileBinario, binaryImage);*/
        double angle = getAnglefromMat(binaryImage);

        Point center = new Point(src.width() / 2, src.height() / 2);
        Mat rotImage = Imgproc.getRotationMatrix2D(center, angle * 180 / Math.PI, 1);
        double[] color = src.get(src.height() / 2, src.width() / 2);
        Scalar s;
        if (color != null) {
            s = new Scalar(color[0], 0, 0);
        } else {
            s = new Scalar(255, 0, 0);
        }
        Imgproc.warpAffine(src, src, rotImage, src.size(), Imgproc.INTER_CUBIC, Core.BORDER_CONSTANT, s);

        /*fileDesk = imagenOriginal.getParent() + File.separator + Utils.filename(imagenOriginal)
         + "_DESKEWING" + Utils.extension(imagenOriginal);
         Imgcodecs.imwrite(fileDesk, src);*/
        return src;

    }

    /**
     * Devuelve el ángulo de inclinación de un ticket aplicando la transformada
     * de Hough.
     *
     * @param src Mat de imagen binarizada.
     * @return angulo de inclinación.
     */
    private double getAnglefromMat(Mat src) {

        double angle = 0;
        Size size = src.size();

        Mat lines = new Mat();
        int threshold = 100;
        double minLineSize = size.width / 10.f;
        int lineGap = 120;

        Imgproc.HoughLinesP(src, lines, 1, Math.PI / 180, threshold,
                minLineSize, lineGap);

        for (int x = 0; x < lines.cols(); x++) {
            double[] vec = lines.get(0, x);
            double x1 = vec[0],
                    y1 = vec[1],
                    x2 = vec[2],
                    y2 = vec[3];
            Point start = new Point(x1, y1);
            Point end = new Point(x2, y2);
            Imgproc.line(original, start, end, new Scalar(0, 255, 0), 3);
            angle += Math.atan2(y2 - y1, x2 - x1);
        }
        if (angle != 0) {
            angle /= lines.cols();
        }

        String fileTemp = imagenOriginal.getParent() + File.separator
                + Utils.getFileName(imagenOriginal) + "_RECTA"
                + Utils.getFileExtension(imagenOriginal);
        Imgcodecs.imwrite(fileTemp, original);

        return angle;
    }

    /**
     * Devuelve un array de ficheros que contiene los productos obtenidos de un
     * ticket en escala de grises
     *
     * @param src ticket en escala de grises
     * @return ArrayList de dicheros con la imagen de cada linea de producto de
     * un ticket.
     */
    private ArrayList<File> cropImage(Mat src) {

        ArrayList<File> productos = new ArrayList<>();
        Mat binaryImageOriginal = new Mat();

        Imgproc.threshold(src, binaryImageOriginal, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

        Mat binaryImage = binaryImageOriginal.clone();

        /*String fileBinario = imagenOriginal.getParent() + File.separator + Utils.filename(imagenOriginal)
         + "_BINARIZADA2" + Utils.extension(imagenOriginal);
         Imgcodecs.imwrite(fileBinario, binaryImage);*/
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(binaryImage.width(), 1));
        Mat dilate = new Mat();
        Imgproc.dilate(binaryImage, dilate, element);

        /*String fileDilate = imagenOriginal.getParent() + File.separator + Utils.filename(imagenOriginal)
         + "_DILATE" + Utils.extension(imagenOriginal);
         Imgcodecs.imwrite(fileDilate, dilate);*/
        Mat mask = Mat.zeros(binaryImage.size(), CvType.CV_8UC1);

        List<MatOfPoint> contours = new ArrayList<>();
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

            if (r > .60 && (rect.height > 25 && rect.width >= (2 * binaryImage.width()) / 3)) {

                Imgproc.rectangle(binaryImageOriginal, new Point(rect.x, rect.y),
                        new Point(rect.x + rect.width, rect.y + rect.height),
                        new Scalar(255, 0, 0), 1);
                Mat cropped = src.submat(rect);

                Imgproc.threshold(cropped, cropped, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
                String filesCrop = imagenOriginal.getParent() + File.separator + Utils.getFileName(imagenOriginal)
                        + "_CROP" + "_" + i + Utils.getFileExtension(imagenOriginal);
                Imgcodecs.imwrite(filesCrop, cropped);
                productos.add(new File(filesCrop));
            }
        }

        /*String fileCrop = imagenOriginal.getParent() + File.separator + Utils.filename(imagenOriginal)
         + "_CROP" + Utils.extension(imagenOriginal);
         Imgcodecs.imwrite(fileCrop, binaryImageOriginal);*/
        return productos;
    }

    private ArrayList<File> binarizaTion(ArrayList<File> files) {
        ArrayList<File> filesDilate = new ArrayList<>();
        try {
            for (File f : files) {

                File input = f;
                BufferedImage image;

                image = ImageIO.read(input);

                byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
                Mat src = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC1);
                src.put(0, 0, data);

                Mat dilate = new Mat(src.rows(),
                        src.cols(), src.type());
                Mat erode = new Mat(src.rows(),
                        src.cols(), src.type());

                double dilation_size = 2;

                Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                        new Size(dilation_size, dilation_size));
                Imgproc.dilate(src, dilate, element1);
                //Imgproc.morphologyEx(binaryAdaptative, erodeAndDilate, Imgproc.MORPH_OPEN, element1);

                String fileDilate = imagenOriginal.getParent() + File.separator + Utils.getFileName(imagenOriginal)
                        + "_DILATE2_" + files.indexOf(f) + Utils.getFileExtension(imagenOriginal);
                Imgcodecs.imwrite(fileDilate, dilate);

                double erode_size = 3;
                Mat element2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                        new Size(erode_size, erode_size));
                Imgproc.erode(dilate, erode, element2);
                //Imgproc.morphologyEx(binaryAdaptative, erodeAndDilate, Imgproc.MORPH_OPEN, element1);

                String fileErode = imagenOriginal.getParent() + File.separator + Utils.getFileName(imagenOriginal)
                        + "_ERODE_" + files.indexOf(f) + Utils.getFileExtension(imagenOriginal);

                Imgcodecs.imwrite(fileErode, erode);

                filesDilate.add(new File(fileErode));
            }
        } catch (IOException ex) {
             logger.severe(ex.getMessage());
        }
        return filesDilate;
    }
}