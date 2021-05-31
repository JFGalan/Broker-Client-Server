package es.florida;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Worker implements Runnable {

    private final Socket client;
    private boolean menu = true;
    private static final int THREADS = 2;
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
    private final int PORT = 1025;
    private File logFile = new File("logFile.txt");
    private final String HOST_NAME = "localhost";
    private final String EMAIL_BROKER = "Broker@broker.es";

    public Worker(Socket client) {
        this.client = client;
    }

    public void run() {

        try {

            String brokerConnected = "\n" + Thread.currentThread().getName() + " Client connected on port " + client.getPort() + "\n";
            System.out.println(brokerConnected);
            writtingUserData(brokerConnected);

            while (this.menu) {

                BufferedReader reader = buildReader(client);
                PrintWriter writer = buildWriter(client);
                SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                selectedOptions(reader, date, writer);

            }

        } catch (IOException e) {

            System.out.println(e);

        }

    }

    private PrintWriter buildWriter(Socket client) throws IOException {

        OutputStream outputStream = client.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        PrintWriter printer = new PrintWriter(outputStreamWriter, true);
        return printer;

    }

    private BufferedReader buildReader(Socket client) throws IOException {

        InputStream inputStream = client.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        return reader;

    }

    private void logOff(Socket client) {

        try {

            client.close();

        } catch (IOException e) {

            System.out.println(e);

        }

    }

    private void showMenu(PrintWriter writer) {

        writer.println("AVAILABLE OPERATIONS");
        writer.println("1. CREATE USER");
        writer.println("2. DELETE USER");
        writer.println("3. NOTIFY EXECUTION (BUY/SELL) ==> EXAMPLE: INTRODUCE ONLY BUY-BTC OR SELL-BTC FOR NOTIFY");
        writer.println("4. LOCK SERVER");
        writer.println("5. UNLOCK SERVER");
        writer.println("6. LOG OFF.");

    }

    private void selectedOptions(BufferedReader reader, SimpleDateFormat date, PrintWriter writer) throws IOException {

        this.menu = false;
        Users users = new Users();
        String order = reader.readLine();
        Notify notify;
        LinkedList<String> listUsers;
        Encryptor password = new Encryptor();
        String log;

        if (!password.existBlockFile()) {

            if (order.equals("1")) {

                writer.println("ADD USER");
                writer.println(" Name:");
                String name = reader.readLine();
                writer.println("Surname:");
                String surname = reader.readLine();
                writer.println("Email:");
                String email = reader.readLine();
                String user = name + ";" + surname + ";" + email;
                boolean added = users.introduceNewUser(user);

                if (!added) {

                    writer.println("USER ADDED " + email + ".");
                    log = "\n" + date.format(new Date()) + " User added: " + email;
                    System.out.println(log);
                    writtingUserData(log);

                } else {

                    writer.println("USER COULDN'T BE ADDED BECAUSE HIS EMAIL ADDRESS EXISTS IN OUR DATABASE.");
                    log = "\n" + date.format(new Date()) + " User not added because his email exists in our database: " + email;
                    System.out.println(log);
                    writtingUserData(log);

                }

            } else if (order.equals("2")) {

                writer.println(" DELETE USER");
                writer.println(" Introduce an email for delete a user:");
                String deleteUserByEmail = reader.readLine();
                Users deletedUser = new Users();
                boolean deleted = deletedUser.deleteUser(deleteUserByEmail);

                if (deleted) {

                    writer.println("\n USER DELETED " + deleteUserByEmail + ".");
                    log = "\n" + date.format(new Date()) + " User deleted: " + deleteUserByEmail;
                    System.out.println(log);
                    writtingUserData(log);

                } else {

                    writer.println("USER NOT DELETED BECAUSE DOESN'T EXIST IN OUR DATABASE.");
                    log = "\n" + date.format(new Date()) + " User not deleted because this user not exist in our database " + deleteUserByEmail;
                    System.out.println(log);
                    writtingUserData(log);

                }


            } else if (order.toUpperCase().contains("BUY") || order.toUpperCase().contains("SELL")) {

                String buyOrSell = order.toUpperCase();

                if (buyOrSell.equals("BUY-") || buyOrSell.equals("BUY") || buyOrSell.equals("SELL-") || buyOrSell.equals("SELL")) {

                    writer.println("IT'S NOT POSSIBLE TO SEND MAIL, BECAUSE YOU HAVEN'T ENTERED WHICH CURRENCY TO BUY OR SELL");
                    writer.println("\nEXAMPLE ==> INTRODUCE ONLY BUY-BTC OR SELL-BTC FOR NOTIFY.");
                    log = "\n" + date.format(new Date()) + "- INCORRECT DATA ENTRY FOR SEND EMAILS.";
                    System.out.println(log);
                    writtingUserData(log);

                } else {

                    listUsers = users.loadEmailList();

                    if (listUsers.size() > 0) {

                        for (String emailUser : listUsers) {

                            notify = new Notify(emailUser, buyOrSell, buyOrSell, HOST_NAME, EMAIL_BROKER, this.PORT);
                            executorService.execute(notify);
                            writer.println("\n EMAIL SENDED TO " + emailUser);

                        }

                        writer.println(".");

                        log = "\n" + date.format(new Date()) + " - PURCHASE ORDER: " + buyOrSell;
                        System.out.println(log);
                        writtingUserData(log);

                    } else {

                        writer.println("THERE ARE NO USERS IN THE DATABASE, SO NO MAIL CAN BE SENT.");
                        log = "\n" + date.format(new Date()) + "- THERE AREN'T USERS IN OUR DATABASE AND THEREFORE WE CANNOT SEND MAILS.";
                        System.out.println(log);
                        writtingUserData(log);

                    }


                }

            } else if (order.equals("4")) {

                writer.println("INTRODUCE A PASSWORD, PLEASE ==>");
                String passw = reader.readLine();

                if (password.checkPassword(passw)) {

                    password.createBlockFile();
                    writer.println("SERVER IS LOCKED.");
                    log = "\n" + date.format(new Date()) + "- The client has locked the server";
                    System.out.println(log);
                    writtingUserData(log);

                } else {

                    writer.println("THE PASSWORD ENTERED ISN'T CORRECT.");
                    log = "\n" + date.format(new Date()) + "- Server is unlocked because the password is wrong";
                    System.out.println(log);
                    writtingUserData(log);

                }

            } else if (order.equals("5")) {

                writer.println("THE SERVER ISN'T LOCKED AND THEREFORE SOMETHING THAT ISN'T LOCKED CANNOT BE UNLOCKED.");
                log = "\n" + date.format(new Date()) + "- The server isn't locked and therefore something that isn't locked cannot be unlocked. ";
                System.out.println(log);
                writtingUserData(log);

            } else if (order.equals("6")) {

                writer.println("Exit");
                log = "\n" + date.format(new Date()) + "- The client has logged off";
                System.out.println(log);
                writtingUserData(log);
                logOff(this.client);

            } else if (order.equals("HELP") || order.equals("help")) {

                this.showMenu(writer);

            } else {

                writer.println("THE INTRODUCED COMMAND IS NOT CORRECT.");
                log = "\n" + date.format(new Date()) + "- The client has introduced a wrong command";
                System.out.println(log);
                writtingUserData(log);

            }

        } else if (order.equals("5")) {

            writer.println("INTRODUCE A PASSWORD, PLEASE ==>");
            String passw = reader.readLine();
            boolean matches = password.checkPassword(passw);

            if (matches) {

                password.deleteBlockFile();
                writer.println("SERVER IS UNLOCK.");
                log = "\n" + date.format(new Date()) + "- The client has unlocked the server.";
                System.out.println(log);
                writtingUserData(log);

            } else {

                writer.println("THE PASSWORD ENTERED IS NOT CORRECT. ");
                log = "\n" + date.format(new Date()) + "- The client has tried to unlock the server, but the password entered isn't correct";
                System.out.println(log);
                writtingUserData(log);

            }

        } else if (order.equals("6")) {

            writer.println("Exit");
            log = "\n" + date.format(new Date()) + "- The client has logged off";
            System.out.println(log);
            writtingUserData(log);
            logOff(this.client);

        } else if (order.equals("HELP") || order.equals("help")) {

            this.showMenu(writer);

        } else {

            if (order.equals("1") || order.equals("2") || order.contains("SELL") || order.contains("BUY") || order.equals("4")) {

                writer.println("THE SERVER IS BLOCKED AND AS SUCH YOU CANNOT CREATE USERS, DELETE USERS AND SEND MAIL.");
                log = "\n" + date.format(new Date()) + "- Server is locked and create user, delete user and notify execution commands aren't available.";
                System.out.println(log);
                writtingUserData(log);

            } else {

                writer.println("THE INTRODUCED COMMAND IS NOT CORRECT.");
                log = "\n" + date.format(new Date()) + "- The client has introduced a wrong command";
                System.out.println(log);
                writtingUserData(log);

            }


        }

        this.menu = true;

    }


    private void writtingUserData(String data) throws IOException {

        FileWriter writer = new FileWriter(this.logFile, true);
        PrintWriter print = new PrintWriter(writer);
        print.println(data);
        print.close();

    }

}
