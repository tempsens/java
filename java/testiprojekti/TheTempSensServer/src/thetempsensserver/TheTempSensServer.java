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

    public static void main(String args[]) {
	// declaration section:
	// declare a server socket and a client socket for the server
	Socket clientSocket = null;

	try {
	    // Try to open a server socket on port PORT
	    ServerSocket serveri = new ServerSocket(PORT);
	    while (true) { // Loopataan
		Thread t = new Thread(new MainMenu(serveri.accept()));
		t.start();
	    }
	} catch (IOException e) {
	    System.out.println(e);
	}
    }
}
