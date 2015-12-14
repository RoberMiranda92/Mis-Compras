/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubu.miscompras.utils;

import java.io.File;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class UtilsTest {

    public UtilsTest() {
    }

    @Test
    public void getFileExtensionTest() {

        String file = System.getProperty("user.dir") + File.separator + "misCompras-1.0-SNAPSHOT" + File.separator + "testfiles" + File.separator + "TicketPrueba.jpg";

        String extension = Utils.getFileExtension(new File(file));

        assertThat("Error obteniendo extension de fichero", extension, is(".jpg"));
    }

    /**
     * Test of filename method, of class Utils.
     */
    @Test
    public void testFilename() {
        String file = System.getProperty("user.dir") + File.separator + "misCompras-1.0-SNAPSHOT" + File.separator + "testfiles" + File.separator + "TicketPrueba.jpg";

        String extension = Utils.getFileName(new File(file));

        assertThat("Error obteniendo nombre de fichero", extension, is("TicketPrueba"));
    }

}
