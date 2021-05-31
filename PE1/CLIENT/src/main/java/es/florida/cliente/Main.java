package es.florida.cliente;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    private static final int SERVER_PORT = 9876;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(HOST, SERVER_PORT));
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter printer = new PrintWriter(new OutputStreamWriter(outputStream), true);
        String command;
        Scanner dataEntry = new Scanner(System.in);
        String line;
        boolean connection = true;

        System.out.println("Welcome user, introduced a command: ");

        while (connection) {

            command = dataEntry.nextLine();

            printer.println(command);

            try {

                while ((line = reader.readLine()) != null) {

                    System.out.println(line);

                    if (line.equals("INTRODUCE A PASSWORD, PLEASE ==>")) {

                        command = dataEntry.nextLine();
                        String password = encryptClientPassword(command);
                        printer.println(password);
                        line = reader.readLine();
                        System.out.println(line);
                        break;

                    }


                    if (line.endsWith(":") || line.endsWith(".")) {

                        break;

                    }

                    if(line.equals("Exit")){

                        socket.close();
                        connection = false;

                    }


                }


            } catch (IOException ioException) {

                System.out.println(ioException);

            }

        }


    }

    public static String encryptClientPassword(String command) {

        StandardPBEStringEncryptor superEncryptor = new StandardPBEStringEncryptor();
        superEncryptor.setPassword("algo");
        String superEncrypt = superEncryptor.encrypt(command);
        return superEncrypt;

    }

}
