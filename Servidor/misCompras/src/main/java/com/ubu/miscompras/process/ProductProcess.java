/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubu.miscompras.process;

import com.ubu.miscompras.exceptions.MisComprasException;
import com.ubu.miscompras.utils.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static net.sourceforge.tess4j.ITessAPI.TessOcrEngineMode.OEM_TESSERACT_ONLY;
import static net.sourceforge.tess4j.ITessAPI.TessPageSegMode.PSM_SINGLE_LINE;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * Esta clase se encarga de obtener el texto a partir de una lista de
 * imagenes
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Perez</a>
 */
public class ProductProcess {

    private final Tesseract instance;
    private final ArrayList<File> files;
    private final HashMap<String, Integer> nWords = new HashMap<>();
    private final String ruta = Utils.RUTA_DICCIONARIO;
    private final String csvFile = Utils.RUTA_CSV;
    private final String absolutePath;

    /**
     * Constructor de la clase que recibe como argumento una lista de
     * imgenes para analizar el texto
     *
     * @param files lista de imagenes
     */
    public ProductProcess(ArrayList<File> files) {

        this.files = files;
        instance = new Tesseract();
        instance.setDatapath(System.getProperty("TESSERACT_DATA_DIR"));
        instance.setPageSegMode(PSM_SINGLE_LINE);
        instance.setOcrEngineMode(OEM_TESSERACT_ONLY);
        instance.setTessVariable("tessedit_char_blacklist", "`?:");
        
        File classPath = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        String path = classPath.getAbsolutePath();

        while (!path.endsWith("target")) {
            int index;
            index = path.lastIndexOf(File.separator);
            path = path.substring(0, index);
        }
        
        absolutePath= path;

        try {
            File diccionario = new File(absolutePath+ File.separator + ruta);
            System.out.print(diccionario.getCanonicalPath());
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
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Este metodo devuelve el texto de una lista de archivos de imagen.
     *
     * @return texto obtenido
     * @throws MisComprasException Devuelve una excepcion si la lista es nula
     * o vacia. o si hay algun error al procesar el texto.
     *
     */
    public String getText() throws MisComprasException {

        String result = "";
        if (files == null || files.isEmpty()) {
            throw new MisComprasException(MisComprasException.FICHEROS_PRODUCTOS_ERROR);
        }
        try {
            for (File f : files) {
                String lineaProducto = instance.doOCR(f);

                result += getCorrectProdut(lineaProducto);
            }
            return result;

        } catch (TesseractException ex) {
            throw new MisComprasException(MisComprasException.ERROR_TESSERACT);
        }
    }

    /**
     * Este metodo devuelve el producto de una linea de producto correguido
     *
     * @param lineaProducto linea de producto
     * @return producto de una linea de producto.
     */
    public String getCorrectProdut(String lineaProducto) {
        String result = "";
        String lineaProductoCorrecta = deleteUnnecessarySpaces(lineaProducto);
        String producto = getProduct(lineaProductoCorrecta);
        int nSpaces = producto.length() - producto.replace(" ", "").length();;
        int i = 0;
        do {
            if (producto.contains(" ")) {
                int indexEspacio = producto.indexOf(" ");
                String word = producto.substring(0, indexEspacio);
                String correction = correct(word);
                result += correction + " ";
                producto = producto.substring(indexEspacio + 1);
            } else {
                result += correct(producto);
            }
            i++;
        } while (i <= nSpaces);

        String cantidad = getProductAmount(lineaProductoCorrecta);

        String precioUnitario = getProductUnitPrice(lineaProductoCorrecta);
        String total = getProductTotal(lineaProductoCorrecta);
        boolean correct = isProductPricesCorrect(cantidad, precioUnitario, total);

        if (!correct) {
            cantidad = correctNumber(cantidad);
            precioUnitario = correctNumber(precioUnitario);
            total = correctNumber(total);
        }

        if (cantidad.length() > 1) {
            cantidad = "" + cantidad.charAt(0);
        }

        result = cantidad + " " + result + " " + precioUnitario + " " + total;

        return result;
    }

    /**
     * Este metodo devuelve la catidad de productos de una linea de producto.
     *
     * @param lineaProducto linea deproducto
     * @return cantidad de una linea de producto.
     */
    private String getProductAmount(String lineaProducto) {
        int space = lineaProducto.indexOf(" ");
        String amount = "";
        if (space != -1) {
            amount = lineaProducto.substring(0, space);
        } else {
            amount = lineaProducto;
        }
        if (amount.contains(",")) {
            amount = amount.replace(",", ".");
        }
        if (amount.contains(":")) {
            amount = amount.replace(":", ".");
        }
        if (amount.contains(";")) {
            amount = amount.replace(";", ".");
        }

        return amount;
    }

    /**
     * Este metodo devuelve el total de una linea de producto
     *
     * @param lineaProducto linea de producto
     * @return total de la linea de producto.
     */
    private String getProductTotal(String lineaProducto) {
        int space = lineaProducto.lastIndexOf(" ");
        String precioTotal = lineaProducto.substring(space + 1, lineaProducto.length());

        if (!precioTotal.contains(".")) {
            precioTotal = precioTotal.substring(0, 1) + "." + precioTotal.substring(1);
        }

        return precioTotal;
    }

    /**
     * Este metodo devuelve el precio unitario de una linea de producto.
     *
     * @param lineaProducto linea de producto
     * @return precio del producto.
     */
    private String getProductUnitPrice(String lineaProducto) {
        int space = lineaProducto.lastIndexOf(" ");
        String price = lineaProducto.substring(0, space);
        space = price.lastIndexOf(" ");
        price = price.substring(space + 1, price.length());

        return price;
    }

    /**
     * Este metodo devuelve el producto de una linea de producto.
     *
     * @param lineaProducto linea de producto
     * @return producto de una linea de producto
     */
    private String getProduct(String lineaProducto) {
        int space = lineaProducto.indexOf(" ");
        String temp = lineaProducto.substring(space + 1);
        space = temp.lastIndexOf(" ");
        if (space != -1) {
            temp = temp.substring(0, space);
        } else {
            return temp;
        }
        space = temp.lastIndexOf(" ");
        if (space != -1) {
            return temp.substring(0, space);
        } else {
            return temp;
        }
    }

    /**
     * Este metodo elimina los espacios inecesarios que pueden encontrarse entre
     * los numeros de una linea de producto
     *
     *
     * @param lineaProducto con espacios invalidos
     * @return linea de productos sin espacios invalidos
     */
    private String deleteUnnecessarySpaces(String lineaProducto) {

        String[] characters = {".", ",", ":", ";",};
        String remplaceCharacter = "";
        for (int i = 0; i < characters.length; i++) {

            remplaceCharacter += characters[i];

        }
        String pattern = "(\\s*)([\\" + remplaceCharacter + "])(\\s*)";
        lineaProducto = lineaProducto.replaceAll(pattern, ".");

        return lineaProducto;
    }

    /**
     * Este metodo remplaza las letras de un nÃƒÂºmero por su corresponiente
     * valor del archivo csv facilitado.
     *
     * @param n numero a transormar
     * @return numero transofrmado de letras a numerico.
     */
    private String correctNumber(String n) {
        boolean isNumeric = true;

        String newNumero = n;

        try {
            double numero = Double.parseDouble(n);
            return n;
        } catch (NumberFormatException ex) {
            isNumeric = false;
        }
        if (!isNumeric) {
            for (int i = 0; i < n.length(); i++) {
                char character = n.charAt(i);
                if (!Character.isDigit(character)) {
                    ArrayList<String> ocurrences = getOcurrences(character);
                    if (!ocurrences.isEmpty()) {
                        newNumero = newNumero.replace("" + character, ocurrences.get(0));
                    }
                }
            }
        }

        newNumero = newNumero.replace(".", "");

        newNumero = newNumero.substring(0, 1) + "." + newNumero.substring(1);

        return newNumero;
    }

    /**
     * Este metodo devuelve true si cantidad multiplicada por el precio unitario
     * es igual al total, si no devuelve falso.
     *
     * @param cantidad cantidad de un producto.
     * @param precioUnidad precio unitario del producto.
     * @param total coste total de el producto.
     * @return true si cantidad*precioUnidad=total, si no false
     */
    private boolean isProductPricesCorrect(String cantidad, String precioUnidad, String total) {

        try {
            Double c = Double.parseDouble(cantidad);
            Double pU = Double.parseDouble(precioUnidad);
            Double t = Double.parseDouble(total);

            return c * pU == t;

        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Este metodo devuelve las ocurrecias encontradas en el archivo csv para
     * un caracter
     *
     * @param character caracter del que se desean las ocurrencias
     * @return lista de ocurrencias
     */
    private ArrayList<String> getOcurrences(char character) {
        ArrayList<String> ocurrences = new ArrayList<>();

        BufferedReader br = null;
        String row = "";
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(absolutePath+ File.separator +csvFile));
            while ((row = br.readLine()) != null) {
                String[] line = row.split(cvsSplitBy);

                if (line[0].charAt(0) == character) {
                    for (int i = 1; i < line.length; i++) {
                        ocurrences.add(line[i]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ocurrences;
    }

    /**
     * Este metodo sustituye una palabra por la palabra mas cercana segun
     * la distancia de levenshtein
     *
     * @param word palabra a sustituir.
     * @return palabra mas cercana.
     */
    public final String correct(String word) {
        int maxDistance = Integer.MAX_VALUE;
        String posible = word;
        if (nWords.containsKey(word)) {
            return word;
        }

        for (String s : nWords.keySet()) {
            int distancia = computeLevenshteinDistance(word.toUpperCase(), s.toUpperCase());
            if (distancia < maxDistance) {
                maxDistance = distancia;
                posible = s;
            }
        }
        return posible.toUpperCase();

    }

    private int minimum(int a, int b, int c) {
        if (a <= b && a <= c) {
            return a;
        }
        if (b <= a && b <= c) {
            return b;
        }
        return c;
    }

    /**
     * Este metodo devuelve la distancia de levenshtein entre 2 palabras.
     *
     * @param str1 palabra 1
     * @param str2 palabra 2
     * @return distancia
     */
    private int computeLevenshteinDistance(String str1, String str2) {
        return computeLevenshteinDistance(str1.toCharArray(),
                str2.toCharArray());
    }

    /**
     * Este metodoo devuelve la distancia de levenshtein entre 2 palabras.
     *
     * @param str1 palabra 1
     * @param str2 palabra 2
     * @return distancia
     */
    private int computeLevenshteinDistance(char[] str1, char[] str2) {
        int[][] distance = new int[str1.length + 1][str2.length + 1];

        for (int i = 0; i <= str1.length; i++) {
            distance[i][0] = i;
        }
        for (int j = 0; j <= str2.length; j++) {
            distance[0][j] = j;
        }
        for (int i = 1; i <= str1.length; i++) {
            for (int j = 1; j <= str2.length; j++) {
                distance[i][j] = minimum(distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1]
                        + ((str1[i - 1] == str2[j - 1]) ? 0 : 1));
            }
        }
        return distance[str1.length][str2.length];

    }

}
