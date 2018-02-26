/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thetempsenssensor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Joakim
 */
public class LoginSensor {
      private String username;	// Alustetaan käyttäjätunnuksen muuttuja
    private String password;	// Alustetaan salasanan muuttuja
    private String responssi;	// Alustetaan palautteen muuttuja

    public String login(Socket soketti) {
	try {
	    DataOutputStream os =   new DataOutputStream(soketti.getOutputStream());	// Output stream
	    DataInputStream is =    new DataInputStream(soketti.getInputStream());	// Input stream
	//    Scanner input =	    new Scanner(System.in);		    // Reading from System.in

	//    System.out.print("\nUsername: ");
	//    String userInput = input.next();	// Luetaan username käyttäjältä

	//    System.out.print("Password: ");
	//    String passInput = input.next();	// Luetaan password käyttäjältä

	    os.writeBytes("login\n");		// Lähetetään palvelimelle: login
	    os.writeBytes("sensoren" + "\n");	// Lähetetään palvelimelle: username
	    os.writeBytes("sensoren" + "\n");	// Lähetetään palvelimelle: password

	    responssi = is.readLine();		// Luetaan palvelimen vastaus

	} catch (IOException e) {
	    System.err.println("IOException:  " + e);
	    responssi = "0";			// Jos epäonnistuu, niin palaute "0"
	}
	return responssi;			// Palautetaan palvelimen vastaus
    }
}
