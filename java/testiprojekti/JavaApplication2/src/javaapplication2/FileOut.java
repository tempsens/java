/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Boolean.TRUE;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author juksu
 */
public class FileOut {

    String today = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ").format(new Date());

    public int console(String viesti) {
	try (FileWriter writer = new FileWriter("console.log", TRUE)) {
	    String textToWrite = viesti;
	    writer.write(today + "Server: " + textToWrite + "\r\n");
	    writer.close();
	    return 1;
	} catch (IOException ex) {
	    System.out.println("IOException: " + ex.getMessage());
	    return 0;
	}
    }

    public int users() {
	try (FileWriter writer = new FileWriter("users.log", TRUE)) {
	    String textToWrite = "Userlist:\r\n1: test\r\n2: \r\n3: \r\n4: \r\n5: \r\n";
	    writer.write(today + textToWrite + "\r\n");
	    writer.close();
	    return 1;
	} catch (IOException ex) {
	    System.out.println("IOException: " + ex.getMessage());
	    return 0;
	}
    }
}
