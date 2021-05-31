package es.florida;

import java.io.*;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Users {

    private File fileUsers = new File("fileUsers.txt");

    public boolean introduceNewUser(String newUser) throws IOException {

        boolean checked = checkUserExistence(newUser);

        if (checked == false) {

            writtingUserData(newUser);
        }

        return checked;

    }

    public boolean deleteUser(String user) throws IOException {

        boolean deleted = checkEmailExistenceForDelete(user);
        LinkedList<String> emailData = loadList();

        if (deleted == true) {

            if (this.fileUsers.exists()) {

                emptyUsersTextFile();

                for (String userEmail : emailData) {

                    StringTokenizer st = new StringTokenizer(userEmail, ";");
                    String name = st.nextToken();
                    String surname = st.nextToken();
                    String email2 = st.nextToken();

                    if (!email2.equals(user)) {

                        String data = name + ";" + surname + ";" + email2;
                        writtingUserData(data);

                    }

                }

            }

        }

        return deleted;
    }


    private LinkedList<String> loadList() throws IOException {

        LinkedList<String> emailData = new LinkedList<>();
        FileReader fileReader;
        BufferedReader bufferedReader;
        String line = "";

        if (fileUsers.isFile()) {

            fileReader = new FileReader(this.fileUsers);
            bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {

                emailData.add(line);

            }

        }

        return emailData;

    }

    private boolean checkUserExistence(String user) throws IOException {

        LinkedList<String> listUsers = loadList();
        String email;
        StringTokenizer st = new StringTokenizer(user, ";");
        st.nextToken();
        st.nextToken();
        email = st.nextToken();
        boolean check = false;

        for (String userEmail : listUsers) {

            StringTokenizer st2 = new StringTokenizer(userEmail, ";");
            st2.nextToken();
            st2.nextToken();
            String email2 = st2.nextToken();

            if (email.equals(email2)) {

                check = true;
            }

        }

        return check;

    }

    private boolean checkEmailExistenceForDelete(String email) throws IOException {

        LinkedList<String> listUser = loadList();

        boolean deleted = false;

        for (String userMail : listUser) {

            StringTokenizer st = new StringTokenizer(userMail, ";");
            st.nextToken();
            st.nextToken();
            String emailList = st.nextToken();

            if (email.equals(emailList)) {

                deleted = true;

            }

        }

        return deleted;

    }

    private void emptyUsersTextFile() throws IOException {

        FileOutputStream writer = new FileOutputStream(this.fileUsers);
        writer.write(("").getBytes());
        writer.close();

    }

    private void writtingUserData(String data) throws IOException {

        FileWriter writer = new FileWriter(this.fileUsers, true);
        PrintWriter print = new PrintWriter(writer);
        print.println(data);
        print.close();

    }

    public LinkedList<String> loadEmailList() throws IOException {

        LinkedList<String> listUsers = loadList();
        LinkedList<String> listEmailUser = new LinkedList<>();

        for(String emailUser : listUsers){

            StringTokenizer st = new StringTokenizer(emailUser, ";");
            st.nextToken();
            st.nextToken();
            String email = st.nextToken();
            listEmailUser.add(email);

        }

        return listEmailUser;

    }

}
