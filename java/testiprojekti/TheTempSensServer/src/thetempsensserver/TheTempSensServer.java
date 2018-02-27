//  Versio 0.1     
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

    public int serverRunning = 0;
    private static final int PORT = 1234;
    
    public static void main(String args[]) throws InterruptedException {
	// declaration section:
	// declare a server socket and a client socket for the server
	Socket clientSocket = null;
	int round = 1;

	while (true) {
	    System.out.println("TempSensServer starting...");
	    try {
		// Try to open a server socket on port PORT
		ServerSocket serveri = new ServerSocket(PORT);
		System.out.println("TempSensServer running...");
		while (true) { // Loopataan uusia säikeitä aina kun uusi yhteys
		    Thread t = new Thread(new MainMenu(serveri.accept()));
		    t.start();
		}
	    } catch (IOException e) {
		System.out.println(e);
		Thread.sleep(10000);
		System.out.println("["+round+"] Trying again in 10 seconds...\n");
		round++;
	    }
	}
    }
}
