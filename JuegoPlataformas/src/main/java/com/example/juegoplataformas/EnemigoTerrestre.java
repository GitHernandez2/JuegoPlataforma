package com.example.juegoplataformas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class EnemigoTerrestre extends Enemigo {
    private Plataforma plataforma;     // opcional: si se asigna, patrulla sólo sobre la plataforma
    private double velocidad;
    private boolean moviendoDerecha = true;
    private Image sprite;

    // Límites de patrulla si no hay plataforma
    private double minX = 0;
    private double maxX = 800; // por defecto, ancho de ventana; Game pasa valores reales si lo desea

    // Constructor: conserva la forma que tenías (y la Y/alto/anchura que quieras)
    public EnemigoTerrestre(double x, double y, double width, double height, double velocidad, String spritePath) {
        super(x, y-20, width, height*1.5, velocidad);
        this.velocidad = velocidad;
        try {
            // conservar ruta tal como la tenías; si está en recursos usa "/enemy_ground.jpg"
            sprite = new Image(spritePath);
            System.out.println("Imagen cargada correctamente para el enemigo terrestre.");
        } catch (Exception e) {
            sprite = null;
            System.out.println("Error al cargar la imagen del enemigo terrestre: " + e.getMessage());
        }
    }

    public void setPlataforma(Plataforma plataforma) {
        this.plataforma = plataforma;
        if (plataforma != null) {
            // ajustar límites al ancho de la plataforma (evitar que salga)
            minX = plataforma.getX();
            maxX = plataforma.getX() + plataforma.getWidth() - this.width;
            // si el enemigo estaba fuera, colocarlo sobre la plataforma
            if (x < minX) x = minX;
            if (x > maxX) x = maxX;
            // ajustar y para estar sobre la plataforma (si lo deseas)
            y = plataforma.getY() - this.height;
        }
    }

    // Permite fijar límites manualmente (Game puede usar esto)
    public void setPatrolBounds(double minX, double maxX) {
        this.minX = minX;
        this.maxX = maxX - this.width; // asegurar que no salga
    }

    @Override
    public void update() {
        // Movimiento simple de patrulla entre minX y maxX
        if (moviendoDerecha) {
            x += Math.abs(velocidad);
            if (x >= maxX) {
                x = maxX;
                moviendoDerecha = false;
            }
        } else {
            x -= Math.abs(velocidad);
            if (x <= minX) {
                x = minX;
                moviendoDerecha = true;
            }
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (sprite != null) {
            gc.drawImage(sprite, this.x, this.y, this.width, this.height);
        } else {
            gc.setFill(Color.RED);
            gc.fillRect(this.x, this.y, this.width, this.height);
        }
    }
}
