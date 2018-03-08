//  Versio 0.3
//  Versionumeroinnin aloitus
//------------------------------------------------------------------------------
package thetempsensclient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Jukka
 */
public class AddTemp {

    void addNewTemp(Socket soketti) {
	DataOutputStream os = null;
	try {

	    os = new DataOutputStream(soketti.getOutputStream());

	    Scanner input = new Scanner(System.in);  // Reading from System.in

	    System.out.print("\nAnna lämpötila [double]: ");
	    double newTempInput = Double.parseDouble(input.next());

	    System.out.print("Anna anturin numero [int]: ");
	    double sensNum = Integer.parseInt(input.next());
	    int newSensNum = (int) sensNum;

	    os.writeBytes("add temp" + "\n");
	    os.writeBytes(newTempInput + "\n");
	    os.writeBytes(newSensNum + "\n");

	} catch (IOException e) {
	    System.err.println("IOException:  " + e);
	}
    }

}
