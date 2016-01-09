package com.ubu.miscompras.contract;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Roberto
 */
public class ProductContractTest {

    private String productLine = "1.00 REFRESCO DE COLA 1.20 1.20";

    public ProductContractTest() {

    }
    

    @Test
    public void buildJSONText() {

        String producto = ProductContract.buildJSON(productLine).toString();

        Assert.assertThat("Error builder JSON", producto, is("{\"nombre\":\"REFRESCO DE COLA\",\"total\":\"1.20\",\"precio\":\"1.20\",\"cantidad\":\"1.00\"}"));

    }
}
