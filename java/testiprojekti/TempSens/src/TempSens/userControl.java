/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TempSens;

import java.util.Scanner;

/**
 *
 * @author juksu
 */
public class userControl {

    private String username;
    private String password;
    private int userLevel;

// These 3 are temporaty variables - remove after db connection works
    private final String testUser = "test";
    private final String testPass = "passu";
    private final int testLevel = 10;

    public int login() {    //TODO If password is empty then force loop to add password
	Scanner input = new Scanner(System.in);  // Reading from System.in
	FileOut fileout = new FileOut();
	
	System.out.print("\nUsername: ");
	String userInput = input.next();

	System.out.print("Password: ");
	String passInput = input.next();

	// TODO: db connection and query
	DB user = new DB();
        user.connect();
        
        return (user.checkUser(userInput, passInput));
        
/*        if (userInput.equals(testUser) && passInput.equals(testPass)) {
	    this.username = userInput;
    this.password = passInput;
	    this.userLevel = testLevel;
	    return this.userLevel;
	} else {
	    System.out.println("Login incorrect!");
	    this.username = "";
	    this.password = "";
	    this.userLevel = 0;
	    fileout.loginError("u:" + userInput + " p:" + passInput);
	    return 0;
	}
    }
        */
}
}
