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
 * @author PetShopBoys
 */
public class Main {

    static final String PERUSPALAUTE = "Kirjoita 'help' jos et muuta osaa!\r\n";

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
	int userLevel = 0;
	System.out.println("Temperature recorder software");
	Scanner scanner = new Scanner(System.in);

	// Het alkuusa myö kutsumma sissää kirjaatumise
	userControl userControl = new userControl();
	while (userLevel < 1) {
	    userLevel = userControl.login();
	}

	// Muuttujat ja luokat pääohjelmalle
	String komento = "";
	String commandLog = "";
	Inputti inputti = new Inputti();
	FileOut fileout = new FileOut();
	String today = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ").format(new Date());
	DB kanta = new DB();
	serverControl srvC = new serverControl();

	// Ohjelman looppi alkaa tästä
	while (!komento.equals("exit")) {
	    System.out.print("[" + userLevel + "]" + "Anna komento: ");	// Userlevel added to beginning
	    komento = scanner.nextLine();
	    switch (komento.toLowerCase()) {
		case "help":
		    commandLog = commandLog + today + "Server: " + komento + "\r\n";
		    if (inputti.Help() == 0) {
			System.out.println("Errori!"); // KORVATAAN LOGIIN KIRJOTUKSELLA
		    }
		    break;
		case "add":
		    commandLog = commandLog + today + "Server: " + komento + "\r\n";
		    System.out.println("Missing parameter! (user or temp needed)");
		    break;

		case "add user":
		    commandLog = commandLog + today + "Server: " + komento + "\r\n";
		    if (userLevel >= 10) {
			Scanner input = new Scanner(System.in);  // Reading from System.in

			System.out.print("\nGive username for new user: ");
			String newUserInput = input.next();

			System.out.print("Anna käyttäjä taso: ");
			//int newUserLevel = Integer.parseInt(input.next());
			double UserLevel = Integer.parseInt(input.next());
			//int newUserLevel = Integer.parseInt(String.format("%d", (int) UserLevel));
			int newUserLevel = (int) UserLevel;
			DB uusiUseri = new DB();
			uusiUseri.connect();
			uusiUseri.insertUser(newUserLevel, newUserInput);
			uusiUseri.disconnect();
		    } else {
			System.out.println(PERUSPALAUTE);
		    }
		    break;

		case "list":
		    commandLog = commandLog + today + "Server: " + komento + "\r\n";
		    System.out.println("Missing parameter! (users or temps needed)");
		    break;

		case "list users":
		    commandLog = commandLog + today + "Server: " + komento + "\r\n";
		    if (userLevel >= 10) {
			DB listuser = new DB();
			listuser.listUsers();
		    }
		    break;

		case "list temps":
		    commandLog = commandLog + today + "Server: " + komento + "\r\n";
		    DB listtemps = new DB();
		    listtemps.listTemps();
		    break;

		case "fileout":
		    commandLog = commandLog + today + "Server: " + komento + "\r\n";
		    System.out.println("Missing parameter! (console or userlist needed)");
		    break;

		case "fileout console":
		    commandLog = commandLog + today + "Server: " + komento + "\r\n";
		    if (fileout.console(commandLog) < 1) {
			System.out.println("Errori!");
		    } else {
			System.out.println("console.log writen.");
		    }
		    break;

		case "fileout userlist":
		    commandLog = commandLog + today + "Server: " + komento + "\r\n";
		    if (fileout.userlist() < 1) {
			System.out.println("Errori!");
		    } else {
			System.out.println("Users list -file created [users.txt]");
		    }
		    break;

		case "test":			    // THIS CASE WILL BE REMOVED LATER
		    Shuffle shuffle = new Shuffle();
		    DB db = new DB();
		    String luku;
		    db.connect();

		    for (int laskuri = 1; laskuri <= 100; laskuri++) {
			luku = shuffle.ShuffleTemp(1);
			System.out.println(luku);
			db.insertTemp(Double.parseDouble(luku), 1);
		    }
		    break;

		case "exit":
		    break;

		case "start":
		    commandLog = commandLog + today + "Server: " + komento + "\r\n";
		    srvC.start();
		    break;
		case "stop":
		    commandLog = commandLog + today + "Server: " + komento + "\r\n";
		    srvC.stop();
		    break;
		case "restart":
		    commandLog = commandLog + today + "Server: " + komento + "\r\n";
		    srvC.restart();
		    break;

		default:
		    commandLog = commandLog + today + "Server: " + komento + "\r\n";
		    String[] array = komento.split(" ", -1);
		    if (array[0].equals("add") && array[1].equals("temp")) {
			if (srvC.getStatus() == 1) {
			    if(1 == 1) {	// VAIHDA KUN KEKSIT MITEN TARKASTAA array[2] ja [3]!!!
			    DB db3 = new DB();
			    db3.connect();
			    // System.out.println(array[2]); // Prints temp. Just for debugging
			    db3.insertTemp(Double.parseDouble(array[2]), Integer.parseInt(array[3]));
			    db3.disconnect();
			    } else {
				System.out.println("Missing parameter! (temperature value and sensor number needed) ");
			    }
			} else {
			    System.out.println("NO-GO: Server is offline.");
			}

		    } else {
			System.out.println(PERUSPALAUTE);
		    }
	    }	// SWITCHin loppusulje
	}	// Ohjelmaloopin loppusulje
    }		// Pääohjelman loppusulje
}		// Pääluokan loppusulje
