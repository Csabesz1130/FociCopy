package com.example.robotfoci;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball {
    public final Circle circle;
    private final double x;
    private final double y;
    private  double vx;
    private  double vy;

    public Ball(double x, double y, double vx, double vy) {
        this.circle = new Circle(5, Color.BLACK); // Átmérő és szín
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        updateCirclePosition();
    }
    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

      public void updateCirclePosition() {
        this.circle.setCenterX(x);
        this.circle.setCenterY(y);
    }
}