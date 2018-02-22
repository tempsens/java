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

    public void Help(Socket soketti) {
        try {
            PrintStream os = new PrintStream(soketti.getOutputStream());
            os.println("\n"
                    + "HELP - Commands\n"               // TODO admin help eller user help riippuen userLevelistä
                    + "-------------------------------\n\n"
                    + "For all users:\n"
                    + " start \t\t\t\t Start logging\n"
                    + " stop \t\t\t\t Stop logging\n"
                    + " restart \t\t\t Restart logging\n"
                    /*            + "newpass \t\t Change password\r\n"          // <-- to be needed? */
                    + " exit \t\t\t\t Exit program\n"
                    + " help \t\t\t\t Displays this help\n\n"
                    + "For users with level above 5:\n"
                    + " add user \t\t\t Add's user\n"
                    + " add temp [value] [sensor] \t Add temp value\n"
                    + " list [args]\t\t\t Show list of users/temps\n"
                    + " fileout [args]\t\t\t Writes console/userlist to a file\n");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
