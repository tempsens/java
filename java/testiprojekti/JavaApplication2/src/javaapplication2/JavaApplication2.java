/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;


import java.util.Scanner;
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
       Scanner scanner = new Scanner(System.in);
       
        
        // Het alkuusa myö kutsumma sissää kirjaatumise (usr=test, pwd=passu)
        userControl userControl = new userControl();
        while (userLevel < 1) {
            userLevel = userControl.login();
        }
        // Ohjelman looppi alkaa tästä
        System.out.println("Ines ollaha!");
        String komento = "";

        while ("exit".equals(komento)) {
            System.out.print("Anna komento (help = apuuwa): ");
            komento = scanner.nextLine();
        
                switch(komento.toLowerCase()){
                
                    case "help":
                        System.out.println(inputti.Help());
                }
        }
    }

}
