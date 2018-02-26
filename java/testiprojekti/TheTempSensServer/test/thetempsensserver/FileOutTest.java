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
public class FileOutTest {
    
    public FileOutTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of loginError method, of class FileOut.
     */
    @Test
    public void testLoginError() {
	System.out.println("loginError");
	String viesti = "";
	FileOut instance = new FileOut();
	int expResult = 0;
	int result = instance.loginError(viesti);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of loginSuccess method, of class FileOut.
     */
    @Test
    public void testLoginSuccess() {
	System.out.println("loginSuccess");
	String viesti = "";
	FileOut instance = new FileOut();
	int expResult = 0;
	int result = instance.loginSuccess(viesti);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of console method, of class FileOut.
     */
    @Test
    public void testConsole() {
	System.out.println("console");
	String viesti = "";
	Socket soketti = null;
	FileOut instance = new FileOut();
	instance.console(viesti, soketti);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of userlist method, of class FileOut.
     */
    @Test
    public void testUserlist() {
	System.out.println("userlist");
	Socket soketti = null;
	FileOut instance = new FileOut();
	instance.userlist(soketti);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }
    
}
