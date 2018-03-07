//  Versio 0.4  01.03.2018  Jukka
//   Ylimääräisiä konsoliviestejä karsittu
//   Datetime lähetysviestin eteen
//   Anturin numero ja lähetystaajuus lisätty muuttujiin
//------------------------------------------------------------------------------
//  Versio 0.3  01.03.2018  Jukka
//   Versionumero lisätty
//   Kommentointeja lisätty
//   Login tukemaan serverin uutta palautetta (sisältää myös infotekstiä)
//------------------------------------------------------------------------------
//  Versio 0.2  01.03.2018  masa     
//              lisätty inteval yhteyden luontiin
//------------------------------------------------------------------------------
//  Versio 0.1     
//              
//------------------------------------------------------------------------------
package thetempsenssensor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import static jdk.nashorn.internal.objects.NativeString.trim;

/**
 *
 * @author PetShopBoys
 */
public class TheTempSensSensor {

    private static final int SENSORNUM = 1;                  // Oletus anturin numero
    private static final int INTERVAL = 5;                   // Lähetystaajuus (sekuntia)
    private static final String VERSION = "0.4";             // Ohjelman versionumero
    private static final String HOSTNAME = "127.0.0.1";      // Palvelimen osoite
    private static final int PORT = 1234;                    // Määritetään käytettävä portti

    public static void main(String[] args) {
	int sensori;
	long startTime = System.currentTimeMillis();         // Ajastinmuuttuja
	long elapsedTime = 0L;                               // Ajastinmuuttuja
	Socket MyClient = null;                              // Alustetaan soketti
	DataOutputStream os = null;                          // Alustetaan output stream
	System.out.println("TempSensClient v" + VERSION);    // Tulostetaan ohjelman versio

	ShuffleRefucked newShuffeli = new ShuffleRefucked(); // Satunnaislämpötilageneraattorin olio
	String loginresponssi;                               // Serverin palaute loginiin
	int userLevel = 0;  // Käyttäjätason alustus
	int retries = 20;   // Yhteyden muodostuksen uudelleenyrityksiä

	if (args.length > 0) {
	    try {
		sensori = Integer.parseInt(args[0]);
	    } catch (NumberFormatException e) {
		System.out.println("Going with default sensor number: " + SENSORNUM);
		sensori = SENSORNUM;
	    }
	} else {
	    System.out.println("Going with default sensor number: " + SENSORNUM);
	    sensori = SENSORNUM;
	}
	if (sensori > 10) {
	    sensori = 10;
	} else if (sensori < 1) {
	    sensori = 1;
	}

	while (retries > 0) {
	    retries--;
	    try {
		MyClient = new Socket(HOSTNAME, PORT);
		os = new DataOutputStream(MyClient.getOutputStream());
	    } catch (UnknownHostException e) {
		System.err.println("Don't know about host: " + HOSTNAME);
	    } catch (IOException e) {
		System.err.println("Couldn't get I/O for the connection to: " + HOSTNAME);
		userLevel = 0;
	    }

	    if (MyClient != null && os != null) {
		LoginSensor login = new LoginSensor();

		while (userLevel < 1) {
		    loginresponssi = login.login(MyClient);
		    if (loginresponssi.contains("1|")) {
			String[] leveli = loginresponssi.split("\\|");
			userLevel = Integer.parseInt(trim(leveli[2]));
			//System.out.println("Login success! (level " + userLevel + ")"); // FOR DEBUG
		    } else if (loginresponssi.contains("0|")) {
			System.out.println("Login FAIL!");
		    } else {
			System.out.println("Server says: " + loginresponssi);
		    }
		    //System.out.println("userLevel looppi. userLevel=" + userLevel); // FOR DEBUG
		}

		while (true) {
		    try {
			while (elapsedTime < INTERVAL * 1000) { // Ajastin (5 * 1000ms)
			    elapsedTime = (new Date()).getTime() - startTime;
			}
			startTime = System.currentTimeMillis();
			elapsedTime = 0L;

			os.writeBytes("add temp" + "\n"); // Lähetetään käsky
			//arvottu lämpötila
			String tempValue = newShuffeli.ShuffleTemp(2);
			os.writeBytes(tempValue + "\n"); // Lähetetään lämpötila
			// sensorin numero
			os.writeBytes(sensori + "\n"); // Lähetetään sensorin numero
			// Tulostetaan konsoliin
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String formatted = now.format(formatter);
			System.out.println("[" + userLevel + "] " + formatted
				+ " Sent temperature " + tempValue
				+ "\tfrom sensor " + sensori);
		    } catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + HOSTNAME);
			break;
		    }
		}
	    }
	} // Intervalliloopin loppsulje
    }
}
