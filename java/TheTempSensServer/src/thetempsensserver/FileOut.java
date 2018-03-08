//  Versio 0.2
//  Versionumeroinnin aloitus
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
	    String textToWrite = today + "Server: LOGIN ERROR! (" + viesti + ")\r\n";
	    clientEventLog("Login ERROR | " + viesti, IP);

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
	    String textToWrite = today + "Client event from(" + IP + "): " + viesti + "\r\n";
	    writer.write(textToWrite);
	    writer.close();
	    // System.out.println(textToWrite);     // for DEBUG
	    return 1;
	} catch (IOException ex) {
	    // System.out.println("IOException: " + ex.getMessage());
	    return 0;
	}
    }

    public int loginSuccess(String viesti, String IP) {	   // Prints to file who has logged in
	try (FileWriter writer = new FileWriter("console.log", TRUE)) {
	    String textToWrite = today + "Server: LOGIN: (" + viesti + ")\r\n";
	    clientEventLog("Login successful | " + viesti, IP);

	    writer.write(textToWrite);
	    writer.close();
	    return 1;
	} catch (IOException ex) {
	    System.out.println("IOException: " + ex.getMessage());
	    return 0;
	}
    }

    public void console(String viesti, Socket soketti, String IP) { // OK
	try (FileWriter writer = new FileWriter("console.log", TRUE)) {
	    PrintStream os = new PrintStream(soketti.getOutputStream());
	    String textToWrite = viesti;
	    writer.write(textToWrite + "\n");
	    writer.close();
	    clientEventLog("Console.log written", IP);
	    os.println("console.log written.");

	    // return 1;
	} catch (IOException ex) {
	    System.out.println("IOException: " + ex.getMessage());
	    // return 0;
	}
    }

    public int userlist(Socket soketti, String IP) {// OK
	String fileName = "users.txt";
	try (FileWriter writer = new FileWriter(fileName, TRUE)) {
	    PrintStream os = new PrintStream(soketti.getOutputStream());
	    DB db = new DB();
	    // Ladataan käyttäjät tietokannasta
	    String textToWrite = db.GetUsersFromDB(IP);
	    if (textToWrite == "-1") {
		return -1;
	    } else if (textToWrite == "-2") {
		return -2;
	    }
	    // Tallennetaan käyttäjätiedot tiedostoon
	    writer.write(today + textToWrite + "\n");
	    writer.close();
	    os.println("Users list -file created [" + fileName + "]");
	    return 1;

	} catch (IOException ex) {
	    System.out.println("IOException: " + ex.getMessage());
	    return -2;
	}
    }
}
