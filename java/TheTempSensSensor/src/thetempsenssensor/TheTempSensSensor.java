//  Versio 1.0  09.03.2018  Tiimi
//   Julkaisu
//------------------------------------------------------------------------------
//  Versio 0.5  08.03.2018  Jukka
//   Virhelooppia paranneltu
//   Palvelimen palautteen tulostus lisätty
//   Kommentointeja lisätty rajusti! :)
//------------------------------------------------------------------------------
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
//  Versionumeroinnin aloitus
//------------------------------------------------------------------------------
package thetempsenssensor;

import java.io.DataInputStream;
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

    private static final String VERSION = "1.0";             // Ohjelman versionumero
    private static final String HOSTNAME = "127.0.0.1";      // Palvelimen osoite
    private static final int PORT = 1234;                    // Määritetään käytettävä portti
    private static final int RETRIES = 200;                  // Yhteyden muodostuksen uudelleenyrityksiä
    private static final int SENSORNUM = 1;                  // Oletus anturin numero
    private static final int INTERVAL = 10;                  // Lähetystaajuus (sekuntia)

    public static void main(String[] args) {
	int sensori;                                         // Anturin numero
	int round = 1;                                       // Yhteysvirheen laskuri
	int userLevel = 0;                                   // Käyttäjätason alustus
	long startTime = System.currentTimeMillis();         // Ajastinmuuttuja
	long elapsedTime = 0L;                               // Ajastinmuuttuja
	String palaute = "";
	Socket MyClient = null;                              // Alustetaan soketti
	DataOutputStream os;                                 // Alustetaan output stream
	DataInputStream is;                   // Input streamin alustus
	System.out.println("TempSensClient v" + VERSION);    // Tulostetaan ohjelman versio

	ShuffleRefucked newShuffeli = new ShuffleRefucked(); // Satunnaislämpötilageneraattorin olio
	String loginresponssi;                               // Serverin palaute loginiin

	if (args.length > 0) {                  // Jos ohjelmalle on annettu käynnistyksessä parametrejä
	    try {                               // Yritetään parsia ensimmäinen parametri intiksi
		sensori = Integer.parseInt(args[0]);
	    } catch (NumberFormatException e) { // Muutoin mennään oletusnumerolla
		System.out.println("Going with default sensor number: " + SENSORNUM);
		sensori = SENSORNUM;
	    }
	} else {                                // Muutoin mennään oletusnumerolla
	    System.out.println("Going with default sensor number: " + SENSORNUM);
	    sensori = SENSORNUM;
	}
	if (sensori > 10) {                     // Tarkastetaan ettei ole annettu liian isoa numeroa
	    sensori = 10;                       // Tiputetaan liian suuret maksimiin (10)
	} else if (sensori < 1) {               // Tai ettei ole annettu liian pientä numeroa
	    sensori = 1;                        // Korotetaan liian pienet pienimpään
	}
// "Retries loop"
	while (round < RETRIES + 1) {           // Loopataan muuttujassa määrätty määrä
	    try {
		MyClient = new Socket(HOSTNAME, PORT);                 // Yritetään muodostaa yhteys
		os = new DataOutputStream(MyClient.getOutputStream()); // Yritetään muodostaa os
		is = new DataInputStream(MyClient.getInputStream());  // Input
		round = 1;                                             // Nollataan virhelaskuri onnistuessa
	    } catch (UnknownHostException e) {
		System.err.println("[" + round + "/" + RETRIES + "] os Creation: Don't know about host: " + HOSTNAME);
		userLevel = 0;                                        // Aiheuta "Logout"
		is = null;                                            // Nollataan is, jottei jatkotoimenpiteet toteudu
		os = null;                                            // Nollataan os, jottei jatkotoimenpiteet toteudu
		round++;                                              // Kasvatetaan kierrosmittaria
	    } catch (IOException e) {
		System.err.println("[" + round + "/" + RETRIES + "] os Creation: Couldn't get I/O for the connection to: " + HOSTNAME);
		userLevel = 0;                                        // Aiheuta "Logout"
		is = null;                                            // Nollataan is, jottei jatkotoimenpiteet toteudu
		os = null;                                            // Nollataan os, jottei jatkotoimenpiteet toteudu
		round++;                                              // Kasvatetaan kierrosmittaria
	    }

	    if (MyClient != null && os != null && is != null) {
		LoginSensor login = new LoginSensor();
// "loginLoop"
		while (userLevel < 1) {                                // Loopataan, kunnes saadaan userLevel
		    loginresponssi = login.login(MyClient);               // Suoritetaan login ja luetaan palaute
		    if (loginresponssi.contains("1|")) {                  // Jos palautteessa 1|, niin
			String[] leveli = loginresponssi.split("\\|");        // Hajotetaan responssi muuttujatauluun
			userLevel = Integer.parseInt(trim(leveli[2]));        // Poimitaan taulusta ja siistitään userLevel
		    } else if (loginresponssi.contains("0|")) {           // Tai jos palautteessa 0|, niin
			System.out.println("Login FAIL!");                    // Login epäonnistunut
		    } else {                                              // Muutoin
			System.out.println("Server says: " + loginresponssi); // Tulostetaan palvelimen palaute
			break;                                                // ja poistutaan loopista
		    }
// "sendLoop"
		    while (true) {
			try {
// Ajastin (X * 1000ms)
			    while (elapsedTime < INTERVAL * 1000) {
				elapsedTime = (new Date()).getTime() - startTime;
			    }
			    startTime = System.currentTimeMillis();
			    elapsedTime = 0L;                 // 0 "longina"

// Lähetetään käsky palvelimelle
			    String tempValue = newShuffeli.ShuffleTemp(2); // Arvotaan lämpötila
			    os.writeBytes("add temp" + "\n"); // Lähetetään käsky
			    os.writeBytes(tempValue + "\n");  // Lähetetään lämpötila
			    os.writeBytes(sensori + "\n");    // Lähetetään sensorin numero

// Haetaan ja muotoillaan päiväys konsoliin tulostusta varten
			    LocalDateTime now = LocalDateTime.now();
			    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			    String formatted = now.format(formatter);
			    System.out.println(formatted
				    //				+ "(Userlevel="+userLevel+")"
				    + " Sent temperature " + tempValue
				    + "\tfrom sensor " + sensori);

// Lukija palvelimen palautteelle
			    while (!palaute.contains("QQ")) {
				palaute = is.readLine();
				if (palaute.length() > 1 && !palaute.equals("QQ")) {
				    System.out.println("Server says: " + palaute);
				}
			    }
			    palaute = "";

			    round = 1;                        // Nollataan virhelaskuri onnistuessa
			} catch (IOException e) {
			    System.err.println("[" + round + "/" + RETRIES + "] Sendloop: Couldn't get I/O for the connection to: " + HOSTNAME);
			    break;                            // Poistutaan sendLoopista virheen sattuessa
			}
		    } // sendLoop loppusulje
		} // LoginLoop loppusulje
	    } // IF socket/os != null loppsulje
	} // Retries loopin loppusulje
    } // Pääohjelman loppusulje
} // Pääluokan loppusulje
