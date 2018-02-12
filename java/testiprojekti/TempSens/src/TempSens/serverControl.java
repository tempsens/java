/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TempSens;

/**
 *
 * @author PetShopBoys
 */
public class serverControl {
    private int serverRunning = 0;
    
    public void start() {
	this.serverRunning = 1;
    }
    
    public void stop() {
	this.serverRunning = 0;
	DB db = new DB();
	db.disconnect();
    }
    
    public void restart() {
	stop();
	start();
    }
    
    public int getStatus() {
	return this.serverRunning;
    }
    
}
