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
import java.util.Scanner;

/**
 *
 * @author juksu
 */
class ClientWorker implements Runnable {

    private Socket client;
    private String textArea;

//Constructor
    ClientWorker(Socket client, String textArea) {
	this.client = client;
	this.textArea = textArea;
    }

    @Override
    public void run() {
	String komento = "";
	BufferedReader in = null;
	PrintWriter out = null;
	try {
	    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	    out = new PrintWriter(client.getOutputStream(), true);
	    
	} catch (IOException e) {
	    System.out.println("worker: in or out failed");
	    System.exit(-1);
	}


	while (true) {
	    try {
		komento = in.readLine();
//Send data back to client
		out.println(komento);
	    } catch (IOException e) {
		System.out.println("worker: Read failed\n" + e);
		System.exit(-1);
	    }
	}
    }
}
