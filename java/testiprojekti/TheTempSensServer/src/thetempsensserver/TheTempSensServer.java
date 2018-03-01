//  Versio 0.2     
//              
//------------------------------------------------------------------------------
package thetempsensserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author PetShopBoys
 */
public class TheTempSensServer {

    private static final int MAX_CON_SPEED = 200;
    private static final String VERSION = "0.2";
    private static final int PORT = 1234; // Palvelin kuuntelee tätä porttia
    public static int serverRunning = 0; // Globaali muuttuja

    public static void main(String args[]) throws InterruptedException {
	// declaration section:
	// declare a server socket and a client socket for the server
	Socket clientSocket = null;
	int round = 1;

	while (true) {
	    System.out.println("TempSensServer v" + VERSION+ " starting...");
	    try {
		// Try to open a server socket on port PORT
		ServerSocket serveri = new ServerSocket(PORT);
		System.out.println("TempSensServer v" + VERSION+ " running...");
		
		MainMenu menu; // Alustetaan menu
		
		while (true) { // Loopataan uusia säikeitä aina kun uusi yhteys
		    menu = new MainMenu(serveri.accept()); // TODO: IF CONNS > 20 -> REFUSE -Jukka-
		    Thread t = new Thread(menu);
		    t.start();
		}
	    } catch (IOException e) {
		System.out.println(e);
		System.out.println("["+round+"] Trying again in 10 seconds...\n");
		Thread.sleep(10000);	// Odottaa 10000ms = 10s
		round++;
	    }
	}
    }
}
