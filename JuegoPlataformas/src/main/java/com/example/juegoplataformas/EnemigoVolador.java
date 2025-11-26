package com.example.juegoplataformas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class EnemigoVolador extends Enemigo {
    private double baseY;
    private Image sprite;

    public EnemigoVolador(double x, double y, double width, double height, double velX) {
        super(x, y, width*3, height*2, velX);
        this.baseY = y;
        try { sprite = new Image("/enemy_fly.png"); } catch (Exception e) { sprite = null; }
    }

    @Override
    public void update() {
        x += velX;
        y = baseY + Math.sin(x * 0.05) * 20;
        if (x < -50) x = 850;
        if (x > 850) x = -50;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (sprite != null) gc.drawImage(sprite, x, y-30, width, height);
        else {
            gc.setFill(Color.ORANGE);
            gc.fillOval(x, y, width, height);
        }
    }
}
