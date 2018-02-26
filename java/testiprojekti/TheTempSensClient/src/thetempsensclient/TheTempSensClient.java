package thetempsensclient;

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
public class TheTempSensClient {

    // CLIENT
    private static final String HOSTNAME = "127.0.0.1"; // Palvelimen osoite
    private static final int PORT = 1234; // Määritetään käytettävä portti

    public static void main(String[] args) {

	Socket MyClient =	null; // Alustetaan soketti
	DataOutputStream os =	null; // Alustetaan output stream
	DataInputStream is =	null; // Alustetaan input stream
	try {
	    MyClient =	new Socket(HOSTNAME, PORT);			    // Määritetään soketti
	    os =	new DataOutputStream(MyClient.getOutputStream());   // Määritetään output stream
	    is =	new DataInputStream(MyClient.getInputStream());	    // Määritetään input stream
	} catch (UnknownHostException e) {
	    System.err.println("Don't know about host: hostname");
	} catch (IOException e) {
	    System.err.println("Couldn't get I/O for the connection to: hostname");
	}
	if (MyClient != null && os != null && is != null) {
	    try {
		Scanner scanner = new Scanner(System.in);
		String komento = "";
		String loginresponssi = "0";

		// Kirjautuminen, tarkistetaan käyttöoikeudet
		Login login = new Login();
		while (true) {
		    loginresponssi = login.login(MyClient);
		    if (loginresponssi.equals("1")) {
			break;
		    } else {
			System.out.println(loginresponssi);
		    }

		}
			System.out.println("DEBUG: ennen mainlooppia");

		if (loginresponssi.equals("1")) {

		    //  while (login.login(MyClient) == "1") {
		    while (!komento.equals("exit")) {
			System.out.print(": ");
			komento = scanner.nextLine();

			if (komento.equals("add user")) {

			    AddUser addUser = new AddUser();
			    addUser.addNewUser(MyClient);
			} else if (komento.equals("add temp")) {
			    System.out.print("Anna lämpötila [double]: ");
			    os.writeBytes(scanner.nextLine());
			    System.out.print("Anna anturin numero [int]: ");
			    os.writeBytes(scanner.nextLine());
			} else {
			    os.writeBytes(komento + "\n");
			}
//			while (!responssi.contains("QQ")) {
			while (true) {
			    String responssi = is.readLine();
			    //System.out.println("Jumi 1");
			    System.out.println("outer loop: Saatiin palaute: " + responssi);

			    if (responssi.contains("QQ")) {
				System.out.println("inner loop: Saatiin palaute QQ: Ulos loopista.");

				break;
			    }
//			    System.out.println(responssi);

			} // Sisempi While loppu (MULTILINE)
		    } // Ulompi While loppu (EXIT PROGRAM)
		}

// clean up:
		System.out.println("Bye. Thanks for choosing this awesome software and we hope you will be using it again and recommend it"
			+ "for your friends too! Some day we will be BIG and you may be proud about using this software from the beginning."
			+ "It may also be nice if you would donate some change to poor students. Jukka has drinken teams all money...");

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
