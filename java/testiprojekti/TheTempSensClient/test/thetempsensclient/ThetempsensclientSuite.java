/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thetempsensclient;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Jukka Suni / INTIM17A6 / Riihimäki / HAMK
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({thetempsensclient.LoginTest.class, thetempsensclient.AddUserTest.class, thetempsensclient.TheTempSensClientTest.class})
public class ThetempsensclientSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
}
