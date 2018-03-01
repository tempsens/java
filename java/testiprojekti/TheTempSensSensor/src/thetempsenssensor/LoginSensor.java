//  Versio 0.2     
//              
//------------------------------------------------------------------------------
package thetempsenssensor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Joakim
 */
public class LoginSensor {
      private String username;	// Alustetaan käyttäjätunnuksen muuttuja
    private String password;	// Alustetaan salasanan muuttuja
    private String responssi;	// Alustetaan palautteen muuttuja
    

    public String login(Socket soketti) {
	try {
	    DataOutputStream os =   new DataOutputStream(soketti.getOutputStream());	// Output stream
	    DataInputStream is =    new DataInputStream(soketti.getInputStream());	// Input stream
	
            

	    os.writeBytes("login\n");		// Lähetetään palvelimelle: login
	    os.writeBytes("sensoren" + "\n");	// Lähetetään palvelimelle: username
	    os.writeBytes("sensoren" + "\n");	// Lähetetään palvelimelle: password

	    responssi = is.readLine();		// Luetaan palvelimen vastaus

	} catch (IOException e) {
	    System.err.println("IOException:  " + e);
	    responssi = "0";			// Jos epäonnistuu, niin palaute "0"
	}
	return responssi;			// Palautetaan palvelimen vastaus
    }
}
