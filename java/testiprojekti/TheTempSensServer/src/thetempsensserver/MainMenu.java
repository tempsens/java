package thetempsensserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Joakim
 */
public class MainMenu implements Runnable {

    private static final String PERUSPALAUTE = "Kirjoita 'help' jos et muuta osaa!";
    private static final int PORT = 1234; // Portin määritys
    ServerSocket servu = null;
    Socket clinu = null;
    PrintStream os = null;
    DataInputStream is = null;

    MainMenu(Socket clinu) { // Konstruktori, jossa määritetään I/O -toiminnot
        try {
            this.clinu = clinu;
            this.is = new DataInputStream(clinu.getInputStream());
            this.os = new PrintStream(clinu.getOutputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        int userLevel = 10;

        // Muuttujat ja luokat pääohjelmalle
        String commandLog = ""; // Aloitetaan tyhjällä komentologilla
        String komento;
        String today = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ").format(new Date());

        Inputti inputti = new Inputti(); // Tarvitaan help -tekstin tulostukseen
        FileOut fileout = new FileOut(); // Tarvitaan tiedosto outputiin
        serverControl srvC = new serverControl(); // Start/Stop/Restart
        int ulosta = 0; // Muuttuja while -loopista poistumiseksi

        while (ulosta < 1) { // Pääloop komentojen kuunteluun
            try {
                komento = is.readLine();    // Luetaan yksi rivi muuttujaan
            System.out.println(komento);

            } catch (IOException e) {
                System.out.println(e);
                break;		    // Poistutaan loopista jos luku epäonnistuu
            }
            switch (komento.toLowerCase()) {
                case "help": // Tulostaa helpin
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    inputti.Help(clinu); // Tulostaa helpin
                    break;

                case "add": // Väärä syntaksi -> antaa vain virheilmoituksen
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    os.println("Missing parameter! (user or temp needed)");
                    break;
                case "login":

                    try {
                        String userName = is.readLine();
                        String userPass = is.readLine();

                        DB user = new DB();
                        user.connect();
                        int vastaus = user.checkUser(userName, userPass);
                        
                        if (vastaus>0){
                        os.println("1");
                        } else {
                        os.println("Login incorrect");
                        }
                        
                        
                    } catch (IOException e) {
                        System.out.println(e);
                        break;		    // Poistutaan loopista jos luku epäonnistuu
                    }

                    break;
                case "add user": // Lisätään käyttäjä
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    if (userLevel >= 10) {
                        try { // Luetaan kaksi riviä lisää
                            int newUserLevel = Integer.parseInt(is.readLine());
                            String newUserInput = is.readLine();

                            DB uusiUseri = new DB();
                            uusiUseri.connect();
                            uusiUseri.insertUser(newUserLevel, newUserInput);
                            uusiUseri.disconnect();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                    } else {
                        System.out.println(PERUSPALAUTE);
                    }
                    break;

                case "list": // Väärä syntaksi -> antaa vain virheilmoituksen
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    os.println("Missing parameter! (users or temps needed)");
                    break;

                case "list users": // Tulostaa listan käyttäjistä
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    if (userLevel >= 10) {
                        DB listuser = new DB();
                        listuser.listUsers(clinu);
                    }
                    break;

                case "list temps": // Tulostaa listan lämpötiloista (100 viimeisintä)
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    DB listtemps = new DB();
                    listtemps.listTemps(clinu);
                    break;

                case "fileout": // Väärä syntaksi -> antaa vain virheilmoituksen
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    os.println("Missing parameter! (console or userlist needed)");
                    break;

                case "fileout console": // Tulostetaan komentologi
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    fileout.console(commandLog, clinu);
                    break;

                case "fileout userlist": // Tulostetaan lista käyttäjistä
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    fileout.userlist(clinu);
                    break;
                case "add temp": // Lämpötilan lisääminen
                    try {
                        String tempvalue = is.readLine();
                        String sensori = is.readLine();

                        DB db = new DB();
                        db.connect();
                        System.out.println(tempvalue);
                        System.out.println(sensori);
                        db.insertTemp(Double.parseDouble(tempvalue), Integer.parseInt(sensori), today);
                        db.disconnect();

                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    break;
                case "quit": // OK!!
                    os.println("QQ"); // Lähetetään QQ lähetyksen lopuksi
                    ulosta = 1;	    // Poistuu while -loopista
                    break;
                case "start":	// Käynnistää tietojen välityksen
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    srvC.start(clinu);
                    break;
                case "stop":	// Pysäyttää tietojen välityksen
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    srvC.stop(clinu);
                    break;
                case "restart":	// Käynnistää tietojen välityksen uudelleen
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    srvC.restart(clinu);
                    break;
                case "status":	// Tulostaa tietojen välityksen tilan
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    os.println(srvC.getStatus());
                    break;

                default:
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    os.println(PERUSPALAUTE);
            }   // SWITCHin loppusulje
       System.out.println("Jumi 666");

        os.println("\nQQ");  
        }   // Ohjelmaloopin loppusulje (while)
     
    
    }	// Metodin loppusulje (void run)
} // Luokan loppusulje (MainMenu)
