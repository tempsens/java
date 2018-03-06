//  Versio 0.5  06.03.2018  Jukka
//   Kommentointeja lisätty ja koodia tiivistetty
//   Debug -tekstejä kommentoitu pois käytöstä
//   Liian alhaisella userlevelillä yrityksistä "peruspalaute" ja kirjoitus logiin
//------------------------------------------------------------------------------
//  Versio 0.4  05.03.2018  Jukka
//   listUsers from/to date ja sensor # lisätty
//   loginiin mysql.disconnect (Too many connections -virhe)
//------------------------------------------------------------------------------
//  Versio 0.3  05.03.2018  Jukka
//   päätoimintojen järjestäminen
//   Virheenkäsittely kaikkiin tietokantaa käyttäviin toimintoihin
//   Globaali asiakkaan IP -muuttuja lisätty
//------------------------------------------------------------------------------
//  Versio 0.2	28.02.2018  Jukka
//   Static VERSION -muuttuja ja versionumeron tulostus ohjelman alkuun
//   AdminBackupLogin, jos DB yhteys ei toimi
//   server QUIT -käskyn ohjaus: server quit, client exit
//   catch kuvaustekstejä debuggia helpottamaan
//------------------------------------------------------------------------------
//  Versio 0.1  26.2.2018   Joakim 
//              Siirrettiin login ulos isosta silmukasta, toimii
//------------------------------------------------------------------------------
package thetempsensserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import static thetempsensserver.TheTempSensServer.IP;

/**
 * @author PetShopBoys
 */
public class MainMenu implements Runnable {

    private static final String VERSION = "0.2";
    private static final String PERUSPALAUTE = "Kirjoita 'help' jos et muuta osaa!";
    private static final int PORT = 1234; // Portin määritys
    ServerSocket servu = null;            // Palvelin soketin alustus
    Socket clinu = null;                  // Asiakas soketin alustus
    PrintStream os = null;                // Output streamin alustus
    DataInputStream is = null;            // Input streamin alustus

    MainMenu(Socket clinu) {              // Konstruktori, jossa määritetään I/O -oliot
	try {
	    this.clinu = clinu;					    // Soketti
	    this.is = new DataInputStream(clinu.getInputStream());  // Input
	    this.os = new PrintStream(clinu.getOutputStream());	    // Output
	    IP = clinu.getRemoteSocketAddress().toString();	    // Clientin IP
	    System.out.println("Connection from: " + IP);	// Ilmoitus yhteydestä
	} catch (IOException e) {
	    System.out.println("Socket/is/os creation failed: " + e);
	}
    }

