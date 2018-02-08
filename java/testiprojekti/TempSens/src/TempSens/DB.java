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
//import java.util.regex.Pattern;
//import static jdk.nashorn.tools.ShellFunctions.input;

/**
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
            //	    conn = DriverManager.getConnection("jdbc:mysql://c3-suncomet.com/XXXXXXXX_tempsens?"
            //      + "user=XXXX_XXXXX&password=XXXXXXX"); // MUISTA LISÄTÄ TIETOKANNAN NIMI, USER JA PWD

            conn = DriverManager.getConnection("jdbc:mysql://c3-suncomet.com/juksohia_tempsens?"
                    + "user=juksohia_sensor&password=!sensor1"); // MUISTA LISÄTÄ TIETOKANNAN NIMI, USER JA PWD
        } catch (SQLException ex) {
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

    public void insertUser(int val1, String val2) {
         this.connect();
        try {
            String query = "INSERT INTO users (userlvl, username, password) VALUES(" + val1 + ", '" + val2 + "', '" + val2 + "')";
            // String query = "INSERT INTO temps (value) VALUES(0.0);";
            // System.out.println(query);
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
          this.disconnect();
    }

    public void insertTemp(double val1, int val2) {
        // this.connect();
        try {
            String query = "INSERT INTO temps (value, sensor) VALUES ('" + val1 + "', " + val2 + ")";
            // String query = "INSERT INTO temps (value) VALUES(0.0);";
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
        // this.disconnect();
    }

    public int checkUser(String user, String pass) {

        int leveli = 0;
        try {
            String query = "SELECT userlvl FROM users WHERE username='" + user + "' AND password='" + pass + "'";
            // String query = "INSERT INTO temps (value) VALUES(0.0);";
            // System.out.println(query);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                leveli = rs.getInt(1);

            }
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        disconnect();

        return leveli;
        
    }
         public void listUsers()  {
              this.connect();   
        try {
            String query = "SELECT username FROM users";
            // String query = "INSERT INTO temps (value) VALUES(0.0);";
            // System.out.println(query);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
             
                System.out.println(rs.getString("username"));
            }
          

            
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        disconnect();
         }
         public void listTemps()  {
              this.connect();   
        try {
            String query = "SELECT paivays, value, sensor  FROM temps";
            // String query = "INSERT INTO temps (value) VALUES(0.0);";
            // System.out.println(query);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
             
                System.out.println(rs.getString(1) + "\t" +  rs.getDouble(2) + "\t" + rs.getInt(3));
            }
          

            
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        disconnect();
         }
}
