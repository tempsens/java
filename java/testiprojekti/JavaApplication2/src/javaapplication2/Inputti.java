/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

/**
 *
 * @author Joakim
 */
public class Inputti {
/*
    final String txtHelp = ""
            + "HELP - Commands \r\n"
            + "------------------------------- \r\n"
            + "start \t\t Start logging\r\n"
            + "stop \t\t Stop logging\r\n"
            + "restart \t Restart loggin\r\n"
            + "users \t\t Show users\r\n"
            + "temps \t\t List last 50 rows\r\n"
            + "exit \t\t Exit program\r\n";
*/
    public int Help() {
//        return txtHelp;
	System.out.println("\r\n"
            + "HELP - Commands \r\n"
            + "------------------------------- \r\n"
            + "start \t\t Start logging\r\n"
            + "stop \t\t Stop logging\r\n"
            + "restart \t Restart loggin\r\n"
            + "users \t\t Show users\r\n"
            + "temps \t\t List last 50 rows\r\n"
	    + "fileout \t Writes console/userlist to a file\r\n"
	    + "help \t\t Displays this help\r\n"
            + "exit \t\t Exit program\r\n");
	return 1;
    }


    
    
}
