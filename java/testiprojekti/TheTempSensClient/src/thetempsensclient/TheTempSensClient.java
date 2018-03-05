//  Versio 0.3	05.03.2018  Jukka
//   AddTemp luokan luonti
//------------------------------------------------------------------------------
//  Versio 0.2	28.02.2018  Jukka
//   Static VERSION -muuttuja ja versionumeron tulostus ohjelman alkuun
//   server QUIT -käskyn ohjaus: server quit, client exit
//   catch kuvaustekstejä debuggia helpottamaan
//------------------------------------------------------------------------------
//  Versio 0.1     
//              
//------------------------------------------------------------------------------
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
    private static final String VERSION = "0.2";
    private static final String HOSTNAME = "127.0.0.1"; // Palvelimen osoite
    private static final int PORT = 1234; // Määritetään käytettävä portti

    public static void main(String[] args) {
        int userID = -1;
        Socket MyClient = null;      // Alustetaan soketti
        DataOutputStream os = null;  // Alustetaan output stream
        DataInputStream is = null;   // Alustetaan input stream
        System.out.println("TempSensClient, v" + VERSION);
        try {
            MyClient = new Socket(HOSTNAME, PORT);		   // Määritetään soketti
            os = new DataOutputStream(MyClient.getOutputStream()); // Määritetään output stream
            is = new DataInputStream(MyClient.getInputStream());   // Määritetään input stream
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + HOSTNAME);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + HOSTNAME);
        }
        if (MyClient != null && os != null && is != null) {
            try {
                Scanner scanner = new Scanner(System.in);
                String komento = "";
                String loginresponssi = "0";

                // Kirjautuminen, tarkistetaan käyttöoikeudet
                Login login = new Login();
                while (true) { // Login loop
                    loginresponssi = login.login(MyClient);
                    if (loginresponssi.contains("1|")) {
                        String[] vastaus = loginresponssi.split("\\|");
                        System.out.println(loginresponssi);
                        System.out.println("Vastaus [1]: " + vastaus[1]);

                        userID = Integer.parseInt(vastaus[1]);
                        break;

                    } else {
                        System.out.println(loginresponssi);
                    }
                }
                if (loginresponssi.contains("1|")) {
                    while (!komento.equals("exit")) {
                        System.out.print(": ");
                        komento = scanner.nextLine();
                        // Jos QUIT, niin ensin käsky serverille, sitten client ulos loopista
                        if (komento.equals("quit")) {
                            os.writeBytes(komento + "\n");
                            System.out.println(is.readLine());
                            break;
                        }

                        // Jos käyttäjän lisääminen
                        if (komento.equals("add user")) {
                            AddUser addUser = new AddUser();		    // Luodaan olio
                            addUser.addNewUser(MyClient);		    // Kutsutaan metodia
                        } else if (komento.equals("add temp")) {
			    AddTemp addTemp = new AddTemp();		    // Luodaan olio
			    addTemp.addNewTemp(MyClient);		    // Kutsutaan metodia
			} else if (komento.equals("change pass")) {
                            ChangePassword newpass = new ChangePassword();  // Luodaan olio
                            newpass.change(MyClient, userID);		    // Kutsutaan metodia

                        } else {
                            os.writeBytes(komento + "\n");
                        }
                        while (true) {
                            String responssi = is.readLine();
                            if (responssi.contains("QQ")) {
                                break;
                            }
                            System.out.println(responssi);

                        } // Sisempi While loppusulje (MULTILINE)
                    } // Ulompi While loppusulje (EXIT PROGRAM)
                } // Login kyselyn loppusulje (IF login=1)

                System.out.println("\nBye. Thanks for choosing this awesome software and "
                        + "we hope you will continue using it and also recommend it "
                        + "for your friends too! Some day we will be BIG and you will be "
                        + "proud about using this software from the very beginning. "
                        + "It may also be nice if you could donate some change for "
                        + "us because Jukka has drinken teams all money... Again...\n");

// clean up:		
                os.close(); // close the output stream
                is.close(); // close the input stream
                MyClient.close(); // close the socket

            } catch (UnknownHostException e) {
                System.err.println("Trying to connect to unknown host: " + e);
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        } else {
            System.out.println("Socket, is or os is empty! Connection error!");
        }
    } // Pääohjelman loppusulje (MAIN)
} // Pääluokan loppusulje
