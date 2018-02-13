/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TempSens;

import java.util.Scanner;

/**
 * @author PetShopBoys
 */
public class userControl {

    private String username;
    private String password;
    private int userLevel;

    public int login() {
	Scanner input = new Scanner(System.in);  // Reading from System.in
	FileOut fileout = new FileOut();

	System.out.print("\nUsername: ");
	String userInput = input.next();

	System.out.print("Password: ");
	String passInput = input.next();

	DB user = new DB();
	user.connect();

	return (user.checkUser(userInput, passInput));
    }
}
