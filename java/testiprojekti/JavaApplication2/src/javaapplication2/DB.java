/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author juksu
 */
public class DB {

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    PreparedStatement preparedStatement = null;

// YHTEYDEN MUODOSTUS
    public void connect() {
	try {
	    conn = DriverManager.getConnection("jdbc:mysql://localhost/oma1?"
		    + "user=root&password=Passu0");
	} catch (SQLException ex) {
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}
    }

// KYSELY
    /**
     *
     * @param objects
     * @param table
     * @param where
     * @return
     */
    public ResultSet query(String objects, String table, String where) {
	try {
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery("SELECT " + objects + " FROM " + table + " " + where + " LIMIT 100");
	    /*
	    // TOISTA "num_rows"
	    while (rs.next()) {
		System.out.println("id=" + rs.getString(1) + " kuvaus=" + rs.getString(2) + " timestamp=" + rs.getString(3));
	    }
	     */
	} catch (SQLException ex) {
	    // handle any errors
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}
	return rs;
    }

    public void insert(String table, String col, String value) {
	try {
	    String query = "INSERT INTO "+table+" ("+col+") VALUES ('"+value+"')";
	    System.out.println(query);
	    stmt = conn.createStatement();
            if (stmt.execute(query)) {
                rs = stmt.getResultSet();
            }
	} catch (SQLException ex) {
	    // handle any errors
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	} 
    }
    
    public void disconnect() {
	if (rs != null) {
	    try {
		rs.close();
	    } catch (SQLException sqlEx) {
	    } // ignore
	    rs = null;
	}

	if (stmt != null) {
	    try {
		stmt.close();
	    } catch (SQLException sqlEx) {
	    } // ignore
	    stmt = null;
	}
    }
}
