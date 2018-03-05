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
	    System.out.println("is/os creation failed: " + e);
	}
    }

    @Override
    public void run() {
//	String IP = clinu.getRemoteSocketAddress().toString();

	int userLevel = 0;	// TEMPORARY - LOGIN WILL OVERRIDE
	int userID = 0;
	// Muuttujat ja luokat pääohjelmalle
//	String commandLog = ""; // Aloitetaan tyhjällä komentologilla
	String komento;
	String today = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ").format(new Date());

	Inputti inputti = new Inputti();          // Tarvitaan help -tekstin tulostukseen
	FileOut fileout = new FileOut();          // Tarvitaan tiedosto outputiin
	serverControl srvC = new serverControl(); // Start/Stop/Restart
	int ulosta = 0;                           // Muuttuja while -loopista poistumiseksi

	while (userLevel < 1) {
	    try {
		komento = is.readLine();
		if (komento.equals("login")) {
		    try {
			String userName = is.readLine();
			String userPass = is.readLine();

			DB user = new DB();
			user.connect();
			String vastaus = user.checkUser(userName, userPass, IP);

			if (vastaus.equals("-1")) {
			    os.println("Server database error! Only admin login possible.");

			    while (userLevel < 10) {
				komento = is.readLine();
				if (komento.equals("login")) {
				    try {
					userName = is.readLine();
					userPass = is.readLine();
					if (userName.equals("admin") && userPass.equals("0000")) {
					    userLevel = 10;

					    os.println("1|-1");
					} else {
					    os.print("Server database error! Only admin login possible.");
					    fileout.clientEventLog("Server database error! Only admin login possible.", IP);

					}
				    } catch (IOException e) {
					System.out.println("BackupAdminLoginLoop readLine fail: " + e);
					fileout.clientEventLog("BackupAdminLoginLoop readLine fail", IP);

				    }
				}
                            }
                        } else if(vastaus.equals("0")) {
                            os.println("0");
			} else {
			    String[] vastaukset = vastaus.split("\\|");

			    userLevel = Integer.parseInt(vastaukset[0]); // Luetaan käyttäjätaso muuttujaan
			    userID = Integer.parseInt(vastaukset[1]);
			    os.println("1|" + userID);
			} 
			//	}
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
	// inputti.Help(clinu, userLevel); // Tulostaa helpin				EI PERKELE TOIMI -Jukka- :(
	// ^ Johtuen siitä herkästä systeemistä server-client -kommunikaatiossa, että kuka kuuntelee millonkin
	while (ulosta < 1 && userLevel > 0) { // Pääloop komentojen kuunteluun        LISÄTTY UserLevel vaatimus Joakim
	    try {
		komento = is.readLine();    // Luetaan yksi rivi muuttujaan
		System.out.println(IP + ": " + komento); // DEBUG

	    } catch (IOException e) {
		System.out.println("MainLoop readLine fail: " + e);
		break;		    // Poistutaan loopista jos luku epäonnistuu
		// ^	HUOM! Sammuttaa pääohjelman jos client droppaa!!! DAS IS NICHT GUT! -- EI OLLU TÄÄ -> Saa jäädä.
	    }
	    if (komento == null) {
		break;
	    }
	    switch (komento.toLowerCase()) {
		case "help": // Tulostaa helpin

		    // help + userlever --> passaa Inputtiin...
		    System.out.println("Helpin tulostus...");
		    fileout.clientEventLog(komento, IP);
		    inputti.Help(clinu, userLevel); // Tulostaa helpin
		    break;

		case "change pass":
		    try {
			String strUserID = is.readLine();
			String strNewPass = is.readLine();
			DB cp = new DB();

			if (cp.changepass(Integer.parseInt(strUserID), strNewPass) == 1) {
			    os.println("Salasana vaihdettu!");
			} else {
			    os.println("Error with change password");
			}
		    } catch (IOException e) {
			System.out.println(e);
			os.println("Error with change password connection");
		    }
		    break;

		case "add": // Väärä syntaksi -> antaa vain virheilmoituksen
		    fileout.clientEventLog(komento, IP);
		    os.println("Missing parameter! (user or temp needed)");
		    break;

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

		case "add temp": // Lämpötilan lisääminen
		    if (userLevel >= 5) {
			try {
			    double tempvalue = Double.parseDouble(is.readLine());
			    int sensori = Integer.parseInt(is.readLine());

			    DB uusiTemp = new DB();
			    System.out.println(tempvalue);	// FOR DEBUG
			    System.out.println(sensori);	// FOR DEBUG
			    int palaute = uusiTemp.insertTemp(tempvalue, sensori, today, IP);
			    System.out.println("add temp (insertTemp palaute: "+ palaute);
                            if (palaute == -1) {
				os.println("Server database error! Command not available.");
			    } else if (palaute == -2) {
				os.println("Server error!");
			    }
			} catch (IOException e) {
			    System.out.println("readLine error in add temp: " + e);
			}
		    } else {
			System.out.println(PERUSPALAUTE);
			fileout.clientEventLog("Add User (UserLevel too low (" + userLevel + ")", IP);
		    }
		    break;

		case "list": // Väärä syntaksi -> antaa vain virheilmoituksen
		    fileout.clientEventLog(komento, IP);

		    os.println("Missing parameter! (users or temps needed)");
		    break;

		case "list users": // Tulostaa listan käyttäjistä
		    if (userLevel >= 5) {
			DB listuser = new DB();
			int palaute = listuser.listUsers(clinu);
			if (palaute == -1) {
			    os.println("Server database error! Command not available.");
			} else if (palaute == -2) {
			    os.println("Server error!");
			}
		    }
		    break;

		case "list temps": // Tulostaa listan lämpötiloista (100 viimeisintä)
		    if (userLevel >= 5) {
			DB listtemps = new DB();
			int palaute = listtemps.listTemps(clinu);
			if (palaute == -1) {
			    os.println("Server database error! Command not available.");
			} else if (palaute == -2) {
			    os.println("Server error!");
			}
		    }
		    break;

		case "fileout": // Väärä syntaksi -> antaa vain virheilmoituksen
		    if (userLevel >= 5) {
			fileout.clientEventLog(komento, IP);

			os.println("Missing parameter! (console or userlist needed)");
		    }
		    break;

/*              case "fileout console": // Tulostetaan komentologi		    TÄMÄ CLIENTIIN? -Jukka-
		    if (userLevel >= 5) {
			fileout.clientEventLog(komento, IP);

			fileout.console(commandLog, clinu);
		    }
		    break;
*/
		case "fileout users": // Tulostetaan lista käyttäjistä
		    if (userLevel >= 5) {
			int palaute = fileout.userlist(clinu);
		    }
		    break;

		case "quit": // OK!!
		    fileout.clientEventLog(komento, IP);

		    os.println("Server quit.");
		    os.println("QQ"); // Lähetetään QQ lähetyksen lopuksi
		    ulosta = 1;	    // Poistuu while -loopista
		    System.exit(-1);
		    break;
		case "start":	// Käynnistää tietojen välityksen
		    fileout.clientEventLog(komento, IP);

		    srvC.start(clinu);
		    break;
		case "stop":	// Pysäyttää tietojen välityksen
		    fileout.clientEventLog(komento, IP);

		    srvC.stop(clinu);
		    break;
		case "restart":	// Käynnistää tietojen välityksen uudelleen
		    fileout.clientEventLog(komento, IP);

		    srvC.restart(clinu);
		    break;
		case "status":	// Tulostaa tietojen välityksen tilan
		    fileout.clientEventLog(komento, IP);

		    os.println(srvC.getStatus());
		    break;

		default:
		    //   fileout.clientEventLog(komento, IP);

		    os.println(PERUSPALAUTE);
		    fileout.clientEventLog("Unknown command(" + komento + ")", IP);

	    }   // SWITCHin loppusulje
//	    System.out.println("Jumi 666");

	    os.println("\nQQ");
	}   // Ohjelmaloopin loppusulje (while)
	System.out.println("Client disconnected.");
    }	// Metodin loppusulje (void run)
} // Luokan loppusulje (MainMenu)
