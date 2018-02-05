/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TempSens;

import java.util.Random;

/**
 *
 * @author Joakim
 */
public class Shuffle {

    Random Luku = new Random();

    public Shuffle() {
    }

    public String ShuffleTemp(int month) {
        // Arvotaan luku välillä 0-60
        int luku1;
        int luku2;
        do {
            luku1 = Luku.nextInt(60);
            //  Lisätään arvottu luku lukuun -30, jolloin lämpötilat ovat välillä -30 ja +30
            luku2 = luku1 - 30;
        } while (!IsItBelievable(month, luku2));

        // Palautetaan arvottu luku ja lisätään siihen arpomalla desimaalin kymmenesosa
        return Integer.toString(luku2) + "." + Integer.toString(Luku.nextInt(9));
    }

    private boolean IsItBelievable(int month, int temp) {
        // Tarkastetaan vastaako arvottu lämpötila noin suunnilleen ko. kuukautta

        switch (month) {
            case 12:
            case 1:
            case 2:
                return (temp >= -30 && temp <= 5);

            case 10:
            case 11:
            case 3:
            case 4:
                return (temp >= -10 && temp <= 10);

            case 5:
            case 9:
                return (temp >= -2 && temp <= 20);
            
            case 6:
            case 7:
            case 8:
                return (temp >= 5 && temp <= 30);
        }
        return false;
    }
}