    @Override
    public void run() {
	int userLevel = 0; // Käyttäjätaso. Haetaan oikea arvo tietokannasta kirjautumisessa
	int userID = 0;    // Käyttäjän ID tietokannassa. Tarvitaan salasanan vaihtoon
	// Muuttujat ja luokat pääohjelmalle
	String komento;
	String today = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ").format(new Date());
	Inputti inputti = new Inputti();          // Tarvitaan help -tekstin tulostukseen
	FileOut fileout = new FileOut();          // Tarvitaan tiedosto outputiin
	serverControl srvC = new serverControl(); // Start/Stop/Restart
	sensorControl senC = new sensorControl();
	int ulosta = 0;                           // Muuttuja while -loopista poistumiseksi

	while (userLevel < 1) {	// Login loop
	    try {
		komento = is.readLine();                 // Luetaan ensimmäinen rivi clientiltä
		if (komento.equals("login")) {           // Vaaditaan ekana käskynä login
		    try {
			String userName = is.readLine(); // Luetaan clientiltä username
			String userPass = is.readLine(); // Luetaan clientiltä password

			DB user = new DB();
			user.connect();
			String vastaus = user.checkUser(userName, userPass, IP);

			if (vastaus.equals("-1")) {      // Tietokantavirheen sattuessa "failsafeLogin"
			    os.println("Server database error! Only failsafe login possible.");

			    while (userLevel < 10) {
				komento = is.readLine();          // Luetaan ensimmäinen rivi clientiltä
				if (komento.equals("login")) {    // Vaaditaan edelleen login -käsky ensin
				    try {
					userName = is.readLine(); // Luetaan clientiltä username
					userPass = is.readLine(); // Luetaan clientiltä password
					if (userName.equals("admin") && userPass.equals("0000")) {
					    userLevel = 10;       // Määritetään 10 taso, koska admin
					    os.println("1|-1");   // Kerrotaan clientille, että onnistui ja userID -1
					} else {
					    os.print("Server database error! Only failsafe login possible.");
					    fileout.clientEventLog("Server database error! Only failsafe login possible.", IP);
					}
				    } catch (IOException e) {
					System.out.println("FailsafeAdminLoginLoop readLine fail: " + e);
					fileout.clientEventLog("FailsafeAdminLoginLoop readLine fail", IP);
				    }
				}
			    }
			} else if (vastaus.contains("0|")) {
			    os.println("0");
			} else {
			    String[] vastaukset = vastaus.split("\\|");  // Hajotetaan | -merkillä erottaen
			    userLevel = Integer.parseInt(vastaukset[0]); // Luetaan käyttäjätaso muuttujaan
			    userID = Integer.parseInt(vastaukset[1]);    // Luetaan ID muuttujaan
			    os.println("1|" + userID);			 // Lähetetään tiedot clientille
			}
			user.disconnect();
		    } catch (IOException e) {
			System.out.println("LoginLoop db-login fail: " + e);
		    }
		}
	    } catch (IOException e) {
		System.out.println("LoginLoop readLine fail: " + e);
		fileout.clientEventLog("LoginLoop readLine fail", IP);
		is = null;  // Nollataan input streamin olio, jottei jää readLine looppi päälle
	    }
	}
	// Pääohjelma loop komentojen kuunteluun
	while (ulosta < 1 && userLevel > 0) { // LISÄTTY UserLevel vaatimus Joakim
	    try {
		komento = is.readLine();      // Luetaan yksi rivi muuttujaan
		System.out.println(IP + ": " + komento); // DEBUG: Tulostaa serverin konsoliin
	    } catch (IOException e) {
		System.out.println("MainLoop readLine fail: " + e);
		break;		    // Poistutaan loopista jos luku epäonnistuu
	    }
	    if (komento == null) {
		break;
	    }
	    switch (komento.toLowerCase()) {
// Tulostaa helpin
		case "help":
		    // System.out.println("Helpin tulostus...");    // FOR DEBUG
		    fileout.clientEventLog(komento, IP);
		    inputti.Help(clinu, userLevel); // Tulostaa helpin
		    break;
// Salasanan vaihtaminen kirjautuneelle käyttäjälle tietokantaan
		case "change pass":
		    try {
			String strUserID = is.readLine();
			String strNewPass = is.readLine();
			DB cp = new DB();

			if (cp.changepass(Integer.parseInt(strUserID), strNewPass) == 1) {
			    os.println("Salasana vaihdettu!");
			} else {
			    os.println("Server database error! Command not available.");
			}
		    } catch (IOException e) {
			System.out.println(e);
			os.println("Error with change password readLine");
		    }
		    break;

		case "add": // Väärä syntaksi -> antaa vain virheilmoituksen
		    fileout.clientEventLog(komento, IP);
		    os.println("Missing parameter! (user or temp needed)");
		    break;
// Käyttäjän lisääminen tietokantaan
		case "add user": // Lisätään käyttäjä
		    if (userLevel == 10) {
			try { // Luetaan kaksi riviä lisää
			    int newUserLevel = Integer.parseInt(is.readLine());
			    String newUserInput = is.readLine();

			    DB uusiUseri = new DB();
			    int palaute = uusiUseri.insertUser(newUserLevel, newUserInput, IP);
			    if (palaute == -1) {
				os.println("Server database error! Command not available.");
			    } else if (palaute == -2) {
				os.println("Server error!");
			    }
			} catch (IOException e) {
			    System.out.println("readLine error in add user: " + e);
			}
		    } else {
			System.out.println(PERUSPALAUTE);
			fileout.clientEventLog("Add User (UserLevel too low (" + userLevel + ")", IP);
		    }
		    break;
// Lämpötilan lisääminen tietokantaan
		case "add temp": // Lämpötilan lisääminen
		    if (userLevel >= 5) {
			try {
			    double tempvalue = Double.parseDouble(is.readLine());
			    int sensori = Integer.parseInt(is.readLine());
			    if (senC.getSensor(sensori) > 0) {
				os.println("Sensor " + sensori + " already in use!");
			    } else {

				DB uusiTemp = new DB();
				System.out.println(tempvalue);	// FOR DEBUG
				System.out.println(sensori);	// FOR DEBUG
				int palaute = uusiTemp.insertTemp(tempvalue, sensori, today, IP);
				System.out.println("add temp (insertTemp palaute: " + palaute);
				if (palaute == -1) {
				    os.println("Server database error! Command not available.");
				} else if (palaute == -2) {
				    os.println("Server error!");
				} else {
				    senC.setSensor(sensori);
				}
			    }
			} catch (IOException e) {
			    System.out.println("readLine error in add temp: " + e);
			}
		    } else {
			System.out.println(PERUSPALAUTE);
			fileout.clientEventLog("Add User (UserLevel too low (" + userLevel + ")", IP);
		    }
		    break;
		// Väärä syntaksi -> antaa vain virheilmoituksen
		case "list":
		    fileout.clientEventLog(komento, IP);
		    os.println("Missing parameter! (users or temps needed)");
		    break;
// Listaa käyttäjät tietokannasta
		case "list users": // Tulostaa listan käyttäjistä
		    if (userLevel >= 5) {
			DB listuser = new DB();
			int palaute = listuser.listUsers(clinu);
			if (palaute == -1) {
			    os.println("Server database error! Command not available.");
			} else if (palaute == -2) {
			    os.println("Server error!");
			}
		    } else {
			os.println(PERUSPALAUTE);
			fileout.clientEventLog("Add User (UserLevel too low (" + userLevel + ")", IP);
		    }
		    break;
// Listaa lämpötilat tietokannasta
		case "list temps": // Tulostaa listan lämpötiloista (100 viimeisintä)
		    if (userLevel >= 5) {
			DB listtemps = new DB();
			int palaute = listtemps.listTemps(clinu, 0, "2018-03-01", "2018-03-04");
			if (palaute == -1) {
			    os.println("Server database error! Command not available.");
			} else if (palaute == -2) {
			    os.println("Server error!");
			}
		    } else {
			os.println(PERUSPALAUTE);
			fileout.clientEventLog("Add User (UserLevel too low (" + userLevel + ")", IP);
		    }
		    break;
		// Väärä syntaksi -> antaa vain virheilmoituksen
		case "fileout":
		    if (userLevel >= 5) {
			fileout.clientEventLog(komento, IP);
			os.println("Missing parameter! (console or userlist needed)");
		    } else {
			os.println(PERUSPALAUTE);
		    }
		    break;
// Kirjoita tiedostoon käyttäjistälista tietokannasta
		case "fileout users": // Tulostetaan lista käyttäjistä
		    if (userLevel >= 5) { // ..mutta vain jos on tarpeeksi korkea taso
			int palaute = fileout.userlist(clinu);
			if (palaute < 0) {
			    os.println("Server database error! Command not available.");
			}
		    } else {
			os.println(PERUSPALAUTE);
			fileout.clientEventLog("Add User (UserLevel too low (" + userLevel + ")", IP);
		    }
		    break;
// Sammuttaa palvelinohjelman
		case "quit": // OK!!
		    fileout.clientEventLog(komento, IP);
		    os.println("Server quit."); // Infopläjäys
		    os.println("QQ");           // Lähetetään QQ lähetyksen lopuksi
		    ulosta = 1;                 // Poistuu while -loopista
		    System.exit(-1);
		    break;
// Käynnistää palvelimen lämpötilojen välittämisen tietokantaan
		case "start":
		    fileout.clientEventLog(komento, IP);
		    srvC.start(clinu);
		    break;
// Pysäyttää palvelimen lämpötilojen välittämisen tietokantaan
		case "stop":
		    fileout.clientEventLog(komento, IP);
		    srvC.stop(clinu);
		    break;
// Käynnistää uudelleen palvelimen lämpötilojen välittämisen tietokantaan		    
		case "restart":
		    fileout.clientEventLog(komento, IP);
		    srvC.restart(clinu);
		    break;
// Tulostaa palvelimen lämpötilojen välittämisen tilan (0 tai 1)
		case "status":
		    fileout.clientEventLog(komento, IP);
		    os.println(srvC.getStatus());
		    break;
// Toiminto tuntemattomille komennoille
		default:
		    //   fileout.clientEventLog(komento, IP);
		    os.println(PERUSPALAUTE);
		    fileout.clientEventLog("Unknown command(" + komento + ")", IP);
	    }   // SWITCHin loppusulje
	    os.println("\nQQ");	// Tämä on loppumerkki clientin "multiline loopille"
	}   // Ohjelmaloopin loppusulje (while)
	System.out.println("Client disconnected.");
    }	// Metodin loppusulje (void run)
} // Luokan loppusulje (MainMenu)
