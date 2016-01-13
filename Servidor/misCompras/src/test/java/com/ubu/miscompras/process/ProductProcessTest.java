/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubu.miscompras.process;

import com.ubu.miscompras.exceptions.MisComprasException;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda PÃƒÆ’Ã‚Â©rez</a>
 */
public class ProductProcessTest {

    public final String DICCIONARY_NAME = "Diccionario.txt";
    public final String CSV_NAME = "Ocurrencias.csv";
    public final  String IMAGE_NAME = "TicketPrueba.jpg";
    private String diccionaryPath;
    private String csvPath;
    

    public ProductProcessTest() {

    }

    @Before
    public void setUp() {
        URL urlDiccionary = this.getClass().getClassLoader().getResource(DICCIONARY_NAME);
        URL urlCsv = this.getClass().getClassLoader().getResource(CSV_NAME);

        diccionaryPath = urlDiccionary.getPath();
        csvPath = urlCsv.getPath();

    }

    @Test
    public void getTextWithSpacesnTest() throws MisComprasException {

        String productLine = "1 . 00 REFRESCOS DE COLA 1, 2O 1 :2O";

        ProductProcess p = new ProductProcess(null, diccionaryPath, csvPath);
        String lineaCorrecta = p.getCorrectProdut(productLine);

        assertThat("Error con espacios invalidos", lineaCorrecta, is("1 REFRESCOS DE COLA 1.20 1.20"));

    }

    @Test
    public void getTextIncorrectTest() throws MisComprasException {

        String productLine = "1.00 RER3SCS D3 CAL4 1.20 1.20";

        ProductProcess p = new ProductProcess(null,diccionaryPath,csvPath);
        String lineaCorrecta= p.getCorrectProdut(productLine);

        assertThat("Error con espacios invalidos", lineaCorrecta, is("1 REFRESCOS DE COLA 1.20 1.20"));
     
      }
     
      @Test
    public void getTextInvalidCharactersTest() throws
            MisComprasException {

        String productLine = "LOO REFRESCOS DE COLA 1.20 l2U";

        ProductProcess p = new ProductProcess(null,diccionaryPath,csvPath);
        String lineaCorrecta= p.getCorrectProdut(productLine);

        assertThat("Error con espacios invalidos", lineaCorrecta, is("1 REFRESCOS DE COLA 1.20 1.20"));
     
      }
     
      @Test(expected = MisComprasException.class)
    public void
            getTextNullExceptionTest() throws MisComprasException {

        String productLine = "1.00 RER3SCS D3 CAL4 1.2O 1.2O";

        ProductProcess p = new ProductProcess(null,diccionaryPath,csvPath);
        String lineaCorrecta= p.getText();

    }

    @Test(expected = MisComprasException.class)
    public void
            getTextEmptyFilesExceptionTest() throws MisComprasException {

        String productLine = "1.00 RER3SCS D3 CAL4 1.2O 1.2O";

        ProductProcess p = new ProductProcess(new ArrayList<File>(), diccionaryPath, csvPath);
        String lineaCorrecta = p.getText();

    }

    @Test
    public void getTextTest() {
        String productLine = "2 REFRESCOS 1.70 3.40\n\n";
        
        URL imageUrl = this.getClass().getClassLoader().getResource(IMAGE_NAME);
 
        File image = new File(imageUrl.getFile());
        String path
                = image.getAbsolutePath();

        ArrayList<File> files = new ArrayList<>();
        files.add(image);

        ProductProcess p = new ProductProcess(files, diccionaryPath, csvPath);

        try {
            String lineaObtenida = p.getText();
            assertThat("Error en get text",
                    lineaObtenida, is(productLine));
        } catch (MisComprasException ex) {
            fail("Error en get text");
        }
    }

}
