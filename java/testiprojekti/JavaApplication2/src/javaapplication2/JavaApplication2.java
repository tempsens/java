/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

/**
 *
 * @author juksu
 */
public class JavaApplication2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	int userLevel = 0;
	System.out.println("Tästä se ohjelma alkaa!");
	
	// Het alkuusa myö kutsumma sissää kirjaatumise (usr=test, pwd=passu)
	userControl userControl = new userControl();
	while (userLevel < 1) {
	    userLevel = userControl.login();
	}
	// Ohjelman looppi alkaa tästä
	System.out.println("Ines ollaha!");
    }

}
