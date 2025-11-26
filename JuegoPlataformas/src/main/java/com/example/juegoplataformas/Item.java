package com.example.juegoplataformas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Item extends Entidad {
    private boolean recolectado = false;
    private Image sprite;

    // Constructor con imagen
    public Item(double x, double y, double width, double height, String imagePath) {
        super(x, y, width*2, height*2);
        try {
            sprite = new Image("/Item_Humanity.png");  // Cargar la imagen del item
            System.out.println("Imagen del item cargada correctamente: " + sprite.getWidth() + "x" + sprite.getHeight());
        } catch (Exception e) {
            sprite = null;  // En caso de error al cargar la imagen, no mostrar nada
            System.out.println("Error al cargar la imagen del item: " + e.getMessage());
        }
    }

    @Override
    public void update() {
        // Los items no tienen lógica de movimiento, solo se actualiza el estado si están recolectados
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!recolectado) {
            if (sprite != null) {
                gc.drawImage(sprite, x, y, width, height);  // Dibujar el sprite del item
            } else {
                gc.setFill(Color.GOLD);  // Si no se carga el sprite, dibujar un círculo por defecto
                gc.fillOval(x, y, width, height);
            }
        }
    }

    public boolean isRecolectado() {
        return recolectado;
    }

    public void recolectar() {
        recolectado = true;
    }
}
