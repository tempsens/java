/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author juksu
 */
public class TempSensServer {

    public static void main(String[] args) {
	theListener listener = new theListener();
	listener.listenSocket();
    }
}
