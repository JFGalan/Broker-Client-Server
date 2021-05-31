package es.florida;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private final int SERVER_PORT = 9876;
    private final int THREADS = 5;
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
    private ServerSocket server;
    private Worker worker;
    private Socket client;

    {
        try {

            server = new ServerSocket(SERVER_PORT);

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public void run() {

        while (true) {

            try {

                client = server.accept();
                worker = new Worker(client);
                executorService.execute(worker);

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }
}
