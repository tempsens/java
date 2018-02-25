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

                Scanner scanner = new Scanner(System.in);
                String komento = "";
                String responssi = "0";

                // Kirjautuminen, tarkistetaan käyttöoikeudet
                Login login = new Login();
                while (true) {
                    responssi = login.login(MyClient);
                    if (responssi.equals("1")) {
                        break;
                    } else {
                        System.out.println(responssi);
                    }

                }

                if (responssi.equals("1")) {

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
                            os.writeBytes(komento + "\n\n");
                        }
                        while (!responssi.contains("QQ")) {
                            responssi = is.readLine();
                            //System.out.println("Jumi 1");
                            //System.out.println("responssi:" + responssi);

                        /*    if (responssi.contains("QQ")) {
                                System.out.println("Jumi 2");
                                System.out.println("responssi 2:" + responssi);

                                break;
                            }*/

                            System.out.println(responssi);

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
