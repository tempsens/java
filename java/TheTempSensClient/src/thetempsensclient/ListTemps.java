//  Versio 0.1
//              
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
public class ListTemps {

    void ListTemps(Socket soketti) {
        DataOutputStream os = null;
        // regu PP.KK.VVVV  String regu = "^(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).(\\d{4})$";
        String regu = "^(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$";

        try {
            System.out.println("\nAnna päivämäärien alkamis- ja päättymispäivämäärät\n"
                    + "poistu toiminnosta laittamalla x molempiin päivämääriin\n");

            os = new DataOutputStream(soketti.getOutputStream());

            Scanner input = new Scanner(System.in);  // Reading from System.in
            String startDate = "";
            String endDate = "";

            while (true) {
                System.out.print("\nAnna alkamispäivämäärä(VVVV-KK-PP tai x=jätä tyhjäksi): ");
                startDate = input.next();
                if ((startDate.matches(regu) || (startDate.equals("x")))) {
                    break;
                }

            }
            while (true) {
                System.out.print("\nAnna päättymispäivämäärä(VVVV-KK-PP tai x=jätä tyhjäksi)): ");
                endDate = input.next();
                if ((endDate.matches(regu) || (endDate.equals("x")))) {

                    break;
                }
            }

         //   System.out.println("saatu alkupvm: " + startDate);
         //   System.out.println("saatu loppupvm: " + endDate);
            int ulosta = 0;
            if (startDate.equals("x") && endDate.equals("x")) {
                ulosta = 1;
                // poistu
                os.writeBytes("QQ" + "\n");
            }
            if (ulosta == 0) {
                if (startDate.equals("x")) {
                    startDate = "";
                }
                if (endDate.equals("x")) {
                    endDate = "";
                }

                os.writeBytes("list temps" + "\n");
                os.writeBytes(startDate + "\n");
                os.writeBytes(endDate + "\n");
            }

        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

}
