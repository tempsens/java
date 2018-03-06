//  Versio 0.5
//   status palauttaa nyt tekstin, ei pelkkää numero (0 tai 1)
//   this.serverRunning muuttujaviittaukset vaihdettu serverControl.serverRunning
//------------------------------------------------------------------------------
//  Versio 0.2
//              
//------------------------------------------------------------------------------
package thetempsensserver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author PetShopBoys
 */
public class serverControl extends TheTempSensServer {

    PrintStream os = null;

    public void start(Socket soketti) {
	try {
	    os = new PrintStream(soketti.getOutputStream());
	    if (serverControl.serverRunning == 0) {
		serverControl.serverRunning = 1;
		os.println("Server started.");
	    } else {
		os.println("Server already started.");
	    }
	} catch (IOException e) {
	    System.out.println(e);
	}

    }

    public void stop(Socket soketti) {
	try {
	    os = new PrintStream(soketti.getOutputStream());
	    if (serverControl.serverRunning == 1) {
		serverControl.serverRunning = 0;
		DB db = new DB();
		db.disconnect();
		os.println("Server stopped.");
	    } else {
		os.println("Server already stopped.");
	    }
	} catch (IOException e) {
	    System.out.println(e);
	}

    }

    public void restart(Socket soketti) {
	try {
	    os = new PrintStream(soketti.getOutputStream());
	} catch (IOException e) {
	    System.out.println(e);
	}

	// DO STOP
	if (serverControl.serverRunning == 1) {
	    serverControl.serverRunning = 0;
	    DB db = new DB();
	    db.disconnect();
	    os.println("Server stopped.");
	} else {
	    os.println("Server already stopped.");
	}

	// DO START
	if (serverControl.serverRunning == 0) {
	    serverControl.serverRunning = 1;
	    os.println("Server started.");
	} else {
	    os.println("Server already started.");
	}
    }

    public String getStatus() {
	String serverStatus;
	if(serverControl.serverRunning == 1) {
	    serverStatus = "Server started. ("+serverControl.serverRunning+")";
	} else {
	    serverStatus = "Server stopped. ("+serverControl.serverRunning+")";
	}
	return serverStatus;
    }

}
