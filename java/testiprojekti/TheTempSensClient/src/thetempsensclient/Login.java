package thetempsensclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author PetShopBoys
 */
public class Login {

    private String username;	// Alustetaan käyttäjätunnuksen muuttuja
    private String password;	// Alustetaan salasanan muuttuja
    private String responssi;	// Alustetaan palautteen muuttuja

    public String login(Socket soketti) {
	try {
	    DataOutputStream os =   new DataOutputStream(soketti.getOutputStream());	// Output stream
	    DataInputStream is =    new DataInputStream(soketti.getInputStream());	// Input stream
	    Scanner input =	    new Scanner(System.in);		    // Reading from System.in

	    System.out.print("\nUsername: ");
	    String userInput = input.next();	// Luetaan username käyttäjältä

	    System.out.print("Password: ");
	    String passInput = input.next();	// Luetaan password käyttäjältä

	    os.writeBytes("login\n");		// Lähetetään palvelimelle: login
	    os.writeBytes(userInput + "\n");	// Lähetetään palvelimelle: username
	    os.writeBytes(passInput + "\n");	// Lähetetään palvelimelle: password

	    responssi = is.readLine();		// Luetaan palvelimen vastaus

	} catch (IOException e) {
	    System.err.println("IOException:  " + e);
	    responssi = "0";			// Jos epäonnistuu, niin palaute "0"
	}
	return responssi;			// Palautetaan palvelimen vastaus
    }
}
