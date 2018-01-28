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

    final String txtHelp = ""
            + "HELP - Commands \r\n"
            + "------------------------------- \r\n"
            + "start \t Start logging \r\n"
            + "stop \t Stop logging \r\n"
            + "restart \t Restart loggin \r\n"
            + "users \t Show users"
            + "temps \t List last 50 rows"
            + "exit \t Exit program";

    public String Help() {
        return txtHelp;
    }


    
    
}
