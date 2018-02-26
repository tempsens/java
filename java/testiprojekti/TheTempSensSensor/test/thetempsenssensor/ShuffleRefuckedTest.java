/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thetempsenssensor;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jukka Suni / INTIM17A6 / Riihimäki / HAMK
 */
public class ShuffleRefuckedTest {
    
    public ShuffleRefuckedTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of ShuffleTemp method, of class ShuffleRefucked.
     */
    @Test
    public void testShuffleTemp() {
	System.out.println("ShuffleTemp");
	int month = 0;
	ShuffleRefucked instance = new ShuffleRefucked();
	String expResult = "";
	String result = instance.ShuffleTemp(month);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }
    
}
