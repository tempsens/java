/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TempSens;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author juksu
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
	int userLevel = 0;
	String commandLog = "";
	System.out.println("Temperature recorder software");
	Scanner scanner = new Scanner(System.in);

	// Het alkuusa myö kutsumma sissää kirjaatumise (usr=test, pwd=passu)
	userControl userControl = new userControl();
	while (userLevel < 1) {
	    userLevel = userControl.login();
	}

	// Muuttujat ja luokat pääohjelmalle
	String komento = "";
	Inputti inputti = new Inputti();
	FileOut fileout = new FileOut();
	String today = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ").format(new Date());
   
	// Ohjelman looppi alkaa tästä
	while (!komento.equals("exit")) {
	    System.out.print("Anna komento: ");
	    komento = scanner.nextLine();
	    DB kanta = new DB();
	    switch (komento.toLowerCase()) {
		case "help":
		    commandLog = commandLog + today + "Server: " + "help\r\n";
		    if (inputti.Help() == 0) {
			System.out.println("Errori!"); // KORVATAAN LOGIIN KIRJOTUKSELLA
		    }
		    break;

		case "fileout":
		    commandLog = commandLog + today + "Server: " + "fileout\r\n";
		    System.out.println("Missing parameter! (console or users needed)");
		    break;

		case "fileout console":
		    commandLog = commandLog + today + "Server: " + "fileout console\r\n";
		    if(fileout.console(commandLog) < 1) {
			System.out.println("Errori!"); // KORVATAAN LOGIIN KIRJOTUKSELLA			
		    } else {
			System.out.println("console.log writen.");
		    }
		    break;

		case "fileout users":
		    commandLog = commandLog + today + "Server: " + "fileout userlist\r\n";
		    if(fileout.userlist() < 1) {
			System.out.println("Errori!"); // KORVATAAN LOGIIN KIRJOTUKSELLA			
		    } else {
			System.out.println("users.log writen.");
		    }
		    break;
		case "testcon":
		    kanta.connect();
		    break;

		case "exit":
		    break;

		default:
		    System.out.println("Kirjoita 'help' jos et osaa!");
	    }
	}
    }
}
