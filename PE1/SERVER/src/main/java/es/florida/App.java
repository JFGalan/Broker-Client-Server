package es.florida;

public class App {

    public static void main(String[] args)  {

        Server server = new Server();
        Thread thread = new Thread(server);
        thread.start();

    }

}
