/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author juksu
 */
public class JavaApplication2 {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
	int userLevel = 0;
	System.out.println("Tästä se ohjelma alkaa!");
	Scanner scanner = new Scanner(System.in);

	// Het alkuusa myö kutsumma sissää kirjaatumise (usr=test, pwd=passu)
	userControl userControl = new userControl();
	while (userLevel < 1) {
	    userLevel = userControl.login();
	}

	// Ohjelman looppi alkaa tästä
	String komento = "";
	Inputti inputti = new Inputti();

	    
	while (!komento.equals("exit")) {
	    System.out.print("Anna komento: ");
	    komento = scanner.nextLine();
	    switch (komento.toLowerCase()) {
		case "help":
		    if (inputti.Help() == 0) {
			System.out.println("Errori!"); // KORVATAAN LOGIIN KIRJOTUKSELLA
		    }
		    break;

		case "exit":
		    break;

		default:
		    System.out.println("Kirjoita 'help' jos et osaa!");
	    }
		fileout.console(komento);
	}
    }
}
