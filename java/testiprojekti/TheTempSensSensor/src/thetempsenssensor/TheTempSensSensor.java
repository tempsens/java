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
    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0L;
        DataInputStream is = null;
        DataOutputStream os = null;
        Socket MyClient = null;

        ShuffleRefucked newShuffeli = new ShuffleRefucked();
        int userLevel = 0;

        try {
            MyClient = new Socket(HOSTNAME, PORT);
            os = new DataOutputStream(MyClient.getOutputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + HOSTNAME);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: hostname");
        }
        LoginSensor login = new LoginSensor();

        while (userLevel < 1) {
            userLevel = Integer.parseInt(login.login(MyClient));
        }

        if (MyClient != null && os != null) {

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
                } catch (IOException e) {
                    System.err.println("Couldn't get I/O for the connection to: " + HOSTNAME);
                }
            }
        }
    }
}
