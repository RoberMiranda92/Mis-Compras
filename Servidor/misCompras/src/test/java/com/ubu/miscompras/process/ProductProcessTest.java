/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubu.miscompras.process;

import com.ubu.miscompras.exceptions.MisComprasException;
import java.io.File;
import java.util.ArrayList;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda PÃƒÆ’Ã‚Â©rez</a>
 */
public class ProductProcessTest {

    public ProductProcessTest() {
    }
    
    @Test
    public void getTextWithSpacesnTest() throws MisComprasException {

        String productLine = "1 . 00 REFRESCOS DE COLA 1, 2O 1 :2O";

        ProductProcess p = new ProductProcess(null);
        String lineaCorrecta = p.getCorrectProdut(productLine);

        assertThat("Error con espacios invÃ¡lidos", lineaCorrecta, is("1 REFRESCOS DE COLA 1.20 1.20"));

    }
    
    @Test
    public void getTextIncorrectTest() throws MisComprasException {

        String productLine = "1.00 RER3SCS D3 CAL4 1.20 1.20";

        ProductProcess p = new ProductProcess(null);
        String lineaCorrecta = p.getCorrectProdut(productLine);
        
        assertThat("Error con espacios invÃ¡lidos", lineaCorrecta, is("1 REFRESCOS DE COLA 1.20 1.20"));

    }
    
    @Test
    public void getTextInvalidCharactersTest() throws MisComprasException {

        String productLine = "LOO REFRESCOS DE COLA 1.20 l2U";

        ProductProcess p = new ProductProcess(null);
        String lineaCorrecta = p.getCorrectProdut(productLine);
        
        assertThat("Error con espacios invÃ¡lidos", lineaCorrecta, is("1 REFRESCOS DE COLA 1.20 1.20"));

    }

    @Test(expected = MisComprasException.class)
    public void getTextNullExceptionTest() throws MisComprasException {

        String productLine = "1.00 RER3SCS D3 CAL4 1.2O 1.2O";

        ProductProcess p = new ProductProcess(null);
        String lineaCorrecta = p.getText();

    }

    @Test(expected = MisComprasException.class)
    public void getTextEmptyFilesExceptionTest() throws MisComprasException {

        String productLine = "1.00 RER3SCS D3 CAL4 1.2O 1.2O";

        ProductProcess p = new ProductProcess(new ArrayList<File>());
        String lineaCorrecta = p.getText();

    }

    @Test
    public void getTextTest() {
        String productLine = "2 REFRESCOS 1.70 3.40\n\n";

        File image = new File("test" + File.separator + "resources" + File.separator + "TicketPrueba.jpg");
        String path = image.getAbsolutePath();

        ArrayList<File> files = new ArrayList<>();
        files.add(image);

        ProductProcess p = new ProductProcess(files);

        try {
            String lineaObtenida = p.getText();
            assertThat("Error en get text", lineaObtenida, is(productLine));
        } catch (MisComprasException ex) {
            fail("Error en get text");
        }
    }

}
