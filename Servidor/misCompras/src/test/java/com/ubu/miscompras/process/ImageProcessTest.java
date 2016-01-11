/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubu.miscompras.process;

import java.io.File;
import java.util.ArrayList;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class ImageProcessTest {

    public ImageProcessTest() {
    }

    @Test
    public void getProductsFileFromImageTest() {

        File image = new File("src"+File.separator+"test" + File.separator + "resources" + File.separator + "TicketPrueba2.jpg");
        String path = image.getAbsolutePath();

        ImageProcess i = new ImageProcess(path);
        ArrayList<File> productos = i.getProductsFileFromImage();

        assertThat("Error en el número de productos", productos.size(), is(8));
    }

}
