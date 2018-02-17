/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thetempsensserver;

/**
 *
 * @author PetShopBoys
 */
public class serverControl {

    private int serverRunning = 0;

    public void start() {
	if (this.serverRunning == 0) {
	    this.serverRunning = 1;
	System.out.println("Server started.");
	} else {
	    System.out.println("Server already started.");
	}
    }

    public void stop() {
	if (this.serverRunning == 1) {
	    this.serverRunning = 0;
	    DB db = new DB();
	    db.disconnect();
	    System.out.println("Server stopped.");
	} else {
	    System.out.println("Server already stopped.");
	}
    }

    public void restart() {
	stop();
	start();
    }

    public int getStatus() {
	return this.serverRunning;
    }

}
