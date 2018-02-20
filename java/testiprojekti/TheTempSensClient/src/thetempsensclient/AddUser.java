/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thetempsensclient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Joakim
 */
public class AddUser {

    void addNewUser(Socket soketti) {
        DataOutputStream os = null;
        try {

            os = new DataOutputStream(soketti.getOutputStream());

            Scanner input = new Scanner(System.in);  // Reading from System.in

            System.out.print("\nAnna käyttäjänimi: ");
            String newUserInput = input.next();

            System.out.print("Anna käyttäjätaso: ");
            //int newUserLevel = Integer.parseInt(input.next());
            double UserLevel = Integer.parseInt(input.next());
            //int newUserLevel = Integer.parseInt(String.format("%d", (int) UserLevel));
            int newUserLevel = (int) UserLevel;
     
        
             os.writeBytes("add user" + "\n");
             os.writeBytes(newUserLevel + "\n");
             os.writeBytes(newUserInput + "\n");

        
        
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
            //
        }

// t'ss' l'hetet''n serverille
// add user\
// userlevel
// username
//  DB uusiUseri = new DB();
        //  uusiUseri.connect();
        //  uusiUseri.insertUser(newUserLevel, newUserInput);
        //  uusiUseri.disconnect();
    }

}
