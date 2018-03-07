//  Versio 0.5  07.03.2018  Joakim
//   list temps komento ja list temps luokka
//  Versio 0.4	06.03.2018  Jukka
//   Pääohjelman komennot switch-case alle. Oli aiemmin if, else if, else
//   Kommentointeja lisää
//   Welcome -teksti lisätty
//   Debug -tulosteet kommentoitu pois
//------------------------------------------------------------------------------
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
    private static final String WELCOME = "Tervetuloa! Aloitappa vaikka komennolla 'help'";
    private static final String VERSION = "0.5";            // Ohjelman versionumero
    private static final String HOSTNAME = "127.0.0.1";     // Palvelimen osoite
    private static final int PORT = 1234;                   // Määritetään käytettävä portti

    public static void main(String[] args) {
        int userID = -1;                                           // Alustetaan userID (Tietokannassa)
        int userLevel = 0;                                         // Alustetaan userLevel
        Socket MyClient = null;                                    // Alustetaan soketti
        DataOutputStream os = null;                                // Alustetaan output stream
        DataInputStream is = null;                                 // Alustetaan input stream
        System.out.println("TempSensClient v" + VERSION);          // Tulostetaan ohjelman versio
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
                        //System.out.println("Login success: " + loginresponssi);	// FOR DEBUG
                        //System.out.println("Vastaus [1]: " + vastaus[1]);	// FOR DEBUG
                        userID = Integer.parseInt(vastaus[1]);
                        userLevel = Integer.parseInt(vastaus[2]);
                        break; // Exit login loop
                    } else {
                        System.out.println("Login fail: " + loginresponssi);	// FOR DEBUG
                    }
                }
                System.out.println("\n" + WELCOME + "\n");  // Print welcome message
                if (loginresponssi.contains("1|")) {
//		    OUTER: // WTF on lämä? Joku label? Ej tyhmä ummärrä :/ -Jukka-
                    while (!komento.equals("exit")) {
                        System.out.print("[" + userLevel + "]: ");
                        komento = scanner.nextLine();
                        // Jos QUIT, niin ensin käsky serverille, sitten client ulos loopista
                        switch (komento) {
// Palvelimen sammutus + client exit
                            case "quit":
                                os.writeBytes(komento + "\n");
                                System.out.println(is.readLine());
//				break OUTER; // Tässä ton OUTER -labelin breikki -Jukka-
                                komento = "exit"; // Lisätty quittaamaan WHILE:stäkin
                                break;       // Lisätty korvaamaan toi OUTER
// Käyttäjän lisääminen
                            case "add user":
                                AddUser addUser = new AddUser();		// Luodaan olio
                                addUser.addNewUser(MyClient);			// Kutsutaan metodia
                                break;
// Lämpötilan lisääminen
                            case "add temp":
                                AddTemp addTemp = new AddTemp();		// Luodaan olio
                                addTemp.addNewTemp(MyClient);			// Kutsutaan metodia
                                break;
// Kirjautuneen käyttäjän salasanan vaihto
                            case "change pass":
                                ChangePassword newpass = new ChangePassword();  // Luodaan olio
                                newpass.change(MyClient, userID);		// Kutsutaan metodia
                                break;
// Lämpötilojen listaaminen
                            case "list temps":
                                ListTemps listTemps = new ListTemps();		// Luodaan olio
                                listTemps.ListTemps(MyClient);			// Kutsutaan metodia
                                break;

// Jos annetaan tuntematon komento
                            default:
                                os.writeBytes(komento + "\n");
                                break;
                        }
// Looppi, jossa tulostetaan rivejä kunnes QQ annetaan ("Multiline loop")
                        while (true) {
                            String responssi = is.readLine();
                            if (responssi.contains("QQ")) {
                                break; // Lopeta loop, kun QQ saadaan
                            }
                            System.out.println(responssi);
                        } // "Multiline loopin" loppusulje
                    } // Pääloopin loppusulje
                } // Login kyselyn loppusulje (IF login=1)
// Quit message
                System.out.println("\nBye. Thanks for choosing this awesome software and "
                        + "we hope you will continue using it and also recommend it "
                        + "for your friends too! Some day we will be BIG and you will be "
                        + "proud about using this software from the very beginning. "
                        + "It may also be nice if you could donate some change for "
                        + "us because Jukka has drinken teams all money... Again...\n");

// clean up:
//System.out.println("Shutting down client..."); // FOR DEBUG
                os.close(); // close the output stream
                is.close(); // close the input stream
                MyClient.close(); // close the socket

            } catch (UnknownHostException e) {
                System.err.println("Trying to connect to unknown host: " + e);
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        } else {
            System.out.println("Socket, is or os is empty! Server <-> client connection error!");
        }
    } // Pääohjelman loppusulje (MAIN)
} // Pääluokan loppusulje
