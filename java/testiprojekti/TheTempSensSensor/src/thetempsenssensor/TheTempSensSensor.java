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

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0L;
        Socket MyClient = null;
        DataOutputStream os = null;
        ShuffleRefucked newShuffeli = new ShuffleRefucked();
        DataInputStream is = null;
        try {
            MyClient = new Socket("127.0.0.1", PORT);
            os = new DataOutputStream(MyClient.getOutputStream());
      //      is = new DataInputStream(MyClient.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: hostname");
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: hostname");
        }
        // while (true) {

        if (MyClient != null && os != null && is != null) {

            while (true) {
                try {
                    while (elapsedTime < 4 * 1000) {
                        //perform db poll/check
                        elapsedTime = (new Date()).getTime() - startTime;
                    }
                    System.out.println("Ulkona Silmukassa");
                    startTime = System.currentTimeMillis();
                    elapsedTime = 0L;

                    os.writeBytes("add temp" + "\n");
                    //arvottu lämpötila
                    os.writeBytes(newShuffeli.ShuffleTemp(2) + "\n");
                    // sensorin numero
                    os.writeBytes("1\n");
                    System.out.println("Sinne meni\n");

//    System.out.println(is.readLine());
                    //is.close();
                    //os.close();
                    // MyClient.close();
                } catch (IOException e) {
                    System.err.println("Couldn't get I/O for the connection to: åååhostname");
                }
            }

            // System.out.println(newShuffeli.ShuffleTemp(2));
        }

    }
}
