/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thetempsensclient;

import java.net.Socket;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jukka Suni / INTIM17A6 / Riihimäki / HAMK
 */
public class LoginTest {
    
    public LoginTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of login method, of class Login.
     */
    @Test
    public void testLogin() {
	System.out.println("login");
	Socket soketti = null;
	Login instance = new Login();
	String expResult = "";
	String result = instance.login(soketti);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }
    
}
