//  Versio 0.1      
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

    public int loginError(String viesti) {	    // Prints to file if login fails
        try (FileWriter writer = new FileWriter("console.log", TRUE)) {
            String textToWrite = today + "Server: LOGIN ERROR! (" + viesti + ")\n";
            writer.write(textToWrite);
            writer.close();
            System.out.println("Login incorrect!");
            return 1;
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
            return 0;
        }
    }

    public int loginSuccess(String viesti) {	   // Prints to file who has logged in
        try (FileWriter writer = new FileWriter("console.log", TRUE)) {
            String textToWrite = today + "Server: LOGIN: (" + viesti + ")\n";
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
            String textToWrite = db.GetUsersFromDB();
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
