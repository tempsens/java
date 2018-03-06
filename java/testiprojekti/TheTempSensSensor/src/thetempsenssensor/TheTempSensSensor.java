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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

/**
 *
 * @author PetShopBoys
 */
public class TheTempSensSensor {

    /**
     * @param args the command line arguments
     */
    private static final String VERSION =   "0.3";           // Ohjelman versionumero
    private static final String HOSTNAME =  "127.0.0.1";     // Palvelimen osoite
    private static final int PORT =         1234;            // Määritetään käytettävä portti

    public static void main(String[] args) {
	long startTime = System.currentTimeMillis();         // Ajastinmuuttuja
	long elapsedTime = 0L;                               // Ajastinmuuttuja
        Socket MyClient = null;                              // Alustetaan soketti
        DataOutputStream os = null;                          // Alustetaan output stream
        System.out.println("TempSensClient v" + VERSION);    // Tulostetaan ohjelman versio

	ShuffleRefucked newShuffeli = new ShuffleRefucked(); // Satunnaislämpötilageneraattorin olio
	String loginresponssi;                               // Serverin palaute loginiin
	int userLevel = 0;
	int interval = 200;

	while (interval > 0) {
	    interval--;
	    try {
		MyClient = new Socket(HOSTNAME, PORT);
		os = new DataOutputStream(MyClient.getOutputStream());
	    } catch (UnknownHostException e) {
		System.err.println("Don't know about host: " + HOSTNAME);
	    } catch (IOException e) {
		System.err.println("Couldn't get I/O for the connection to: " + HOSTNAME);
	    }

	    if (MyClient != null && os != null) {
		LoginSensor login = new LoginSensor();

		while (userLevel < 1) {
		    loginresponssi = login.login(MyClient);
		    if (loginresponssi.contains("1|")) {
			String[] leveli = loginresponssi.split("\\|");
			userLevel = Integer.parseInt(leveli[0]);
			System.out.println("Login success! (level "+userLevel+")");
		    } else if (loginresponssi.contains("0|")) {
			System.out.println("Login FAIL!");
		    } else {
			System.out.println("Server says: "+loginresponssi);
		    }
System.out.println("userLevel looppi. userLevel="+userLevel); // FOR DEBUG
		}
System.out.println("WE ARE IN!"); // FOR DEBUG
		while (true) {
		    try {
			while (elapsedTime < 4 * 1000) {
			    //perform db poll/check
			    elapsedTime = (new Date()).getTime() - startTime;
			}
			//System.out.println("Ulkona Silmukassa"); // FOR DEBUG
			startTime = System.currentTimeMillis();
			elapsedTime = 0L;

			os.writeBytes("add temp" + "\n");
			//arvottu lämpötila
			os.writeBytes(newShuffeli.ShuffleTemp(2) + "\n");
			// sensorin numero
			os.writeBytes("1\n");
			System.out.println("Sinne meni\n"); // FOR DEBUG
		    } catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + HOSTNAME);
		    }
		}
	    }
	} // Intervalliloopin loppsulje
    }
}
