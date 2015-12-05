/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubu.miscompras.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
 *
 * @author Roberto
 */
public class ProductProcess {

    private final Tesseract instance;
    private final ArrayList<File> files;
    private Spelling corrector;
    private String ruta = "\\\\Mac\\Home\\Desktop\\TrabajoFinaldeGrado\\Servidor\\misCompras\\src\\Diccionario.txt";

    public ProductProcess(ArrayList<File> files) {
        this.files = files;
        instance = new Tesseract();
        instance.setDatapath(System.getProperty("TESSERACT_DATA_DIR"));
        instance.setPageSegMode(PSM_SINGLE_LINE);
        instance.setOcrEngineMode(OEM_TESSERACT_ONLY);

        try {

            File diccionario = new File(ruta);
            if (!diccionario.exists()) {
                diccionario.createNewFile();
            }
            corrector = new Spelling(diccionario.getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getText() {

        String result = "";

        try {
            for (File f : files) {
                String lineaProducto = instance.doOCR(f);
                result += getCorrectProdut(lineaProducto);
            }
            return result;

        } catch (TesseractException ex) {
            Logger.getLogger(ImageProcess.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private String getCorrectProdut(String lineaProducto) {
        String result = "";
        String lineaProductoCorrecta = deleteUnnecessarySpaces(lineaProducto);
        String producto = getProducto(lineaProductoCorrecta);
        int nSpaces = producto.length() - producto.replace(" ", "").length();;
        int i = 0;
        do {
            if (producto.contains(" ")) {
                int indexEspacio = producto.indexOf(" ");
                String word = producto.substring(0, indexEspacio);
                String correction = corrector.correct(word);
                result += correction + " ";
                producto = producto.substring(indexEspacio + 1);
            } else {
                result += corrector.correct(producto);
            }
            i++;
        } while (i <= nSpaces);
        result = getProductAmount(lineaProductoCorrecta) + " " + result + " " + getProductUnitPrice(lineaProductoCorrecta) + " " + getProductTotal(lineaProductoCorrecta) + "<br>";

        return result;
    }

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

    private String getProductTotal(String lineaProducto) {
        int space = lineaProducto.lastIndexOf(" ");
        String precioTotal = lineaProducto.substring(space + 1, lineaProducto.length() - 1);
        if (precioTotal.contains(",")) {
            precioTotal = precioTotal.replace(",", ".");
        }
        if (precioTotal.contains(":")) {
            precioTotal = precioTotal.replace(":", ".");
        }
        if (precioTotal.contains(";")) {
            precioTotal = precioTotal.replace(";", ".");
        }
        return precioTotal;
    }

    private String getProductUnitPrice(String lineaProducto) {
        int space = lineaProducto.lastIndexOf(" ");
        String temp = lineaProducto.substring(0, space);
        space = temp.lastIndexOf(" ");
        temp = temp.substring(space + 1, temp.length());
        if (temp.contains(",")) {
            temp = temp.replace(",", ".");
        }
        if (temp.contains(":")) {
            temp = temp.replace(":", ".");
        }
        if (temp.contains(";")) {
            temp = temp.replace(";", ".");
        }
        return temp;
    }

    private String getProducto(String lineaProducto) {
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

    private String deleteUnnecessarySpaces(String lineaProducto) {

        if (lineaProducto.contains(" . ")) {
            lineaProducto = lineaProducto.replace(" . ", ".");
        }

        if (lineaProducto.contains(" , ")) {
            lineaProducto = lineaProducto.replace(" , ", ".");
        }

        if (lineaProducto.contains(" ; ")) {
            lineaProducto = lineaProducto.replace(" ; ", ".");
        }
        if (lineaProducto.contains(" : ")) {
            lineaProducto = lineaProducto.replace(" : ", ".");
        }

        if (lineaProducto.contains(". ")) {
            lineaProducto = lineaProducto.replace(". ", ".");
        }

        if (lineaProducto.contains(", ")) {
            lineaProducto = lineaProducto.replace(", ", ".");
        }

        if (lineaProducto.contains("; ")) {
            lineaProducto = lineaProducto.replace("; ", ".");
        }
        if (lineaProducto.contains(": ")) {
            lineaProducto = lineaProducto.replace(": ", ".");
        }

        if (lineaProducto.contains(" .")) {
            lineaProducto = lineaProducto.replace(" .", ".");
        }

        if (lineaProducto.contains(" ,")) {
            lineaProducto = lineaProducto.replace(" ,", ".");
        }

        if (lineaProducto.contains(" ;")) {
            lineaProducto = lineaProducto.replace(" ;", ".");
        }
        if (lineaProducto.contains(" :")) {
            lineaProducto = lineaProducto.replace(" :", ".");
        }

        return lineaProducto;
    }

    private class Spelling {

        private final HashMap<String, Integer> nWords = new HashMap<String, Integer>();

        public Spelling(String file) throws IOException {
            BufferedReader in = new BufferedReader(new FileReader(file));
            Pattern p = Pattern.compile("\\w+");
            for (String temp = ""; temp != null; temp = in.readLine()) {
                Matcher m = p.matcher(temp);
                while (m.find()) {
                    nWords.put((temp = m.group()), nWords.containsKey(temp) ? nWords.get(temp) + 1 : 1);
                }
            }
            in.close();
        }

        public final String correct(String word) {
            int maxDistance = Integer.MAX_VALUE;
            ArrayList<String> posibilidades = new ArrayList<>();
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

        /*
         private final ArrayList<String> edits(String word) {
         ArrayList<String> result = new ArrayList<String>();
         for (int i = 0; i < word.length(); ++i) {
         result.add(word.substring(0, i) + word.substring(i + 1));
         }
         for (int i = 0; i < word.length() - 1; ++i) {
         result.add(word.substring(0, i) + word.substring(i + 1, i + 2) + word.substring(i, i + 1) + word.substring(i + 2));
         }
         for (int i = 0; i < word.length(); ++i) {
         for (char c = 'A'; c <= 'Z'; ++c) {
         result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i + 1));
         }
         }
         for (int i = 0; i <= word.length(); ++i) {
         for (char c = 'A'; c <= 'Z'; ++c) {
         result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
         }
         }
         return result;
         }*/
        /*public final String correct(String word) {
         int maxDistance = Integer.MAX_VALUE;
         ArrayList<String> posibilidades = new ArrayList<>();
         if (nWords.containsKey(word)) {
         return word;
         }
         ArrayList<String> list = edits(word);
         HashMap<Integer, String> candidates = new HashMap<Integer, String>();
         for (String s : list) {
         int distancia = computeLevenshteinDistance(word, s);
         if (distancia < maxDistance) {
         maxDistance = distancia;
         posibilidades.add(s);
         }
         if (nWords.containsKey(s)) {
         candidates.put(nWords.get(s), s.toUpperCase());
         }
         }

         if (candidates.size()
         > 0) {
         return candidates.get(Collections.max(candidates.keySet()));
         }
         maxDistance=Integer.MAX_VALUE;
         for (String s : list) {
         for (String w : edits(s)) {
         int distancia = computeLevenshteinDistance(word, w);
         if (distancia < maxDistance) {
         maxDistance = distancia;
         posibilidades.add(w);
         }
         if (nWords.containsKey(w)) {
         candidates.put(nWords.get(w), w);
         }
         }
         }

         return candidates.size()
         > 0 ? candidates.get(Collections.max(candidates.keySet())) : word;
         }
         }*/
        private int minimum(int a, int b, int c) {
            if (a <= b && a <= c) {
                return a;
            }
            if (b <= a && b <= c) {
                return b;
            }
            return c;
        }

        public int computeLevenshteinDistance(String str1, String str2) {
            return computeLevenshteinDistance(str1.toCharArray(),
                    str2.toCharArray());
        }

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
}
