package es.florida;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Encryptor {

    private final File passwordFile = new File("passwordServer.txt");
    private File blockFile = new File("block.txt");
    private final String PASSWORD = "algo";

    public boolean checkPassword(String password) {

        boolean matches = false;
        StandardPBEStringEncryptor superEncryptor = new StandardPBEStringEncryptor();
        superEncryptor.setPassword(PASSWORD);
        String superDecrypt = superEncryptor.decrypt(password);
        String passwordFile = decryptPasswordFile();

        if (passwordFile.equals(superDecrypt)) {

            matches = true;

        }

        return matches;

    }

    private LinkedList<String> LoadList() throws IOException {

        LinkedList<String> passwordServer = new LinkedList<>();
        FileReader fileReader;
        BufferedReader bufferedReader;
        String line = "";

        if (this.passwordFile.isFile()) {

            fileReader = new FileReader(this.passwordFile);
            bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {

                passwordServer.add(line);

            }

        }

        return passwordServer;
    }


    public void createBlockFile() throws IOException {

        this.blockFile.createNewFile();

    }

    public boolean existBlockFile() {

        boolean exist = false;

        if (this.blockFile.exists()) {

            exist = true;

        }

        return exist;
    }

    public void deleteBlockFile() {

        this.blockFile.delete();

    }

    private String decryptPasswordFile() {

        LinkedList<String> passwordFile = null;
        StandardPBEStringEncryptor superEncryptor = new StandardPBEStringEncryptor();
        superEncryptor.setPassword(PASSWORD);

        try {
            passwordFile = LoadList();
        } catch (IOException e) {
            System.out.println(e);
        }

        String password = passwordFile.getFirst();
        String superDecrypt = superEncryptor.decrypt(password);
        return superDecrypt;

    }

}
