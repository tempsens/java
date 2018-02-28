//  Versio 0.2
//              
//------------------------------------------------------------------------------
package thetempsensserver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import static java.lang.Boolean.TRUE;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author PetShopBoys
 */
public class FileOut {

    String today = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ").format(new Date());

    public int loginError(String viesti, String IP) {	    // Prints to file if login fails
        try (FileWriter writer = new FileWriter("console.log", TRUE)) {
            String textToWrite = today + "Server: LOGIN ERROR! (" + viesti + ")\n";
                       clientEventLog("Login ERROR | "+viesti, IP);

            writer.write(textToWrite);
            writer.close();
            System.out.println("Login incorrect!");
            return 1;
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
            return 0;
        }
    }

    public int clientEventLog(String viesti, String IP) {	    // Prints to file if login fails
        try (FileWriter writer = new FileWriter("console.log", TRUE)) {
            String textToWrite = today + "Client event from("+IP+"): " + viesti + "\n";
            writer.write(textToWrite);
            writer.close();
            System.out.println(viesti);     // for DEBUG
            return 1;
        } catch (IOException ex) {
            // System.out.println("IOException: " + ex.getMessage());
            return 0;
        }
    }

    public int loginSuccess(String viesti, String IP) {	   // Prints to file who has logged in
        try (FileWriter writer = new FileWriter("console.log", TRUE)) {
            String textToWrite = today + "Server: LOGIN: (" + viesti + ")\n";
               clientEventLog("Login succes | "+viesti, IP);

            writer.write(textToWrite);
            writer.close();
            return 1;
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
            return 0;
        }
    }

    public void console(String viesti, Socket soketti) { // OK
        try (FileWriter writer = new FileWriter("console.log", TRUE)) {
            PrintStream os = new PrintStream(soketti.getOutputStream());
            String textToWrite = viesti;
            writer.write(textToWrite + "\n");
            writer.close();
            clientEventLog("Console.log written", soketti.getRemoteSocketAddress().toString());
            os.println("console.log written.");

            // return 1;
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
            // return 0;
        }
    }

    public void userlist(Socket soketti) {// OK
        try (FileWriter writer = new FileWriter("users.txt", TRUE)) {
            PrintStream os = new PrintStream(soketti.getOutputStream());
            DB db = new DB();
            // Ladataan käyttäjät tietokannasta
            String textToWrite = db.GetUsersFromDB(soketti.getRemoteSocketAddress().toString());
            // Tallennetaan käyttäjätiedot tiedostoon
            writer.write(today + textToWrite + "\n");
            writer.close();
            os.println("Users list -file created [users.txt]");
            //  return 1;
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
            //    return 0;
        }
    }
}
