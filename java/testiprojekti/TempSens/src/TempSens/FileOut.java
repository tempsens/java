/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TempSens;

import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Boolean.TRUE;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author PetShopBoys
 */
public class FileOut {

    String today = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ").format(new Date());

    public int loginError(String viesti) {	    // Prints to file if login fails
	try (FileWriter writer = new FileWriter("console.log", TRUE)) {
	    String textToWrite = today + "Server: LOGIN ERROR! (" + viesti + ")\r\n";
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
	    String textToWrite = today + "Server: LOGIN: (" + viesti + ")\r\n";
	    writer.write(textToWrite);
	    writer.close();
	    return 1;
	} catch (IOException ex) {
	    System.out.println("IOException: " + ex.getMessage());
	    return 0;
	}	
    }
    
    public int console(String viesti) {
	try (FileWriter writer = new FileWriter("console.log", TRUE)) {
	    String textToWrite = viesti;
	    writer.write(textToWrite + "\r\n");
	    writer.close();
	    return 1;
	} catch (IOException ex) {
	    System.out.println("IOException: " + ex.getMessage());
	    return 0;
	}
    }

    public int userlist() {			// NEEDED TO CONVERT TO USE DATABASE 
	try (FileWriter writer = new FileWriter("users.txt", TRUE)) {
            DB db = new DB();
            // Ladataan käyttäjät tietokannasta
            String textToWrite = db.GetUsersFromDB();
            // Tallennetaan käyttäjätiedot tiedostoon
            writer.write(today + textToWrite + "\r\n");
	    writer.close();
            return 1;
	} catch (IOException ex) {
	    System.out.println("IOException: " + ex.getMessage());
	    return 0;
	}
    }
   
}
