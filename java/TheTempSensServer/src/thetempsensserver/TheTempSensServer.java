//  Versio 1.0  09.03.2018  Tiimi
//   Julkaisu
//------------------------------------------------------------------------------
//  Versio 0.7   Jukka
//  Kommentointeja lisätty
//------------------------------------------------------------------------------
//  Versio 0.2
//  Versionumeroinnin aloitus
//------------------------------------------------------------------------------
package thetempsensserver;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author PetShopBoys
 */
public class TheTempSensServer {

    private static final String VERSION = "1.0"; // Ohjelman versionumero
    private static final int PORT = 1234;        // Palvelin kuuntelee tätä porttia
    public static int serverRunning = 0;         // Globaali muuttuja
    public static int sensorTaulu[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Globaali taulu sensorien valvontaan

    public static void main(String args[]) throws InterruptedException {
	int round = 1; // Yhteyden avauksen epäonnistumisen kierrosmittarin muuttuja 

	while (true) {
	    System.out.println("TempSensServer v" + VERSION + " starting...");
	    try {
		ServerSocket serveri = new ServerSocket(PORT); // Yritetään avata soketti
		System.out.println("TempSensServer v" + VERSION + " running...");
		round = 1; // Nollataan yhteyden avauksen epäonnistumisen kierrosmittari

		// Luodaan oma säie hoitamaan sensorien valvontaa
		sensorControl sC = new sensorControl();        // Luo uuden sensorControl -olion
		Thread sCt = new Thread(sC);                   // Luo uuden säikeen oliolle
		sCt.start();                                   // Käynnistää säikeen

		// Pääohjelmalooppi. Luo uuden säikeen uusille yhteyksille.
		MainMenu menu;             // Alustetaan menu
		while (true) {             // Loopataan uusien yhteyksien kuuntelua
		    menu = new MainMenu(serveri.accept()); // Luo uuden MainMenu -olion yhteydestä
		    Thread t = new Thread(menu);           // Luo uuden säikeen menu -oliolle
		    t.start();                             // Käynnistää säikeen
		}
	    } catch (IOException e) {
		// Käsittely yhteyden avauksen virheelle. Yleensä varatun porti takia.
		System.out.println(e);
		System.out.println("[" + round + "] Trying again in 10 seconds...\n");
		Thread.sleep(1000 * 10);	// Odottaa 10s
		round++;
	    }
	}
    }
}
