package com.example.robotfoci;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

    //private final DoubleProperty player1X = new SimpleDoubleProperty();
    //private final DoubleProperty player1Y = new SimpleDoubleProperty();
    //private final DoubleProperty player2X = new SimpleDoubleProperty();
    //private final DoubleProperty player2Y = new SimpleDoubleProperty();
    private final DoubleProperty ballX = new SimpleDoubleProperty(100);
    private final DoubleProperty ballY = new SimpleDoubleProperty(100);

    public View() {
        initializeUI();
        connectToServer();
        //startAnimation();
    }

    private void initializeUI() {
        // Itt rajzoljuk meg a játékteret és inicializáljuk a grafikai elemeket
        player1Circle = new Circle(500,300, 10, Color.RED);
        player2Circle = new Circle(300, 300, 10, Color.BLUE);
        ball = new Circle(400, 300, 5, Color.BLACK);

        getChildren().addAll(player1Circle, player2Circle, ball);
        // Esetleges további inicializálások

        // Bind properties to UI elements
       //player1Circle.centerXProperty().bind(player1X);
        //player1Circle.centerYProperty().bind(player1Y);
       // player2Circle.centerXProperty().bind(player2X);
      //  player2Circle.centerYProperty().bind(player2Y);
        ball.centerXProperty().bind(ballX);
        ball.centerYProperty().bind(ballY);


        // Add event handlers for player movement
        player1Circle.setOnMouseDragged(this::movePlayer);
        player2Circle.setOnMouseDragged(this::movePlayer);

    }

    private void movePlayer(MouseEvent event) {
        if (event.getSource() instanceof Circle playerCircle) {
            double newX = event.getX();
            double newY = event.getY();
            playerCircle.setCenterX(newX);
            playerCircle.setCenterY(newY);
            // Construct the message to send to the server
            String message = String.format("MOVE %s %.1f %.1f", getPlayerId(playerCircle), newX, newY);

            // Send the message to the server
            sendMessageToServer(message);
            System.out.println("movePlayer sent a message to server: " + message);

            // If the ball is not moving and the player circle contains the ball, start ball movement
            boolean ballMoving = false;
            if (!ballMoving && playerCircle.contains(ball.getCenterX(), ball.getCenterY())) {
                startBallMovement(playerCircle);
            }
        }
    }

    private String getPlayerId(Circle playerCircle) {
        if (playerCircle == player1Circle) {
            return "player1";
        } else if (playerCircle == player2Circle) {
            return "player2";
        }
        return "";
    }


    private void sendMessageToServer(String message) {
        // Send the message to the server via the output stream
        // You should have access to the PrintWriter object connected to the server
        // You can store this PrintWriter object as a member variable during the connection setup
        // and use it here to send messages
        // For example:
        // out.println(message);
        // out.flush(); // Ensure the message is sent immediately
    }

        private void startBallMovement(Circle player) {
            // Ez tartalmazhatná a ladba mozgatásának logikáját pl. a játékos pozíciója esetén.
        }

    private void connectToServer() {
        try {
            // Kapcsolódás a szerverhez
            Socket socket = new Socket("localhost", 12345);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Külön szál indítása az üzenetek fogadására
            new Thread(() -> {
                try {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        String[] parts = inputLine.split(" ");
                        switch (parts[0]) {
                            case "MOVE" -> {
                                final double x = Double.parseDouble(parts[2]);
                                final double y = Double.parseDouble(parts[3]);
                                updatePlayerPosition(parts[1], x, y);
                            }
                            case "BALL" -> {
                                final double ballX = Double.parseDouble(parts[1]);
                                final double ballY = Double.parseDouble(parts[2]);
                  //              final double Vx = Double.parseDouble(parts[3]);
                  //              final double Vy = Double.parseDouble(parts[4]);
                                updateBallPosition(ballX, ballY);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Error receiving message from server: " + e.getMessage());
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }


    // Szerver üzenetei alapján frissítjük a játék elemek pozícióját
        public void updatePlayerPosition(String playerId, double x, double y) {
        Platform.runLater(() -> {
            if ("player1".equals(playerId)) {
                //player1X.set(x);
                //player1Y.set(y);
                player1Circle.setCenterX(x);
                player1Circle.setCenterY(y);
            } else if ("player2".equals(playerId)) {
             //   player2X.set(x);
             //   player2Y.set(y);
                player2Circle.setCenterX(x);
                player2Circle.setCenterY(y);
            }
        });
    }


public void updateBallPosition(double x, double y) {
        Platform.runLater(() -> {
            //ballX.set(x);
            //ballY.set(y);

           ball.setCenterX(x);
           ball.setCenterY(y);
           getChildren().add(ball);
        });
    }
}
