/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thetempsensserver;

import java.util.logging.Level;
import java.util.logging.Logger;
import static thetempsensserver.TheTempSensServer.sensorTaulu;

/**
 *
 * @author PetShopBoys
 */
public class sensorControl implements Runnable {
private final int beginValue = 10; // Alkuarvo, josta aloitetaan countdown

    @Override
    public void run() {
	try {
	    this.purgeLoopper();
	} catch (InterruptedException ex) {
	    Logger.getLogger(sensorControl.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void setSensor(int sensorNum) {
	sensorTaulu[sensorNum] = beginValue; // Asetetaan kyseinen sensori alkuarvoon
    }

    public int getSensor(int sensorNum) {
	return sensorTaulu[sensorNum];
    }

    public void listSensors() {
	System.out.println("Lista sensorien tiloista:"
		+ "-----------------------------------");
	for (int i = 0; i < 10; i++) {
	    System.out.print(i + "=" + sensorTaulu[i] + " | ");
	}
	System.out.println();
    }

    private void purgeLoopper() throws InterruptedException {
	while (true) {
	    System.out.println("purgeLooper: Waiting...");			// FOR DEBUG -Jukka-
	    Thread.sleep(1000 * 10); // ms * seconds * mins
//	    System.out.println("purgeLooper: Emptying table...");		// FOR DEBUG -Jukka-
	    this.purgeTable();
	}
    }

    public void purgeTable() {
	System.out.println("purgeTable: Emptying table...");			// FOR DEBUG -Jukka-
	for (int i = 0; i < 10; i++) {
	    if (sensorTaulu[i] > 0) {
		sensorTaulu[i]--;
	    }
	}
	this.listSensors();							// FOR DEBUG -Jukka-
    }

}
