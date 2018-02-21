/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thetempsenssensor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

/**
 *
 * @author PetShopBoys
 */
public class TheTempSensSensor {

    /**
     * @param args the command line arguments
     */
    private static final int PORT = 1234;
    static long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        long elapsedTime = 0L;
        Socket MyClient = null;
        DataOutputStream os = null;
        ShuffleRefucked newShuffeli = new ShuffleRefucked();
        DataInputStream is = null;
        while (true) {
            try {
                MyClient = new Socket("127.0.0.1", PORT);
                os = new DataOutputStream(MyClient.getOutputStream());
                is = new DataInputStream(MyClient.getInputStream());
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host: hostname");
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to: hostname");
            }

            while (elapsedTime < 2 * 1000) {
                //perform db poll/check
                elapsedTime = (new Date()).getTime() - startTime;
            }
           // System.out.println(newShuffeli.ShuffleTemp(2));
           
            try {
                os.writeBytes("add temp"  + "\n");
                //is.close();
                //os.close();
               // MyClient.close();
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to: åååhostname");
            }
        }
    }
}
