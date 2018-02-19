/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thetempsensserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Joakim
 */
public class TheTempSensServer {

    // S E R V E R
    /**
     * @param args the command line arguments
     */
    private static final int PORT = 1234;

    public static void main(String args[]) {
// declaration section:
// declare a server socket and a client socket for the server
// declare an input and an output stream
        String komento = "";

        ServerSocket serveri = null;
        String line;
        DataInputStream is;
        PrintStream os;
        Socket clientSocket = null;
// Try to open a server socket on port 9999
// Note that we can't choose a port less than 1023 if we are not
// privileged users (root)
        try {
            serveri = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println(e);
        }
// Create a socket object from the ServerSocket to listen and accept 
// connections.
// Open input and output streams
        try {
            clientSocket = serveri.accept();
            is = new DataInputStream(clientSocket.getInputStream());
      //      os = new PrintStream(clientSocket.getOutputStream());
// As long as we receive data, echo that data back to the client.
            while (true) {
                line = is.readLine();

         //       os.println(line);
	 System.out.println(line);		// COMMAND ECHO FOR DEBUGGING
                   MainMenu mainMenu = new MainMenu();
                   
                   mainMenu.SwitchCase(line, clientSocket);
                   
                           
                
                //System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
