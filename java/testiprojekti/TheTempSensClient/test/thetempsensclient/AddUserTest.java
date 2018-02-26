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
public class AddUserTest {
    
    public AddUserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of addNewUser method, of class AddUser.
     */
    @Test
    public void testAddNewUser() {
	System.out.println("addNewUser");
	Socket soketti = null;
	AddUser instance = new AddUser();
	instance.addNewUser(soketti);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }
    
}
