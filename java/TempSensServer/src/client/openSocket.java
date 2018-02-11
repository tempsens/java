/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author juksu
 */
public class openSocket {
    private Socket client;


    public void openSocket() {
	String hostName = "localhost";
	// Create socket connection
	try {
	    Socket socket = new Socket(hostName, 4321);
	    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    System.out.println("Connected to server.");
	    mainLoop looppi = new mainLoop();
	    looppi.start();
	    
	} catch (UnknownHostException e) {
	    System.out.println("cli: Unknown host: " + hostName + "\n" + e);
	    System.exit(1);
	} catch (IOException e) {
	    System.out.println("cli: No I/O");
	    System.exit(1);
	}
    }
}
