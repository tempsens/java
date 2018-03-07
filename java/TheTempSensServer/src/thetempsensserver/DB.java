//  Versio 0.4  05.03.2018  Jukka
//   listUsers from/to date ja sensor # lisätty
//   MySQL hakujen tulokset rajoitettu 100kpl
//------------------------------------------------------------------------------
//  Versio 0.3  05.03.2018  Jukka
//   Virheenkäsittely metodeihin jos ei yhteyttä tietokantaan
//------------------------------------------------------------------------------
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
import static thetempsensserver.TheTempSensServer.IP;

/**
 * @author PetShopBoys
 */
public class DB {

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    PreparedStatement preparedStatement = null;

// YHTEYDEN AVAUS
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

// YHTEYDEN KATKAISU
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
        if (conn != null) {		      // LISÄTTY - katotaan toimiiko... -Jukka-
            try {
                conn.close();
            } catch (SQLException sqlEx) {
            }		// handle any errors - EMPTY
            conn = null;
        }
    }

// Käyttäjän lisääminen
    public int insertUser(int val1, String val2, String IP) {            //SQL INJECTION SAFE
        FileOut fileout = new FileOut();
        this.connect();
        if (conn == null) {
            return -1;
        } else {
            try {
                preparedStatement = conn.prepareStatement("INSERT INTO users (userlvl, username, password) VALUES(?, ?, ?)");
                preparedStatement.setInt(1, val1);
                preparedStatement.setString(2, val2);
                preparedStatement.setString(3, val2);

                if (preparedStatement.execute()) {
                    rs = preparedStatement.getResultSet();
                }
                System.out.println("User added to database.");

                fileout.clientEventLog("New User added ( usr: " + val2 + " / lvl: " + val1 + ")", IP);
                this.disconnect();
                return 1;

            } catch (SQLException ex) {		// handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
                fileout.clientEventLog("New User Add FAIL ( usr: " + val1 + " / lvl: " + val2, IP);
                this.disconnect();
                return -1;
            }
        }
    }

// Lämpötilan lisääminen
    public int insertTemp(double val1, int val2, String dagen, String IP) {            //SQL INJECTION SAFE  IF NEEDED
        this.connect();

        if (conn == null) {
            return -1;
        } else {
            try {

                preparedStatement = conn.prepareStatement("INSERT INTO temps (value, sensor) VALUES (?, ?)");
                preparedStatement.setDouble(1, val1);
                preparedStatement.setInt(2, val2);

                if (preparedStatement.execute()) {
                    rs = preparedStatement.getResultSet();
                }
                System.out.println(dagen + " Temperature " + val1 + " \tadded to database from sensor " + val2);
                this.disconnect();
                return 1;

            } catch (SQLException ex) {		// handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
                this.disconnect();
                return -2;
            }
        }
    }

// Salasanan vaihtaminen
    public int changepass(int userID, String pass) {
        int value;
        this.connect();
        try {
            if (conn == null) {
                return -1;
            } else {
                // System.out.println("CONN = " + conn); // FOR DEBUG
                preparedStatement = conn.prepareStatement("UPDATE users SET password=? WHERE id=?");
                preparedStatement.setString(1, pass);
                preparedStatement.setInt(2, userID);

                System.out.println(preparedStatement.executeUpdate());
                value = 1;
                // System.out.println("changePass: value 1");	// FOR DEBUG
            }
        } catch (SQLException ex) {		// handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            // System.out.println("changePass: value -1");  // FOR DEBUG
            value = -1;
        }
        this.disconnect();
        return value;
    }

// Käyttäjätietojen tarkastaminen ("login")
    public String checkUser(String user, String pass, String IP) {            //SQL INJECTION SAFE
        String leveli = "0";
        int id = 0;
        try {
            if (conn == null) {
                return "-1";
            } else {
                // System.out.println("CONN = " + conn); // FOR DEBUG
                preparedStatement = conn.prepareStatement("SELECT id, userlvl FROM users WHERE username=? AND password=?");
                preparedStatement.setString(1, user);
                preparedStatement.setString(2, pass);

                ResultSet resSet = preparedStatement.executeQuery();
                FileOut fileout = new FileOut();
                if (resSet.next()) {
                    System.out.println("Login from: " + IP // FOR DEBUG -Jukka-
                            + " as '" + user + "' successfull.");               // FOR DEBUG -Jukka-
                    fileout.loginSuccess("u:" + user, IP);
                    id = resSet.getInt(1);
                    leveli = resSet.getString(2);
//                    System.out.println("CheckUser (id): " + id);                // FOR DEBUG -Jukka-
//                    System.out.println("CheckUser (leveli): " + leveli);        // FOR DEBUG -Jukka-
                } else {
//                    System.out.println("Login from: " + IP                      // FOR DEBUG -Jukka-
//                            + " as '" + user + "' FAIL!");                      // FOR DEBUG -Jukka-
                    fileout.loginError("u:" + user, IP);
                }
                disconnect();
                System.out.println("Check user (palautus) :" + leveli);
                return leveli + "|" + Integer.toString(id);         // palautetaan userlevel ja userID
            }
        } catch (SQLException ex) {                                 // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            disconnect();
            return "-1";                                            // Palauttaa -1 virheessä -Suni-
        }
    }

