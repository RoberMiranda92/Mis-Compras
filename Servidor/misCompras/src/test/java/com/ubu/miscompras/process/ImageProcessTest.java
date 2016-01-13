/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubu.miscompras.process;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda PÃ©rez</a>
 */
public class ImageProcessTest {
    
    public final String  IMAGE_NAME="TicketPrueba2.jpg";
    public ImageProcessTest() {
    }

    @Test
    public void getProductsFileFromImageTest() {
        
        
        URL image = this.getClass().getClassLoader().getResource(IMAGE_NAME);
        
        ImageProcess i = new ImageProcess(image.getPath());
        ArrayList<File> productos = i.getProductsFileFromImage();

        assertThat("Error en el numero de productos", productos.size(), is(8));
    }

}
