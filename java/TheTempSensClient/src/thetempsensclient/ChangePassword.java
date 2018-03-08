//  Versio 0.4
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
public class ChangePassword {

    void change(Socket soketti, int userID) {

        DataOutputStream os = null;
        try {

            os = new DataOutputStream(soketti.getOutputStream());

            Scanner input = new Scanner(System.in);  // Reading from System.in
            String newPass = "";
            String newPassToisto = ".";

            while (!newPass.equals(newPassToisto)) {
                System.out.print("Anna uusi salasana: ");
                newPass = input.nextLine();

                System.out.print("Toista salasana: ");
                newPassToisto = input.nextLine();
            }

            os.writeBytes("change pass" + "\n");
            os.writeBytes(userID + "\n");
            os.writeBytes(newPass + "\n");

        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }

    }

}
