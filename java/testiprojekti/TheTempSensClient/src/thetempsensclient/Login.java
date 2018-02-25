/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thetempsensclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Joakim
 */
public class Login {

    private String username;
    private String password;
    private int userLevel;

    public String login(Socket soketti) {
        DataOutputStream os = null;
        DataInputStream is = null;
        String responssi = "";
        try {
            os = new DataOutputStream(soketti.getOutputStream());
            is = new DataInputStream(soketti.getInputStream());

            Scanner input = new Scanner(System.in);  // Reading from System.in
            //FileOut fileout = new FileOut();

            System.out.print("\nUsername: ");
            String userInput = input.next();

            System.out.print("Password: ");
            String passInput = input.next();

            os.writeBytes("login\n");
            os.writeBytes(userInput + "\n");
            os.writeBytes(passInput + "\n");

            responssi = is.readLine();
//            System.out.println(responssi);

            //DB user = new DB();
            //user.connect();
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
            responssi = "0";
            //
        }
        //return (user.checkUser(userInput, passInput));

        return responssi;
    }
}
