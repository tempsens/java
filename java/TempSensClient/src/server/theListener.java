/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author juksu
 */
public class theListener {

    public void listenSocket() {
//	String hostName = "localhost";
	int serverPort = 4321;
	
	String textArea = "cl: Testiä, testiä";
	ServerWorker w;
	try {
	    ServerSocket server = new ServerSocket(serverPort);
	    System.out.println("Server started!\n");

	    while (true) {
		try {
//server.accept returns a client connection
		    w = new ServerWorker(server.accept(), textArea);
		    Thread t = new Thread(w);
		    t.start();
		    System.out.println("Client connected!");
		} catch (IOException e) {
		    System.out.println("srv: Accept failed: " + serverPort);
		    System.exit(-1);
		}
	    }
	} catch (IOException e) {
	    System.out.println("srv: Could not listen on port " + serverPort);
	    System.exit(-1);
	}
    }
}
