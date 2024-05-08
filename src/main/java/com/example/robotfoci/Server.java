package com.example.robotfoci;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ServerSocket serverSocket;

    private final ExecutorService pool = Executors.newCachedThreadPool(); // Using a cached thread pool
    //private ExecutorService pool = Executors.newFixedThreadPool(2);
    private final GameState gameState;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        gameState = new GameState();
    }

    public void start() {
        System.out.println("Server is running...");
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                //ClientHandler clientThread = new ClientHandler(clientSocket, gameState);
                pool.execute(new ClientHandler(clientSocket, gameState));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(12345); // Például a 12345 porton fut
        server.start();
    }
}
