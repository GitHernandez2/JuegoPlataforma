package com.example.juegoplataformas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class EnemigoPlataforma extends Enemigo {

    private Plataforma plataforma;
    private Image sprite;

    public EnemigoPlataforma(double x, double y, Plataforma plataforma) {
        super(x, y, 50, 50, 2);
        this.plataforma = plataforma;

        try {
            sprite = new Image("enemigo2.png");
        } catch (Exception e) {
            sprite = null;
        }
    }

    @Override
    public void update() {
        x += velX;

        // Rebote dentro de los límites de la plataforma
        if (x <= plataforma.getX() || x + width >= plataforma.getX() + plataforma.getWidth()) {
            velX *= -1;
        }
    }

    public void draw(GraphicsContext gc) {
        if (sprite != null) gc.drawImage(sprite, x, y, width, height);
        else {
            gc.setFill(javafx.scene.paint.Color.ORANGE);
            gc.fillRect(x, y, width, height);
        }
    }
}
