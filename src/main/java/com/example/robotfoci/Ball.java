package com.example.robotfoci;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball {
    public final Circle circle;
    private double x;
    private double y;
    private double vx;
    private double vy;

    public Ball(double x, double y, double vx, double vy) {
        this.circle = new Circle(5, Color.BLACK);
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

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public void updateCirclePosition() {
        if (x <= 50 || x >= 750) {
            vx *= -1;
        }
        if (y <= 50 || y >= 550) {
            vy *= -1;
        }
        x += vx;
        y += vy;
        circle.setCenterX(x);
        circle.setCenterY(y);
    }
}
