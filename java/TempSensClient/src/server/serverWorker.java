/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author juksu
 */
class ServerWorker implements Runnable {

    private final Socket server;
    private int serverActive = 1;
//    private String textArea;

//Constructor
    ServerWorker(Socket server, String textArea) {
	this.server = server;
//	this.textArea = textArea;
    }

    @Override
    public void run() {
	String line;
	BufferedReader in = null;
	PrintWriter out = null;
	try {
	    in = new BufferedReader(new InputStreamReader(server.getInputStream()));
	    out = new PrintWriter(server.getOutputStream(), true);
	} catch (IOException e) {
	    System.out.println("worker: in or out failed");
	    System.exit(-1);
	}
//	int serverActive = 1;
	while (serverActive == 1) {
	    try {
		line = in.readLine();
		if (line.equals("stop")) {
		    System.out.println("Server status: STOPPED");
		    serverActive = 0;
		    break;
		} else {
		    System.out.println("Client: " + line);
		}
//Send data back to client
		out.println(line);
	    } catch (IOException e) {
		System.out.println("Client quit.");
		break;
		/* System.out.println("worker: Read failed\n" + e);
		    System.exit(-1); */
	    }
	}
    }
}
