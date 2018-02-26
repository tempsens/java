/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thetempsensserver;

import java.net.Socket;
import java.sql.ResultSet;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jukka Suni / INTIM17A6 / Riihimäki / HAMK
 */
public class DBTest {
    
    public DBTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of connect method, of class DB.
     */
    @Test
    public void testConnect() {
	System.out.println("connect");
	DB instance = new DB();
	instance.connect();
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of disconnect method, of class DB.
     */
    @Test
    public void testDisconnect() {
	System.out.println("disconnect");
	DB instance = new DB();
	instance.disconnect();
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of query method, of class DB.
     */
    @Test
    public void testQuery() {
	System.out.println("query");
	String objects = "";
	String table = "";
	String where = "";
	DB instance = new DB();
	ResultSet expResult = null;
	ResultSet result = instance.query(objects, table, where);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of insertUser method, of class DB.
     */
    @Test
    public void testInsertUser() {
	System.out.println("insertUser");
	int val1 = 0;
	String val2 = "";
	DB instance = new DB();
	instance.insertUser(val1, val2);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of insertTemp method, of class DB.
     */
    @Test
    public void testInsertTemp() {
	System.out.println("insertTemp");
	double val1 = 0.0;
	int val2 = 0;
	String dagen = "";
	DB instance = new DB();
	instance.insertTemp(val1, val2, dagen);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of checkUser method, of class DB.
     */
    @Test
    public void testCheckUser() {
	System.out.println("checkUser");
	String user = "";
	String pass = "";
	DB instance = new DB();
	int expResult = 0;
	int result = instance.checkUser(user, pass);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of listUsers method, of class DB.
     */
    @Test
    public void testListUsers() {
	System.out.println("listUsers");
	Socket soketti = null;
	DB instance = new DB();
	instance.listUsers(soketti);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of listTemps method, of class DB.
     */
    @Test
    public void testListTemps() {
	System.out.println("listTemps");
	Socket soketti = null;
	DB instance = new DB();
	instance.listTemps(soketti);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of GetUsersFromDB method, of class DB.
     */
    @Test
    public void testGetUsersFromDB() {
	System.out.println("GetUsersFromDB");
	DB instance = new DB();
	String expResult = "";
	String result = instance.GetUsersFromDB();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }
    
}
