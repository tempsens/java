/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author juksu
 */
public class TempSensClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	openSocket toClient = new openSocket();
	toClient.openSocket();
    }
    
}