// Käyttäjien listaaminen tietokannasta
    public int listUsers(Socket soketti) { // NO NEED FOR SQL INJECTION PROTECTION
        this.connect();
        int palaute;
        FileOut fileout = new FileOut();

        if (conn == null) {
            return -1;
        } else {
            try {
                PrintStream os = new PrintStream(soketti.getOutputStream());
                String query = "SELECT username FROM users LIMIT 100";
                stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(query);
                while (res.next()) {
                    os.println(res.getString("username"));
                }
                fileout.clientEventLog("List users", IP);
                palaute = 1;

            } catch (SQLException ex) {		// handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
                fileout.clientEventLog("List users FAIL ", soketti.getRemoteSocketAddress().toString());
                palaute = -2;

            } catch (IOException e) {
                System.out.println(e);
                fileout.clientEventLog("List users FAIL", soketti.getRemoteSocketAddress().toString());
                palaute = -2;
            }
        }
        disconnect();
        // System.out.println("DB/listUsers palaute: " + palaute); // FOR DEBUG
        return palaute;
    }

// Lämpötila-arvojen listaaminen tietokannasta
    public int listTemps(Socket soketti, int sensNr, String fromDate, String toDate) { // OK      // NO NEED FOR SQL INJECTION PROTECTION
        FileOut fileout = new FileOut();
        int palaute;

        String sensNrStr = "";
        if (sensNr > 0) {
            sensNrStr = " AND sensor = " + sensNr;
        }

        String fromDateStr = "";
        String toDateStr = "";
        /*  
                              if (fromDate.length() > 0 && toDate.length() > 0) {
                                  fromDateStr = " AND (DATE(paivays) BETWEEN '" + fromDate + "' AND '" + toDate + "')";
                              } else {

                                  if (fromDate.length() > 0) {
                                      fromDateStr = " AND 'paivays' >= '" + fromDate + "'";
                                  }
                                  if (toDate.length() > 0) {
                                      toDateStr = " AND 'paivays' <= '" + toDate + "'";
                                  }   
                              }
         */
        if (fromDate.length() < 1) {
            fromDate = "2000-01-01";
        }
        if (toDate.length() < 1) {
            toDate = "CURDATE()";
        }
        if (!toDate.equals("CURDATE()")) {
            toDate = "'" + toDate + "'";
        }
        fromDateStr = " AND (DATE(paivays) BETWEEN '" + fromDate + "' AND " + toDate + ")";

        this.connect();
        if (conn == null) {
            return -1;
        } else {
            try {
                PrintStream os = new PrintStream(soketti.getOutputStream());
                String query = "SELECT paivays, value, sensor FROM temps WHERE 1"
                        + sensNrStr + fromDateStr + toDateStr
                        + " LIMIT 100";
                System.out.println("DEBUG: listTemps query: " + query);	    // FOR DEBUG
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {

                    os.println(rs.getString(1) + "\t" + rs.getDouble(2) + "\t" + rs.getInt(3));
                }
                palaute = 1;
                fileout.clientEventLog("List temps", soketti.getRemoteSocketAddress().toString());

            } catch (SQLException ex) {		// handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
                fileout.clientEventLog("List temps FAIL", soketti.getRemoteSocketAddress().toString());
                palaute = -2;

            } catch (IOException e) {
                System.out.println(e);
                fileout.clientEventLog("List temps FAIL", soketti.getRemoteSocketAddress().toString());
                palaute = -2;
            }
        }
        disconnect();
        return palaute;
    }
// Listataan käyttäjät tietokannasta

    public String GetUsersFromDB(String IP) {                // NO NEED FOR SQL INJECTION PROTECTION
        FileOut fileout = new FileOut();
        String palaute = "";

        this.connect();
        String palautus = "USERS LIST" + "\r\n\r\n" + "nro" + "\t" + "Username" + "\t" + "Userlevel" + "\r\n"
                + "---------------------------------------------------------" + "\r\n";
        int i = 1;

        try {
            if (conn == null) {
                return "-1";
            } else {
                String query = "SELECT username, userlvl FROM users ORDER BY username ASC";
                stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(query);
                while (res.next()) {

                    palaute += Integer.toString(i) + "\t" + res.getString(1) + "\t\t" + res.getString(2) + "\r\n";
                    i++;
                }
                fileout.clientEventLog("Get Users from DB", IP);
            }
        } catch (SQLException ex) {		// handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            fileout.clientEventLog("Get Users from DB FAIL", IP);
            palaute = "-2";
        }
        disconnect();
        return palaute;

    }
}
