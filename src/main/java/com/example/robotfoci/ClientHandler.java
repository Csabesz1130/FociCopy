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
        // Adding players to the game state
        String playerId = "player1";
        Player player1 = new Player(Color.RED, 100, 100);
        gameState.addPlayer(playerId, player1);
        playerId = "player2";
        Player player2 = new Player(Color.BLUE, 700, 500);
        gameState.addPlayer(playerId, player2);
    }

    @Override
    public void run() {
        PrintWriter writer = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
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
        String[] parts = message.split(" ");
        switch (parts[0]) {
            case "MOVE":
                String playerId = parts[1];
                double x = Double.parseDouble(parts[2]);
                double y = Double.parseDouble(parts[3]);
                updatePlayerPosition(playerId, x, y);
                break;
            case "KICK":
                playerId = parts[1];
                double forceX = Double.parseDouble(parts[2]);
                double forceY = Double.parseDouble(parts[3]);
                applyKickForce(playerId, forceX, forceY);
                break;
            default:
                System.out.println("Unhandled message: " + message);
                break;
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

    private void applyKickForce(String playerId, double forceX, double forceY) {
        Player player = gameState.getPlayerById(playerId);
        Ball ball = gameState.getBall();
        if (player != null && ball != null) {
            double distance = Math.sqrt(Math.pow(ball.getX() - player.getX(), 2) + Math.pow(ball.getY() - player.getY(), 2));
            if (distance < 30) {
                ball.setVx(ball.getVx() + forceX);
                ball.setVy(ball.getVy() + forceY);
                broadcastGameState();
            }
        }
    }

    private void broadcastGameState() {
        gameState.getPlayers().forEach((playerId, player) -> {
            String playerState = String.format("MOVE %s %.1f %.1f", playerId, player.getX(), player.getY());
            clientOutputs.forEach((port, writer) -> {
                try (PrintWriter out = writer) {
                    out.println(playerState);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        Ball ball = gameState.getBall();
        String ballState = String.format("BALL %.1f %.1f %.1f %.1f", ball.getX(), ball.getY(), ball.getVx(), ball.getVy());
        clientOutputs.forEach((port, writer) -> {
            try (PrintWriter out = writer) {
                out.println(ballState);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
