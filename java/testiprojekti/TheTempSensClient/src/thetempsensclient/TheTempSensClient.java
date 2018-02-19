/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thetempsensclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author Joakim
 */
public class TheTempSensClient {

    // CLIENT
    /**
     * @param args the command line arguments
     */
    private static final int PORT = 1234;

    public static void main(String[] args) {

	Socket MyClient = null;
	DataOutputStream os = null;
	DataInputStream is = null;
	try {
	    MyClient = new Socket("127.0.0.1", PORT);
	    os = new DataOutputStream(MyClient.getOutputStream());
	    is = new DataInputStream(MyClient.getInputStream());
	} catch (UnknownHostException e) {
	    System.err.println("Don't know about host: hostname");
	} catch (IOException e) {
	    System.err.println("Couldn't get I/O for the connection to: hostname");
	}
	if (MyClient != null && os != null && is != null) {
	    try {
		// The capital string before each colon has a special meaning to SMTP
		// you may want to read the SMTP specification, RFC1822/3
		//        os.writeBytes("help\n");
		// keep on reading from/to the socket till we receive the "Ok" from SMTP,
		// once we received that then we want to break.
		String responseLine;
		Scanner scanner = new Scanner(System.in);
		String komento = "";

		while (!komento.equals("exit")) {	    // exit ei toimi
		    System.out.print(": ");
		    komento = scanner.nextLine();
		    os.writeBytes(komento + "\n");

			while (true) {
			String responssi = is.readLine();

			if (responssi.contains("QQ")) {
			    break;
			}

			System.out.println(responssi);

		    } // Sisempi While loppu (MULTILINE)
		} // Ulompi While loppu (EXIT PROGRAM)

// clean up:
		System.out.println("PUHDISTAA");

		// close the output stream
		// close the input stream
		// close the socket
		os.close();
		is.close();
		MyClient.close();

	    } catch (UnknownHostException e) {
		System.err.println("Trying to connect to unknown host: " + e);
	    } catch (IOException e) {
		System.err.println("IOException:  " + e);
	    }
	}
    }

}
