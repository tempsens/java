//  Versio 0.2
//  Versionumeroinnin aloitus
//------------------------------------------------------------------------------
package thetempsensclient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Joakim
 */
public class AddUser {

    void addNewUser(Socket soketti) {
	DataOutputStream os = null;
	try {

	    os = new DataOutputStream(soketti.getOutputStream());

	    Scanner input = new Scanner(System.in);  // Reading from System.in

	    System.out.print("\nAnna käyttäjänimi: ");
	    String newUserInput = input.next();

	    System.out.print("Anna käyttäjätaso: ");
	    double UserLevel = Integer.parseInt(input.next());
	    int newUserLevel = (int) UserLevel;

	    os.writeBytes("add user" + "\n");
	    os.writeBytes(newUserLevel + "\n");
	    os.writeBytes(newUserInput + "\n");

	} catch (IOException e) {
	    System.err.println("IOException:  " + e);
	}
    }

}
