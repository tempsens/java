//  Versio 0.2  26.2.2018    
//              Siirrettiin login ulos isosta silmukasta, toimii
//------------------------------------------------------------------------------
package thetempsensserver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
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
            // 	    conn = DriverManager.getConnection("jdbc:mysql://c3-suncomet.com/XXXXXXXX_tempsens?"
            //     + "user=XXXX_XXXXX&password=XXXXXXX"); // MUISTA LISÄTÄ TIETOKANNAN NIMI, USER JA PWD
            conn = DriverManager.getConnection("jdbc:mysql://c3-suncomet.com/juksohia_tempsens?"
                    + "user=juksohia_user&password=!kayttaja1"); // MUISTA LISÄTÄ TIETOKANNAN NIMI, USER JA PWD

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
            } catch (SQLException sqlEx) {
            }		// handle any errors - EMPTY
            rs = null;
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException sqlEx) {
            }		// handle any errors - EMPTY
            stmt = null;
        }
    }

// KYSELY
    /**
     *
     * @param objects	- Mitkä kentät valitaan
     * @param table	- Taulun nimi
     * @param where	- Hakuehdot (WHERE value > 20.0 AND value < 30.0) @ return	-
     * Palauttaa kaikkien valittujen kenttien (objects) arvot
     *
     */
    public ResultSet query(String objects, String table, String where) {
        try {
            this.connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT " + objects + " FROM " + table + " " + where + " LIMIT 100");
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        this.disconnect();
        return rs;
    }

// Käyttäjän lisääminen
    public void insertUser(int val1, String val2, String IP) {            //SQL INJECTION SAFE
        this.connect();
        FileOut fileout = new FileOut();
        try {
            preparedStatement = conn.prepareStatement("INSERT INTO users (userlvl, username, password) VALUES(?, ?, ?)");
            preparedStatement.setInt(1, val1);
            preparedStatement.setString(2, val2);
            preparedStatement.setString(3, val2);

            //  String query = "INSERT INTO users (userlvl, username, password) VALUES(" + val1 + ", '" + val2 + "', '" + val2 + "')";
            // stmt = conn.createStatement();
            if (preparedStatement.execute()) {
                rs = preparedStatement.getResultSet();
            }
            System.out.println("User added to database.");

            fileout.clientEventLog("New User added ( usr: " + val1 + " / lvl: " + val2, IP);

        } catch (SQLException ex) {		// handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            fileout.clientEventLog("New User Add FAIL ( usr: " + val1 + " / lvl: " + val2, IP);

        }
        this.disconnect();
    }

// Lämpötilan lisääminen
    public void insertTemp(double val1, int val2, String dagen) {            //SQL INJECTION SAFE  IF NEEDED
        //this.connect();
        try {
            preparedStatement = conn.prepareStatement("INSERT INTO temps (value, sensor) VALUES (?, ?)");
            preparedStatement.setDouble(1, val1);
            preparedStatement.setInt(2, val2);

            //   String query = "INSERT INTO temps (value, sensor) VALUES ('" + val1 + "', " + val2 + ")";
            // System.out.println(query); // Print query - just for debugging
            // stmt = conn.createStatement();
            if (preparedStatement.execute()) {
                rs = preparedStatement.getResultSet();

            }
            System.out.println(dagen + " Temperature " + val1 + " \tadded to database from sensor " + val2);
        } catch (SQLException ex) {		// handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        //this.disconnect();
    }

// Käyttäjätietojen tarkastaminen ("login")
    public int checkUser(Socket clinu, String user, String pass, String IP) {            //SQL INJECTION SAFE
        int leveli = 0;
        try {
            if (conn == null) {
                return -1;
            } else {
                System.out.println("CONN = " + conn);
                preparedStatement = conn.prepareStatement("SELECT userlvl FROM users WHERE username=? AND password=?");
                preparedStatement.setString(1, user);
                preparedStatement.setString(2, pass);

                //  String query = "SELECT userlvl FROM users WHERE username='" + user + "' AND password='" + pass + "'";
                //  stmt = conn.createStatement();
//            ResultSet resSet = stmt.executeQuery(query);
                ResultSet resSet = preparedStatement.executeQuery();
                FileOut fileout = new FileOut();
                if (resSet.next()) {
                    System.out.println("Login from: " + clinu.getRemoteSocketAddress().toString() // -Jukka-
                            + " as '" + user + "' successfull.");				    // -Jukka-
                    fileout.loginSuccess("u:" + user, clinu.getRemoteSocketAddress().toString());
                    leveli = resSet.getInt(1);
                } else {
                    System.out.println("Login from: " + clinu.getRemoteSocketAddress().toString() // -Jukka-
                            + " as '" + user + "' FAIL!");					    // -Jukka-
                    fileout.loginError("u:" + user, clinu.getRemoteSocketAddress().toString());
                }
                disconnect();
                return leveli;
            }
        } catch (SQLException ex) {		// handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            disconnect();
            return -1;				// Palauttaa -1 virheessä -Suni-
        }
    }

// Käyttäjien listaaminen tietokannasta
    public void listUsers(Socket soketti) { // NO NEED FOR SQL INJECTION PROTECTION
        this.connect();
        FileOut fileout = new FileOut();

        try {
            PrintStream os = new PrintStream(soketti.getOutputStream());
            String query = "SELECT username FROM users";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                os.println(rs.getString("username"));
            }
            //   os.println("QQ");

            fileout.clientEventLog("List users", soketti.getRemoteSocketAddress().toString());

        } catch (SQLException ex) {		// handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            fileout.clientEventLog("List users FAIL ", soketti.getRemoteSocketAddress().toString());

        } catch (IOException e) {
            System.out.println(e);
            fileout.clientEventLog("List users FAIL", soketti.getRemoteSocketAddress().toString());

        }

        disconnect();
    }

// Lämpötila-arvojen listaaminen tietokannasta	    -- FROM temps LIMIT 50 pitäis lisätä?
    public void listTemps(Socket soketti) { // OK      // NO NEED FOR SQL INJECTION PROTECTION
        FileOut fileout = new FileOut();

        this.connect();
        try {
            PrintStream os = new PrintStream(soketti.getOutputStream());
            String query = "SELECT paivays, value, sensor FROM temps";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {

                os.println(rs.getString(1) + "\t" + rs.getDouble(2) + "\t" + rs.getInt(3));
            }
            fileout.clientEventLog("List temps", soketti.getRemoteSocketAddress().toString());

        } catch (SQLException ex) {		// handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            fileout.clientEventLog("List temps FAIL", soketti.getRemoteSocketAddress().toString());

        } catch (IOException e) {
            System.out.println(e);
            fileout.clientEventLog("List temps FAIL", soketti.getRemoteSocketAddress().toString());
        }

        disconnect();
    }

    public String GetUsersFromDB(String IP) {                // NO NEED FOR SQL INJECTION PROTECTION
           FileOut fileout = new FileOut();

        this.connect();
        String palautus = "USERS LIST" + "\r\n\r\n" + "nro" + "\t" + "Username" + "\t" + "Userlevel" + "\r\n"
                + "---------------------------------------------------------" + "\r\n";
        int i = 1;

        try {
            String query = "SELECT username, userlvl FROM users ORDER BY username ASC";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {

                palautus += Integer.toString(i) + "\t" + rs.getString(1) + "\t\t" + rs.getString(2) + "\r\n";
                i++;
            }
                           fileout.clientEventLog("Get Users from DB", IP);

        } catch (SQLException ex) {		// handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
                           fileout.clientEventLog("Get Users from DB FAIL", IP);
        }
        disconnect();
        return palautus;

    }
}
