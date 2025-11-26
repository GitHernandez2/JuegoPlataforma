package com.example.juegoplataformas;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Entidad {
    protected double x, y, width, height;

    public Entidad(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update();
    public abstract void draw(GraphicsContext gc);

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    // Métodos getter
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    // Métodos setter para X, Y (agregados)
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
