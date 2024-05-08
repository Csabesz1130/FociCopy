package com.example.robotfoci;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a View object which represents the soccer field
        View view = new View();

        // Create a scene with the view and set background color
        Scene scene = new Scene(view, 800, 600, Color.rgb(32, 137, 4));

        // Draw the outer boundaries of the soccer field
        Line topBoundary = new Line(50, 50, 750, 50);
        Line bottomBoundary = new Line(50, 550, 750, 550);
        Line leftBoundary = new Line(50, 50, 50, 550);
        Line rightBoundary = new Line(750, 50, 750, 550);

        // Set the color of boundary lines to white
        topBoundary.setStroke(Color.WHITE);
        bottomBoundary.setStroke(Color.WHITE);
        leftBoundary.setStroke(Color.WHITE);
        rightBoundary.setStroke(Color.WHITE);

        // Draw the center line
        Line centerLine = new Line(400, 50, 400, 550);
        centerLine.setStroke(Color.WHITE);

        // Draw the center circle with a smaller filled circle inside
        Circle centerCircle = new Circle(400, 300, 50);
        centerCircle.setFill(null);
        centerCircle.setStroke(Color.WHITE);

        Circle innerCircle = new Circle(400, 300, 3); // Set radius to 3
        innerCircle.setFill(Color.WHITE);

        // Draw the penalty areas
        Rectangle leftPenaltyArea = new Rectangle(50, 200, 150, 200);
        Rectangle rightPenaltyArea = new Rectangle(600, 200, 150, 200);
        leftPenaltyArea.setFill(null);
        leftPenaltyArea.setStroke(Color.WHITE); // Set color to white
        rightPenaltyArea.setFill(null);
        rightPenaltyArea.setStroke(Color.WHITE); // Set color to white

        // Draw the goal areas
        Rectangle leftGoalArea = new Rectangle(50, 250, 50, 100);
        Rectangle rightGoalArea = new Rectangle(700, 250, 50, 100);
        leftGoalArea.setFill(null);
        leftGoalArea.setStroke(Color.WHITE); // Set color to white
        rightGoalArea.setFill(null);
        rightGoalArea.setStroke(Color.WHITE); // Set color to white

        // Draw corner arches
        Arc topLeftCorner = new Arc(50, 50, 10, 10, 0, -90);
        topLeftCorner.setType(ArcType.OPEN);
        topLeftCorner.setStroke(Color.WHITE);
        topLeftCorner.setFill(null);

        Arc topRightCorner = new Arc(750, 50, 10, 10, 180, 90);
        topRightCorner.setType(ArcType.OPEN);
        topRightCorner.setStroke(Color.WHITE);
        topRightCorner.setFill(null);

        Arc bottomLeftCorner = new Arc(50, 550, 10, 10, 0, 90);
        bottomLeftCorner.setType(ArcType.OPEN);
        bottomLeftCorner.setStroke(Color.WHITE);
        bottomLeftCorner.setFill(null);

        Arc bottomRightCorner = new Arc(750, 550, 10, 10, 90, 90);
        bottomRightCorner.setType(ArcType.OPEN);
        bottomRightCorner.setStroke(Color.WHITE);
        bottomRightCorner.setFill(null);

        // Draw goals on left and right sides
        Rectangle leftGoal = new Rectangle(45, 275, 5, 50); // Adjust size as needed
        leftGoal.setFill(null);
        leftGoal.setStroke(Color.WHITE);

        Rectangle rightGoal = new Rectangle(750, 275, 5, 50); // Adjust size as needed
        rightGoal.setFill(null);
        rightGoal.setStroke(Color.WHITE);

        // Draw circle between leftGoalArea and leftPenaltyArea
        Circle circleBetweenLeftAreas = new Circle(150, 300, 3); // Same height as innerCircle
        circleBetweenLeftAreas.setFill(Color.WHITE);

        // Draw circle between rightGoalArea and rightPenaltyArea
        Circle circleBetweenRightAreas = new Circle(650, 300, 3); // Same height as innerCircle
        circleBetweenRightAreas.setFill(Color.WHITE);

        // Add shapes to the pane
        view.setBackground(Background.EMPTY);
        view.getChildren().addAll(
                topBoundary, bottomBoundary, leftBoundary, rightBoundary,
                centerLine, centerCircle, innerCircle, leftPenaltyArea,
                rightPenaltyArea, leftGoalArea, rightGoalArea,
                topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner,
                leftGoal, rightGoal, circleBetweenLeftAreas, circleBetweenRightAreas);

        // Create a label to display for the teams' fields
        Label label1 = new Label();
        Label label2 = new Label();

        label1.setText("Blue Team's field");
        label1.setFont(new Font("Arial", 14));
        label1.setLayoutX(250); // Set the X position
        label1.setLayoutY(35); // Set the Y position
        label1.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        label2.setText("Red Team's field");
        label2.setFont(new Font("Arial", 14));
        label2.setLayoutX(440); // Set the X position
        label2.setLayoutY(35); // Set the Y position
        label2.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        view.getChildren().add(label1);
        view.getChildren().add(label2);

        // Set the scene to the stage and display it
        primaryStage.setScene(scene);
        primaryStage.setTitle("Soccer Field - Welcome to Soccer App!");
        primaryStage.show();
    }

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Server server = new Server(12345);
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        launch(args);
    }
}
