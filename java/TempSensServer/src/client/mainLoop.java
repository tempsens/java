/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author juksu
 */
public class mainLoop {

    String hostName = "localhost";
    Scanner scanner = new Scanner(System.in);
    String komento = "";

    public void start() {

	try {
	    Socket socket = new Socket(hostName, 4321);
	    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	while (!komento.equals(
		"exit")) {
	    System.out.print(": ");
	    komento = scanner.nextLine();
	    out.println(komento);
	}
	} catch (UnknownHostException e) {
	    System.out.println("cli: Unknown host: " + hostName + "\n" + e);
	    System.exit(1);
	} catch (IOException e) {
	    System.out.println("cli: No I/O");
	    System.exit(1);
	}

    }

}
