package com.example.robotfoci;

import javafx.scene.paint.Color;


import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private static final ConcurrentHashMap<Integer, PrintWriter> clientOutputs = new ConcurrentHashMap<>();
    private final GameState gameState;

    public ClientHandler(Socket socket, GameState gameState) {
        this.clientSocket = socket;
        this.gameState = gameState;
        // Játékos azonosítás és hozzáadás
        String playerId = "player1";// + clientSocket.getPort(); // Egyedi ID javasolt
        Player player1 = new Player(Color.RED, 100, 100); // Alapértelmezett szín és pozíció
        gameState.addPlayer(playerId, player1);
        // Játékos azonosítás és hozzáadás
        playerId = "player2";// + clientSocket.getPort(); // Egyedi ID javasolt
        Player player2 = new Player(Color.BLUE, 700, 500); // Alapértelmezett szín és pozíció
        gameState.addPlayer(playerId, player2);
    }


    @Override
    public void run() {
        PrintWriter writer = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())))
        {
            writer = clientOutputs.get(clientSocket.getPort());

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from " + clientSocket.getPort() + ": " + inputLine);
                processMessage(inputLine);
            }
        } catch (IOException e) {
            System.err.println("Error handling client #" + clientSocket.getPort() + ": " + e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                clientOutputs.remove(clientSocket.getPort());
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close a socket: " + e.getMessage());
            }
        }
    }

    private void processMessage(String message) {
        // Feldolgozza az uzeneteket, frissiti a jatekallapotot
        updateGameState(message);

        // Állapotfrissítések küldése minden kliensnek
        broadcastGameState();
    }

    private void updateGameState(String message) {
        String[] parts = message.split(" ");
        if ("MOVE".equals(parts[0])) {
            String playerId = parts[1];
            double x = Double.parseDouble(parts[2]);
            double y = Double.parseDouble(parts[3]);
            updatePlayerPosition(playerId, x, y);
        } else {
            System.out.println("Unhandled message: " + message);
        }
    }


private void updatePlayerPosition(String playerId, double x, double y) {
    Player player = gameState.getPlayerById(playerId);
    if (player != null) {
        player.setPosition(x, y);
        System.out.println("Player " + playerId + " moved to " + x + "," + y);
    } else {
        System.out.println("Player not found: " + playerId);
    }
}

    private void broadcastGameState() {
        // A játékállapot valós idejű reprezentációjának összeállítása
        // Példák: "MOVE player1 100 200" vagy "BALL 400 300 0.5 0.3"
        gameState.getPlayers().forEach((playerId, player) -> {
            // Itt iteráljuk végig a játékosokat, és küldjük el azok aktuális pozícióját
            // gameState.getPlayers().forEach((playerId, player) -> {
            String playerState = String.format("MOVE %s %.1f %.1f", playerId, player.getX(), player.getY());

            clientOutputs.forEach((port, writer) -> {
                try (PrintWriter out = writer) { // Use try-with-resources
                    out.println(playerState);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        // Labda állapotának elküldése
        Ball ball = gameState.getBall();
        String ballState = String.format("BALL %.1f %.1f %.1f %.1f", ball.getX(), ball.getY(), ball.getVx(), ball.getVy());
        clientOutputs.forEach((port, writer) -> {
            try (PrintWriter out = writer) { // Use try-with-resources
                out.println(ballState);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
         });
    }
}