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
    static final String peruspalaute = "Kirjoita 'help' jos et muuta osaa!\r\n";

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
	    System.out.print("["+userLevel+"]"+"Anna komento: ");	// Userlevel added to beginning
	    komento = scanner.nextLine();
	    DB kanta = new DB();
	    switch (komento.toLowerCase()) {
		case "help":
		    commandLog = commandLog + today + "Server: " + "help\r\n";
		    if (inputti.Help() == 0) {
			System.out.println("Errori!"); // KORVATAAN LOGIIN KIRJOTUKSELLA
		    }
		    break;
                case "add user":
                    commandLog = commandLog + today + "Server: " + "help\r\n";
                    if (userLevel >= 10) {
                        Scanner input = new Scanner(System.in);  // Reading from System.in

                        System.out.print("\nGive username for new user: ");
                        String newUserInput = input.next();

                        System.out.print("Anna käyttäjä taso: ");
                        int newUserLevel = Integer.parseInt(input.next());
                        DB uusiUseri = new DB();
                        uusiUseri.connect();

                        uusiUseri.insertUser(newUserLevel, newUserInput);

                        uusiUseri.disconnect();
                        break;
                        
                    } else {
                        System.out.println(peruspalaute);
                    }
           
                 
                        


// starts a program addUser
                    // && userLever=10 then --> add user [username] [userlevel]
                    // string()=komento.split(" ")
                    // string(2)= username
                    // 
                    // string(3)=userlevel
                    // db.connect()
                    // db.insertuser()
                    
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
		//case "testcon":
		  // kanta.connect();
		  //break;
                case "test":
                    Shuffle shuffle = new Shuffle();
                    DB db = new DB();
                    String luku;
                    db.connect();
                    
                    for (int laskuri = 1; laskuri <= 10; laskuri++) {

                        luku = shuffle.ShuffleTemp(1);
                        System.out.println(luku);
                        
                  //      db.insert("temps", "value", Double.parseDouble(luku));
                        db.insertTemp(Double.parseDouble(luku), 1);

                    }
                    break;
               	case "exit":
		    break;

		default:
		    System.out.println(peruspalaute);
	    }
	}
    }
}
