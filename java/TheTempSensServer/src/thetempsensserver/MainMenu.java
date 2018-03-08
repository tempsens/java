//  Versio 0.7  08.03.2018  Jukka
//   failsafeLogin username ja password omiin muuttujiinsa
//   Koodin siistimistä ja kommentointeja lisää
//   Lokitiedostoon kirjoituksia lisää
//   Asiakkailta jäi pelkkä viimeisin IP -osoite muistiin. Korjattu (Private String)
//------------------------------------------------------------------------------
//  Versio 0.6  07.03.2018  Tiimi
//   UserLevel passaus clienteille
//   List temps -haun täsmennykset (fromDate, toDate)
//------------------------------------------------------------------------------
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
import java.util.Set;
import static thetempsensserver.TheTempSensServer.serverRunning;

/**
 * @author PetShopBoys
 */
public class MainMenu implements Runnable {

    private static final String PERUSPALAUTE = "Kirjoita 'help' jos et muuta osaa!";
    private String IP = null;                    // Muuttuja asiakkaan osoitteelle
    ServerSocket servu = null;                   // Palvelin soketin alustus
    Socket clinu = null;                         // Asiakas soketin alustus
    DataInputStream is = null;                   // Input streamin alustus
    PrintStream os = null;                       // Output streamin alustus
    private final String FSUSER = "admin";       // FailsafeLogin käyttäjätunnus
    private final String FSPASS = "0000";        // FailsafeLogin käyttäjätunnus

    MainMenu(Socket clinu) {              // Konstruktori, jossa määritetään I/O -oliot
	try {
	    this.clinu = clinu;					    // Soketti
	    this.is = new DataInputStream(clinu.getInputStream());  // Input
	    this.os = new PrintStream(clinu.getOutputStream());	    // Output
	    IP = clinu.getRemoteSocketAddress().toString();	    // Clientin IP
	    System.out.println("Connection from: " + IP);	    // Ilmoitus yhteydestä
	} catch (IOException e) {
	    System.out.println("Socket/is/os creation failed: " + e);
	}
    }

