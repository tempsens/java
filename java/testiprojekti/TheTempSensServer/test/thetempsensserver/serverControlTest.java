/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thetempsensserver;

import java.net.Socket;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jukka Suni / INTIM17A6 / Riihimäki / HAMK
 */
public class serverControlTest {
    
    public serverControlTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of start method, of class serverControl.
     */
    @Test
    public void testStart() {
	System.out.println("start");
	Socket soketti = null;
	serverControl instance = new serverControl();
	instance.start(soketti);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of stop method, of class serverControl.
     */
    @Test
    public void testStop() {
	System.out.println("stop");
	Socket soketti = null;
	serverControl instance = new serverControl();
	instance.stop(soketti);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of restart method, of class serverControl.
     */
    @Test
    public void testRestart() {
	System.out.println("restart");
	Socket soketti = null;
	serverControl instance = new serverControl();
	instance.restart(soketti);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getStatus method, of class serverControl.
     */
    @Test
    public void testGetStatus() {
	System.out.println("getStatus");
	serverControl instance = new serverControl();
	int expResult = 0;
	int result = instance.getStatus();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }
    
}
