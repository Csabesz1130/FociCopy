package com.example.robotfoci;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Player {
    private final Circle circle;
    private double x, y; // Pozíciók tárolására

    public Player(Color color, double x, double y) {
        this.circle = new Circle(20); // Átmérő
        this.x = x;
        this.y = y;
        this.circle.setFill(color);
        updateCirclePosition();
    }
    public Circle getCircle() {
        return circle;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        updateCirclePosition();
    }

    private void updateCirclePosition() {
        this.circle.setCenterX(x);
        this.circle.setCenterY(y);
    }
}