    @Override
    public void run() { // Tämä suoritetaan kun puljataan säikeiden kanssa

// Muuttujat ja luokat pääohjelmalle
	int userLevel = 0; // Käyttäjätaso. Haetaan oikea arvo tietokannasta kirjautumisessa
	int userID;        // Käyttäjän ID tietokannassa. Tarvitaan salasanan vaihtoon
	String komento;    // Käyttäjän (clientin) syöttämä komento tallentuu tähän muuttujaan
	String today = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ").format(new Date());
	Inputti inputti = new Inputti();          // Tarvitaan help -tekstin tulostukseen
	FileOut fileout = new FileOut();          // Tarvitaan tiedosto outputiin
	serverControl srvC = new serverControl(); // Start/Stop/Restart lämpöarvojen välitys
	sensorControl senC = new sensorControl(); // Sensorien syötteen valvontataulun ohjaus
	int ulosta = 0;                           // Muuttuja Pääohjelma -loopista poistumiseksi
// Login looppi
	while (userLevel < 1) {
	    try {
		komento = is.readLine();                 // Luetaan ensimmäinen rivi clientiltä
		if (komento.equals("login")) {           // Vaaditaan ekana käskynä login
		    try {
			String userName = is.readLine(); // Luetaan clientiltä username
			String userPass = is.readLine(); // Luetaan clientiltä password

			DB user = new DB();
			user.connect();                  // Avataan tietokantayhteys
			String vastaus = user.checkUser(userName, userPass, IP);

			if (vastaus.equals("-1")) {      // Tietokantavirheen sattuessa "failsafeLogin"
			    os.println("Server database error! Only failsafe login possible.");
			    fileout.clientEventLog("DB ERROR! Falling to failsafe login...", IP);

			    while (userLevel < 10) {     // failsafeLogin looppi
				komento = is.readLine();          // Luetaan ensimmäinen rivi clientiltä
				if (komento.equals("login")) {    // Vaaditaan edelleen login -käsky ensin
				    try {
					userName = is.readLine(); // Luetaan clientiltä username
					userPass = is.readLine(); // Luetaan clientiltä password
					if (userName.equals(FSUSER) && userPass.equals(FSPASS)) {
					    userLevel = 10;       // Määritetään 10 taso, koska admin
					    os.println("1|-1");   // Kerrotaan clientille, että onnistui ja userID -1
					} else {
					    os.print("Server database error! Only failsafe login possible.");
					    // Kirjoitetaan virhe lokiin
					    fileout.clientEventLog("Server database error! Only failsafe login possible.", IP);
					}
				    } catch (IOException e) {
					System.out.println("FailsafeAdminLoginLoop readLine fail: " + e);
					// Kirjoitetaan virhe lokiin
					fileout.clientEventLog("FailsafeAdminLoginLoop readLine fail", IP);
				    }
				}
			    }
			} else if (vastaus.contains("0|") && !vastaus.contains("10|")) { // Jos vastaus 0, muttei 10
			    os.println("0"); // Lähetetään clientille 0 (-> uusi login)
			    fileout.clientEventLog("ERROR: Login fail.", IP); // Virhe lokiin
			} else {
			    String[] vastaukset = vastaus.split("\\|");  // Hajotetaan | -merkillä erottaen
			    userLevel = Integer.parseInt(vastaukset[0]); // Luetaan käyttäjätaso muuttujaan
			    userID = Integer.parseInt(vastaukset[1]);    // Luetaan ID muuttujaan
			    os.println(" 1|" + userID + "|" + userLevel); // Lähetetään tiedot clientille
			    // Kirjoitetaan onnistuneesta kirjautumisesta tieto lokiin
			    fileout.clientEventLog("User login: " + userID + " with level " + userLevel, IP);
			}
			user.disconnect(); // Suljetaan tietokantayhteys
		    } catch (IOException e) {
			System.out.println("LoginLoop db-login fail: " + e);
			fileout.clientEventLog("ERROR: LoginLoop db-login fail", IP); // Virhe lokiin
		    }
		}
	    } catch (IOException e) {
		System.out.println("LoginLoop readLine fail: " + e);
		fileout.clientEventLog("LoginLoop readLine fail", IP);
		is = null;  // Nollataan input streamin olio, jottei jää readLine looppi päälle
	    }
	}
// Pääohjelma loop komentojen kuunteluun
	while (ulosta < 1 && userLevel > 0) {            // LISÄTTY UserLevel vaatimus Joakim
	    try {
		komento = is.readLine();                 // Luetaan yksi rivi muuttujaan
		System.out.println(IP + ": " + komento); // DEBUG: Tulostaa serverin konsoliin
	    } catch (IOException e) {
		System.out.println("MainLoop readLine fail: " + e);
		break;		    // Poistutaan loopista jos luku epäonnistuu
	    }
	    if (komento == null) {  // Jos komento on tyhjä, niin poistu loopista
		break;
	    }
	    switch (komento.toLowerCase()) {
// Tulostaa helpin
		case "help":
		    fileout.clientEventLog(komento, IP); // Tulostaa komennon lokitiedostoon
		    inputti.Help(clinu, userLevel);      // Tulostaa helpin
		    break;
// Salasanan vaihtaminen kirjautuneelle käyttäjälle tietokantaan
		case "change pass":
		    try {
			String strUserID = is.readLine();
			String strNewPass = is.readLine();
			DB cp = new DB();

			if (cp.changepass(Integer.parseInt(strUserID), strNewPass) == 1) {
			    os.println("Salasana vaihdettu!");
			    fileout.clientEventLog(komento + " for " + strUserID, IP); // Tieto lokiin
			} else {
			    os.println("Server database error! Command not available.");
			    fileout.clientEventLog(komento + " -> DB ERROR!", IP);     // Virhe lokiin
			}
		    } catch (IOException e) {
			System.out.println(e);
			os.println("Error with change password readLine");
			fileout.clientEventLog(komento + " -> readLine ERROR!", IP);   // Virhe lokiin
		    }
		    break;
// Väärä syntaksi -> antaa vain virheilmoituksen
		case "add":
		    fileout.clientEventLog(komento, IP); // Tulostaa komennon lokitiedostoon
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
			    if (palaute == -1) {        // Tulostetaan tietokantavirheilmoitus
				os.println("Server database error! Command not available.");
				fileout.clientEventLog(komento + " -> DB ERROR!", IP);     // Virhe lokiin
			    } else if (palaute == -2) { // Tulostetaan virheilmoitus
				os.println("Server error!");
				fileout.clientEventLog(komento + " -> SERVER ERROR!", IP); // Virhe lokiin
			    }
			    // Tulostetaan käyttäjän lisäys myös tiedostoon
			    fileout.clientEventLog(komento + " " + newUserInput + " with level " + newUserInput, IP);
			} catch (IOException e) {
			    System.out.println("readLine error in add user: " + e);
			    fileout.clientEventLog(komento + " -> readLine ERROR!", IP);   // Virhe lokiin
			}
		    } else {
			System.out.println(PERUSPALAUTE);
			// Tulostaa komennon lokitiedostoon
			fileout.clientEventLog("Add User (UserLevel too low (" + userLevel + ")", IP);
		    }
		    break;
// Lämpötilan lisääminen tietokantaan
		case "add temp": // Lämpötilan lisääminen
		    if (userLevel >= 5) {
			try {
			    if (serverRunning == 0) {
				os.println("Please, Write 'START' first"); // TÄÄ EI TOIMINU VIELÄ -Jukka-
				is.readLine(); // Tyhjennetään lukupuskuri
				is.readLine(); // Tyhjennetään lukupuskuri
				System.out.println("Server not started. Temperature not sent to database.");
			    } else {
				double tempvalue = Double.parseDouble(is.readLine());
				int sensori = Integer.parseInt(is.readLine());
				if (senC.getSensor(sensori) > 0) { // Tarkastetaan onko sensori vapaa
				    os.println("Sensor " + sensori + " already in use!");        // Tulostetaan clientille
				    System.out.println("Sensor " + sensori + " already in use!");// Tulostetaan konsoliin
				} else {
				    DB uusiTemp = new DB();
				    //System.out.println(tempvalue);	// FOR DEBUG
				    //System.out.println(sensori);	// FOR DEBUG
				    int palaute = uusiTemp.insertTemp(tempvalue, sensori, today, IP);
				    switch (palaute) {
					case -1: // Tulostetaan tietokantavirheilmoitus
					    os.println("Server database error! Command not available.");
					    // Tulostaa virheen lokitiedostoon
					    fileout.clientEventLog(komento + " -> DB ERROR!", IP);
					    break;
					case -2: // Tulostetaan virheilmoitus
					    os.println("Server error!");
					    // Tulostaa virheen lokitiedostoon
					    fileout.clientEventLog(komento + " -> SERVER ERROR!", IP);
					    break;
					default:
					    os.println("\n");
					    senC.setSensor(sensori); // Asetetaan sensori käyttöön
					    // Käskyä ei tulosteta tiedostoon "turhan" levyn rasittamisen vuoksi!
					    break;
				    }
				}
			    }
			} catch (IOException e) {
			    System.out.println("readLine error in add temp: " + e);
			}
		    } else {
			System.out.println(PERUSPALAUTE);
			// Tulostaa komennon lokitiedostoon virheilmoituksineen
			fileout.clientEventLog("Add User (UserLevel too low (" + userLevel + ")", IP);
		    }
		    break;
// Väärä syntaksi -> antaa vain virheilmoituksen
		case "list":
		    fileout.clientEventLog(komento, IP); // Tulostaa komennon lokitiedostoon
		    os.println("Missing parameter! (users or temps needed)");
		    break;
// Listaa käyttäjät tietokannasta
		case "list users":        // Tulostaa listan käyttäjistä
		    if (userLevel >= 5) { // ..mutta vain jos on tarpeeksi korkea taso
			DB listuser = new DB();
			int palaute = listuser.listUsers(clinu, IP);
			if (palaute == -1) {        // Tulostetaan tietokantavirheilmoitus
			    os.println("Server database error! Command not available.");
			} else if (palaute == -2) { // Tulostetaan virheilmoitus
			    os.println("Server error!");
			}
		    } else {
			os.println(PERUSPALAUTE);
			// Tulostaa komennon lokitiedostoon virheilmoituksineen
			fileout.clientEventLog("Add User (UserLevel too low (" + userLevel + ")", IP);
		    }
		    break;
// Listaa lämpötilat tietokannasta
		case "list temps": // Tulostaa listan lämpötiloista (100 viimeisintä)
		    if (userLevel >= 5) {
			try {
			    DB listtemps = new DB();
			    String startDate = is.readLine();
			    String endDate = is.readLine();

			    int palaute = listtemps.listTemps(clinu, 0, startDate, endDate);
			    if (palaute == -1) { // Tulostetaan tietokantavirheilmoitus
				os.println("Server database error! Command not available.");
			    } else if (palaute == -2) {
				os.println("Server error!");
			    }
			} catch (IOException e) {
			}
		    } else {
			os.println(PERUSPALAUTE);
			// Tulostaa komennon lokitiedostoon
			fileout.clientEventLog("Add User (UserLevel too low (" + userLevel + ")", IP);
		    }
		    break;
// Väärä syntaksi -> antaa vain virheilmoituksen
		case "fileout":           // Jos unohdetaan täsmennys, niin tulostetaan virheilmoitus
		    if (userLevel >= 5) { // ..mutta vain jos on tarpeeksi korkea taso
			fileout.clientEventLog(komento, IP); // Tulostaa komennon lokitiedostoon
			os.println("Missing parameter! (console or userlist needed)");
		    } else {              // Muutoin tulostetaan väärän komennon ilmoitus
			os.println(PERUSPALAUTE);
		    }
		    break;
// Kirjoita tiedostoon käyttäjistälista tietokannasta
		case "fileout users":     // Tulostetaan lista käyttäjistä
		    if (userLevel >= 5) { // ..mutta vain jos on tarpeeksi korkea taso
			int palaute = fileout.userlist(clinu, IP);
			if (palaute < 0) { // Tulostetaan tietokantavirheilmoitus
			    os.println("Server database error! Command not available.");
			}
		    } else {              // Muutoin tulostetaan väärän komennon ilmoitus
			os.println(PERUSPALAUTE);
			// Tulostaa komennon lokitiedostoon
			fileout.clientEventLog("Add User (UserLevel too low (" + userLevel + ")", IP);
		    }
		    break;
// Sammuttaa palvelinohjelman
		case "quit": // OK!!
		    fileout.clientEventLog(komento, IP); // Tulostaa komennon lokitiedostoon
		    os.println("Server quit.");          // Infopläjäys
		    os.println("QQ");                    // Lähetetään QQ lähetyksen lopuksi
		    System.out.println("Server shutting down...");
		    System.exit(0);		         // Sammuttaa ohjelman
		    break;
// Käynnistää palvelimen lämpötilojen välittämisen tietokantaan
		case "start":
		    fileout.clientEventLog(komento, IP); // Tulostaa komennon lokitiedostoon
		    srvC.start(clinu);                   // Lämpöarvojen välityksen käynnistys
		    break;
// Pysäyttää palvelimen lämpötilojen välittämisen tietokantaan
		case "stop":
		    fileout.clientEventLog(komento, IP); // Tulostaa komennon lokitiedostoon
		    srvC.stop(clinu);                    // Lämpöarvojen välityksen pysäytys
		    break;
// Käynnistää uudelleen palvelimen lämpötilojen välittämisen tietokantaan		    
		case "restart":
		    fileout.clientEventLog(komento, IP); // Tulostaa komennon lokitiedostoon
		    srvC.restart(clinu);                 // Lämpöarvojen välityksen uudelleenkäynnistys
		    break;
// Tulostaa palvelimen lämpötilojen välittämisen tilan (0 tai 1)
		case "status":
		    fileout.clientEventLog(komento, IP); // Tulostaa komennon lokitiedostoon
		    os.println(srvC.getStatus());        // Lämpöarvojen välityksen tilan tulostus
		    break;
// Client sammuttaa ohjelman
		case "exit":
		    fileout.clientEventLog(komento, IP); // Tulostaa komennon lokitiedostoon
		    ulosta = 1;                          // ylemmästä loopista poistuminen
		    break;
// Kehittäjille: listaa säikeet
		case "list threads":
		    Set<Thread> threads = Thread.getAllStackTraces().keySet();
		    for (Thread t : threads) {
			String name = t.getName();
			Thread.State state = t.getState();
			int priority = t.getPriority();
			String type = t.isDaemon() ? "Daemon" : "Normal";
			os.printf("%-30s \t %s \t %d \t %s\n", name, state, priority, type);
		    }
		    break;
// Toiminto tuntemattomille komennoille
		default:
		    os.println(PERUSPALAUTE);
		    // Tulostaa komennon lokitiedostoon
		    fileout.clientEventLog("Unknown command(" + komento + ")", IP);
	    }   // SWITCHin loppusulje
	    os.println("\nQQ");	// Tämä on lopetuskäsky clientin "multiline loopille"
	}   // Ohjelmaloopin loppusulje (while)
	System.out.println("Client disconnected.");
    }	// Metodin loppusulje (void run)
} // Luokan loppusulje (MainMenu)
