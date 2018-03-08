//  Versio 0.2
//  Versionumeroinnin aloitus
//------------------------------------------------------------------------------
package thetempsensserver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @author PetShopBoys
 */
public class Inputti {

    public void Help(Socket soketti, int userLevel) {
	PrintStream os = null;
	try {
	    os = new PrintStream(soketti.getOutputStream());
	    os.println("\n"
		    + "HELP - Commands\n"
		    + "-------------------------------\n\n"
		    + "For all users:\n"
		    /*            + "newpass \t\t Change password\r\n"          // <-- to be needed? */
		    + " help \t\t\t\t Displays this help\n"
		    + " list temps\t\t\t Show list of temperatures\n"
                    + " change pass\t\t\t Change password\n"
		    + " exit \t\t\t\t Exit program\n\n");
	    // Jos käyttäjälevel (userLevel suurempi kuin 4 tulostetaan myös lisätoiminnot
	    if (userLevel > 4) {
		os.println("For users with level above 5:\n"
			+ " start \t\t\t\t Start server (temperatures to DB)\n"
			+ " stop \t\t\t\t Stop server (temperatures to DB)\n"
			+ " restart \t\t\t Restart server (temperatures to DB)\n"
			+ " status \t\t\t Show server status\n"
			+ " add user \t\t\t Add's user\n"
			+ " add temp [value] [sensor] \t Add temperature to DB\n"
			+ " list [args]\t\t\t Show list of [users]/[temps]\n"
			+ " fileout users\t\t Write userlist to a file\n"
			+ " quit\t\t\t\t Shutdown server\n");
	    }

	} catch (IOException e) {
	    System.out.println(e);
	}
    }

}
