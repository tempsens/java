//  Versio 0.5  06.03.2018  Jukka
//  Versionumeroinnin aloitus
//------------------------------------------------------------------------------
package thetempsensserver;

import java.util.logging.Level;
import java.util.logging.Logger;
import static thetempsensserver.TheTempSensServer.sensorTaulu;

/**
 *
 * @author PetShopBoys
 */
public class sensorControl implements Runnable {
private final int BEGINVALUE = 4; // Alkuarvo, josta aloitetaan countdown
private final int INTERVAL = 1;   // Taulujen countdown taajuus (sekuntia)

    @Override
    public void run() {
	try {
	    this.purgeLoopper();
	} catch (InterruptedException ex) {
	    Logger.getLogger(sensorControl.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void setSensor(int sensorNum) {
	sensorTaulu[sensorNum] = BEGINVALUE; // Asetetaan kyseinen sensori alkuarvoon
    }

    public int getSensor(int sensorNum) {
	return sensorTaulu[sensorNum];
    }

    public void listSensors() {
//	System.out.println("Lista sensorien tiloista:"                          // FOR DEBUG -Jukka-
//		+ "-----------------------------------");                       // FOR DEBUG -Jukka-
	for (int i = 1; i < 11; i++) {
	    System.out.print(i+"="+sensorTaulu[i]+"  ");
	}
	System.out.println();
    }

    private void purgeLoopper() throws InterruptedException {
	while (true) {
//	    System.out.println("purgeLooper: Waiting...");			// FOR DEBUG -Jukka-
	    Thread.sleep(INTERVAL * 1000); // ms * seconds * mins
//	    System.out.println("purgeLooper: Emptying table...");		// FOR DEBUG -Jukka-
	    this.purgeTable();
	}
    }

    public void purgeTable() {
//	System.out.println("purgeTable: Emptying table...");			// FOR DEBUG -Jukka-
	for (int i = 1; i < 11; i++) {
	    if (sensorTaulu[i] > 0) {
		sensorTaulu[i]--;
	    }
	}
	this.listSensors();							// FOR DEBUG -Jukka-
    }

}
