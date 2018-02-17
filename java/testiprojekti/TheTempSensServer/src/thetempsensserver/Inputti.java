/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thetempsensserver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @author PetShopBoys
 */
public class Inputti {

    public int Help(Socket soketti) {
//        return txtHelp;
    //    Socket clientSocket = null;

   
    try {

            PrintStream os = new PrintStream(soketti.getOutputStream());
            os.println("\r\n"
                    + "HELP - Commands \r\n" // TODO admin help eller user help riippuen userLevelistä
                    + "------------------------------- \r\n\r\n"
                    + "For all users: \r\n"
                    + "start \t\t\t Start logging\r\n"
                    + "stop \t\t\t Stop logging\r\n"
                    + "restart \t\t Restart logging\r\n"
                    /*            + "newpass \t\t Change password\r\n"          // <-- to be needed? */
                    + "exit \t\t\t Exit program\r\n"
                    + "help \t\t\t Displays this help\r\n\r\n"
                    + "For user with level above 5 \r\n"
                    + "add user \t\t Add's user\r\n"
                    + "add temp [value] \t Add temp value\r\n"
                    + "list [args]\t\t Show list of users/temps\r\n"
                    + "fileout [args]\t\t Writes console/userlist to a file\r\n"
                    +"QQ");

        } catch (IOException e) {
            System.out.println(e);
        }

        return 1;
    }

}
