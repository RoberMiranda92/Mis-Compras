package com.ubu.miscompras.utils;

import java.io.File;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Roberto
 */
public class Utils {

    public static String extension(File file) {
        int dot = file.getName().lastIndexOf(".");
        return file.getName().substring(dot);
    }

    public static String filename(File file) {
        int dot = file.getName().lastIndexOf(".");
        return file.getName().substring(0, dot);
    }
    
}
