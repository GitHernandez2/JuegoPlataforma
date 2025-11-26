package com.example.juegoplataformas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Jugador extends Entidad {
    private double velY = 0;
    private boolean enSuelo = false;
    private int puntaje = 0;
    private boolean vivo = true;
    private Image sprite;

    private double vida = 100;  // 100% de vida por defecto

    // invulnerabilidad
    private long ultimoDaño = 0;
    private long tiempoInvulnerable = 500_000_000; // 0.5s en ns

    // knockback / empujón
    private double knockbackX = 0;
    private double knockbackDecay = 0.85;

    public Jugador(double x, double y, double width, double height) {
        super(x, y, width, height);
        try {
            sprite = new Image("player.png"); // mantengo la ruta original
            System.out.println("Imagen cargada correctamente: " + sprite.getWidth() + "x" + sprite.getHeight());
        } catch (Exception e) {
            sprite = null;
            System.out.println("Error al cargar la imagen: " + e.getMessage());
        }
    }

    public void moverIzquierda() {
        x -= 5;
    }

    public void moverDerecha() {
        x += 5;
    }

    public void saltar() {
        if (enSuelo) {
            velY = -10;
            enSuelo = false;
        }
    }

    public void applyGravity() {
        velY += 0.5;
        y += velY;
    }

    public void landOn(Plataforma p) {
        y = p.getY() - height;
        velY = 0;
        enSuelo = true;
    }

    @Override
    public void update() {
        // Aplicar knockback si existe
        if (Math.abs(knockbackX) > 0.5) {
            x += knockbackX;
            knockbackX *= knockbackDecay;
            if (Math.abs(knockbackX) < 0.5) knockbackX = 0;
        }

        // límites simples de ventana (asume 800x600 por defecto, Game puede ajustar si quieres)
        if (x < 0) x = 0;
        if (x + width > 800) x = 800 - width;
        if (y < 0) y = 0;
        if (y + height > 600) {
            y = 600 - height;
            velY = 0;
            enSuelo = true;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (sprite != null) {
            gc.drawImage(sprite, x, y, width, height);
        } else {
            gc.setFill(Color.BLUE);
            gc.fillRect(x, y, width, height);
        }
    }

    public int getPuntaje() { return puntaje; }
    public void setPuntaje(int puntaje) { this.puntaje = puntaje; }
    public void addPuntaje(int v) { this.puntaje += v; }

    public void setEnSuelo(boolean v) { this.enSuelo = v; }
    public boolean isEnSuelo() { return enSuelo; }

    public void setVivo(boolean v) { this.vivo = v; }
    public boolean isVivo() { return vivo; }

    public void setVelY(double v) { this.velY = v; }

    public double getVida() { return vida; }

    public void reducirVida(double porcentaje) {
        long ahora = System.nanoTime();
        if (ahora - ultimoDaño < tiempoInvulnerable) return; // está invulnerable
        this.vida -= porcentaje;
        ultimoDaño = ahora;
        if (vida <= 0) {
            vida = 0;
            setVivo(false);
        }
    }

    public void recuperarVida(double porcentaje) {
        if (this.vida < 100) {
            this.vida += porcentaje;
            if (this.vida > 100) this.vida = 100;
        }
    }

    public void aplicarKnockback(double fuerza) {
        this.knockbackX = fuerza;
    }

    // Para compatibilidad con llamadas previas que usaban 'empujon'
    public void empujon(int fuerza) {
        aplicarKnockback(fuerza);
    }

    public void drawHealthBar(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(10, 10, 200, 20);
        gc.setFill(Color.GREEN);
        gc.fillRect(10, 10, (vida / 100) * 200, 20);
    }
}
