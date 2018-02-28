//  Versio 0.1     
//              
//------------------------------------------------------------------------------
package ohjelmistotestaus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author PetShopBoys
 */
public class Ohjelmistotestaus {

    private static final String VERSION = "0.1";
    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 1234;

    public static void main(String[] args) {
	DataInputStream is = null;
	DataOutputStream os = null;
	Socket MyClient = null;

	Scanner skanneri = new Scanner(System.in);
	String toiminto;
	int getOut = 0; // Exit variable for while loop
	long round = 0; // Loop round counter variable

	while (getOut == 0) {
	    System.out.println("\n\n\n\n\n\n\nTempSens ohjelmistotestaus, v" + VERSION);
	    System.out.println("----------------------------------\n"
		    + "Toiminnot:\n"
		    + " 0 = EXIT\n"
		    + " 1 = Server connection overflow\n"
		    + " 2 = Server input overflow\n");
	    System.out.print("Anna toiminto: ");
	    toiminto = skanneri.nextLine();
	    switch (toiminto) {
		case "0":
		    getOut = 1;
		    break;

		case "1":
		    try {
			while (true) { // Start connection overflow looper!
			    MyClient = new Socket(HOSTNAME, PORT);
			    System.out.println("[" + round + "] LOOPER START"); // Show how how many rounds looped
			    round++;
			}
		    } catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + HOSTNAME);
		    } catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + HOSTNAME);
		    }
		    break;

		case "2":
		    try { // Try to create socket and output stream
			MyClient = new Socket(HOSTNAME, PORT);
			os = new DataOutputStream(MyClient.getOutputStream());
		    } catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + HOSTNAME);
		    } catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + HOSTNAME);
		    }
		    if (MyClient != null && os != null) { // Check that connection and output stream is not null
			while (true) { // Start input overflow looper!
			    try {
				System.out.println("[" + round + "] LOOPER START"); // Show how how many rounds looped
				os.writeBytes("0\n"); // Send 0 and ENTER to server
			    } catch (IOException e) {
				System.err.println("Couldn't get I/O for the connection to: " + HOSTNAME);
			    }
			    round++;
			} // WHILE LOPPUSULJE
		    } // IF LOPPUSULJE
		    break;
		default:
	    } // SWITCH LOPPUSULJE
	} // PÄÄLOOPIN LOPPUSULJE
    } // PÄÄOHJELMAN LOPPUSULJE

}
