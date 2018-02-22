/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
	    if (this.serverRunning == 0) {
		this.serverRunning = 1;
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
	    if (this.serverRunning == 1) {
		this.serverRunning = 0;
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
	if (this.serverRunning == 1) {
	    this.serverRunning = 0;
	    DB db = new DB();
	    db.disconnect();
	    os.println("Server stopped.");
	} else {
	    os.println("Server already stopped.");
	}

	// DO START
	if (this.serverRunning == 0) {
	    this.serverRunning = 1;
	    os.println("Server started.");
	} else {
	    os.println("Server already started.");
	}
    }

    public int getStatus() {
	return this.serverRunning;
    }

}
