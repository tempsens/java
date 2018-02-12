/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TempSens;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author PetShopBoys
 */
public class DB {

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    PreparedStatement preparedStatement = null;

// YHTEYDEN MUODOSTUS
    public void connect() {
	try {
	    	    conn = DriverManager.getConnection("jdbc:mysql://c3-suncomet.com/XXXXXXXX_tempsens?"
	         + "user=XXXX_XXXXX&password=XXXXXXX"); // MUISTA LISÄTÄ TIETOKANNAN NIMI, USER JA PWD

	  

	} catch (SQLException ex) {		// handle any errors
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}
    }

    public void disconnect() {
	if (rs != null) {
	    try {
		rs.close();
	    } catch (SQLException sqlEx) { }		// handle any errors - EMPTY
	    rs = null;
	}
	if (stmt != null) {
	    try {
		stmt.close();
	    } catch (SQLException sqlEx) { }		// handle any errors - EMPTY
	    stmt = null;
	}
    }
    
// KYSELY
    /**
     *
     * @param objects	- Mitkä kentät valitaan
     * @param table	- Taulun nimi
     * @param where	- Hakuehdot (WHERE value > 20.0 AND value < 30.0)
     * @return	    - Palauttaa kaikkien valittujen kenttien (objects) arvot
     */
    public ResultSet query(String objects, String table, String where) {
	try {
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery("SELECT " + objects + " FROM " + table + " " + where + " LIMIT 100");
	} catch (SQLException ex) {
	    // handle any errors
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}
	return rs;
    }

// Käyttäjän lisääminen
    public void insertUser(int val1, String val2) {
	this.connect();
	try {
	    String query = "INSERT INTO users (userlvl, username, password) VALUES(" + val1 + ", '" + val2 + "', '" + val2 + "')";
	    stmt = conn.createStatement();
	    if (stmt.execute(query)) {
		rs = stmt.getResultSet();
	    }
	} catch (SQLException ex) {		// handle any errors
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}
	this.disconnect();
    }

// Lämpötilan lisääminen
    public void insertTemp(double val1, int val2) {
	//this.connect();
	try {
	    String query = "INSERT INTO temps (value, sensor) VALUES ('" + val1 + "', " + val2 + ")";
	    System.out.println(query);
	    stmt = conn.createStatement();
	    if (stmt.execute(query)) {
		rs = stmt.getResultSet();
	    }
	} catch (SQLException ex) {		// handle any errors
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}
	//this.disconnect();
    }

// Käyttäjätietojen tarkastaminen ("login")
    public int checkUser(String user, String pass) {
	int leveli = 0;
	try {
	    String query = "SELECT userlvl FROM users WHERE username='" + user + "' AND password='" + pass + "'";
	    stmt = conn.createStatement();
	    ResultSet resSet = stmt.executeQuery(query);
	    FileOut fileout = new FileOut();
	    if (resSet.next()) {
		fileout.loginSuccess("u:" + user);
		leveli = resSet.getInt(1);
	    } else {				// Login failures to logfile -Suni-
		fileout.loginError("u:" + user);
	    }
	} catch (SQLException ex) {		// handle any errors
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}
	disconnect();

	return leveli;

    }

// Käyttäjien listaaminen tietokannasta
    public void listUsers() {
	this.connect();
	try {
	    String query = "SELECT username FROM users";
	    stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(query);
	    while (rs.next()) {

		System.out.println(rs.getString("username"));
	    }
	} catch (SQLException ex) {		// handle any errors
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}
	disconnect();
    }

// Lämpötila-arvojen listaaminen tietokannasta	    -- FROM temps LIMIT 50 pitäis lisätä?
    public void listTemps() {
	this.connect();
	try {
	    String query = "SELECT paivays, value, sensor  FROM temps";
	    stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(query);
	    while (rs.next()) {

		System.out.println(rs.getString(1) + "\t" + rs.getDouble(2) + "\t" + rs.getInt(3));
	    }
	} catch (SQLException ex) {		// handle any errors
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}
	disconnect();
    }
     public String GetUsersFromDB(){
         this.connect();
         String palautus = "USERS LIST" + "\r\n\r\n" + "nro" + "\t" + "Username" + "\t" + "Userlevel" + "\r\n" + "---------------------------------------------------------" + "\r\n";
         int i = 1;

         try {
             String query = "SELECT username, userlvl FROM users ORDER BY username ASC";
             stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query);
             while (rs.next()) {

                 palautus += Integer.toString(i) + "\t" + rs.getString(1) + "\t" + rs.getString(2) + "\r\n";
             }
         } catch (SQLException ex) {		// handle any errors
             System.out.println("SQLException: " + ex.getMessage());
             System.out.println("SQLState: " + ex.getSQLState());
             System.out.println("VendorError: " + ex.getErrorCode());
         }
         disconnect();
         return palautus;
    
    }
}
