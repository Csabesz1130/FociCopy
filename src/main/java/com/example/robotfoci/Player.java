package com.example.robotfoci;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Player {
    private final Circle circle;
    private double x, y;

    public Player(Color color, double x, double y) {
        this.circle = new Circle(20);
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

    public void setX(double x) {
        this.x = x;
        updateCirclePosition();
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        updateCirclePosition();
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        updateCirclePosition();
    }

    public void kickBall(Ball ball, double forceX, double forceY) {
        double distance = Math.sqrt(Math.pow(ball.getX() - this.x, 2) + Math.pow(ball.getY() - this.y, 2));
        if (distance < 30) {
            ball.setVx(ball.getVx() + forceX);
            ball.setVy(ball.getVy() + forceY);
        }
    }

    private void updateCirclePosition() {
        this.circle.setCenterX(x);
        this.circle.setCenterY(y);
    }
}
