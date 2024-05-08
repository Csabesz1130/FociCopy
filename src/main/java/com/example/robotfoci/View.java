package com.example.robotfoci;

import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class View extends Pane {
    private Circle player1Circle;
    private Circle player2Circle;
    private Circle ball;

    public View() {
        initializeUI();
        connectToServer();
    }

    private void initializeUI() {
        player1Circle = new Circle(500,300, 10, Color.RED);
        player2Circle = new Circle(300, 300, 10, Color.BLUE);
        ball = new Circle(400, 300, 5, Color.BLACK);

        getChildren().addAll(player1Circle, player2Circle, ball);

        player1Circle.setOnMouseDragged(this::movePlayer);
        player2Circle.setOnMouseDragged(this::movePlayer);
    }

    private void movePlayer(MouseEvent event) {
        if (event.getSource() instanceof Circle playerCircle) {
            double newX = event.getX();
            double newY = event.getY();
            playerCircle.setCenterX(newX);
            playerCircle.setCenterY(newY);
            String message = String.format("MOVE %s %.1f %.1f", getPlayerId(playerCircle), newX, newY);
            sendMessageToServer(message);
            checkAndKickBall(playerCircle);
        }
    }

    private String getPlayerId(Circle playerCircle) {
        return playerCircle == player1Circle ? "player1" : "player2";
    }

    private void checkAndKickBall(Circle playerCircle) {
        if (playerCircle.contains(ball.getCenterX(), ball.getCenterY())) {
            double forceX = 5 * Math.signum(Math.random() - 0.5);
            double forceY = 5 * Math.signum(Math.random() - 0.5);
            String ballMessage = String.format("KICK %s %.1f %.1f", getPlayerId(playerCircle), forceX, forceY);
            sendMessageToServer(ballMessage);
        }
    }

    private void sendMessageToServer(String message) {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(message);
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }
}
