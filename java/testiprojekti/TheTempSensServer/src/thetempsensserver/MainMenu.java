package thetempsensserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Joakim
 */
public class MainMenu {

    static final String PERUSPALAUTE = "Kirjoita 'help' jos et muuta osaa!";

    void MainMenu() {

    }

    public void SwitchCase(Socket soketti) {
        int userLevel = 10;
        PrintStream os = null;
        DataInputStream is = null;

        // Muuttujat ja luokat pääohjelmalle
        String commandLog = "";
        Inputti inputti = new Inputti();
        FileOut fileout = new FileOut();
        String komento = "";

        String today = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ").format(new Date());
        serverControl srvC = new serverControl();
        try {
            os = new PrintStream(soketti.getOutputStream());
            is = new DataInputStream(soketti.getInputStream());

        } catch (IOException e) {
            System.out.println(e);

        } finally {
            if (os == null) {
                System.exit(0);
            }
        }
        while (true) {
            //       while (!komento.equals("exit")) {
            //           System.out.print("[" + userLevel + "]" + "Anna komento: ");	// Userlevel added to beginning
//            komento = scanner.nextLine();
            try {
                komento = is.readLine();
            } catch (IOException e) {
                System.out.println(e);
            }
            switch (komento.toLowerCase()) {
                case "help": // OK
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    inputti.Help(soketti);
                    break;

                case "add": // OK
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    os.println("Missing parameter! (user or temp needed)");
                    break;

                case "add user":
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    if (userLevel >= 10) {
                        try {
                            String nwUserLevel = is.readLine();
                            int newUserLevel = Integer.parseInt(nwUserLevel);
                            String newUserInput = is.readLine();

                            DB uusiUseri = new DB();
                            uusiUseri.connect();
                            uusiUseri.insertUser(newUserLevel, newUserInput);
                            uusiUseri.disconnect();
                        } catch (IOException e) {
                            System.out.println(e);
                        }

                        //Scanner input = new Scanner(System.in);  // Reading from System.in
                        //  System.out.print("\nGive username for new user: ");
                        // String newUserInput = input.next();

                        //System.out.print("Anna käyttäjä taso: ");
                        //int newUserLevel = Integer.parseInt(input.next());
                        // double UserLevel = Integer.parseInt(input.next());
                        //int newUserLevel = Integer.parseInt(String.format("%d", (int) UserLevel));

                    } else {
                        System.out.println(PERUSPALAUTE);
                    }
                    break;

                case "list": // OK
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    //System.out.println("Missing parameter! (users or temps needed)");
                    os.println("Missing parameter! (users or temps needed)");
                    break;

                case "list users": // OK
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    if (userLevel >= 10) {
                        DB listuser = new DB();
                        listuser.listUsers(soketti);
                    }
                    break;

                case "list temps": // OK
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    DB listtemps = new DB();
                    listtemps.listTemps(soketti);
                    break;

                case "fileout": // OK
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    os.println("Missing parameter! (console or userlist needed)");
                    break;

                case "fileout console": // OK
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    fileout.console(commandLog, soketti);
                    break;

                case "fileout userlist": // OK
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    fileout.userlist(soketti);
                    break;

                case "test":			    // THIS CASE WILL BE REMOVED LATER
                    Shuffle shuffle = new Shuffle();
                    DB db = new DB();
                    String luku;
                    db.connect();

                    for (int laskuri = 1; laskuri <= 10; laskuri++) {
                        luku = shuffle.ShuffleTemp(1);
                        os.println(luku);
                        db.insertTemp(Double.parseDouble(luku), 1, today);

                    }
                    break;

                case "exit": // OK!!
                    os.println("QQ");
                    break;
                case "start":
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    srvC.start(soketti);
                    break;
                case "stop":
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    srvC.stop(soketti);
                    break;
                case "restart":
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    srvC.restart(soketti);
                    break;
                case "status":
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    os.println(srvC.getStatus());
                    break;

                default:
                    commandLog = commandLog + today + "Server: " + komento + "\n";
                    String[] array = komento.split(" ", -1);
                    if (array[0].equals("add") && array[1].equals("temp")) {
                        if (srvC.getStatus() == 1) {
                            if (1 == 1) {	// VAIHDA KUN KEKSIT MITEN TARKASTAA array[2] ja [3]!!!
                                DB db3 = new DB();
                                db3.connect();
                                // System.out.println(array[2]); // Prints temp. Just for debugging
                                db3.insertTemp(Double.parseDouble(array[2]), Integer.parseInt(array[3]), today);
                                db3.disconnect();
                            } else {
                                os.println("Missing parameter! (temperature value and sensor number needed) ");
                            }
                        } else {
                            os.println("NO-GO: Server is offline.");
                        }

                    } else {

                        os.println(PERUSPALAUTE);

                    }
            }	// SWITCHin loppusulje
            os.println("\nQQ");
        }
    }	// Ohjelmaloopin loppusulje 

}
