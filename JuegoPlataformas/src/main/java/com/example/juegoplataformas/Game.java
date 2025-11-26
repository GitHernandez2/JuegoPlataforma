package com.example.juegoplataformas;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final int width;
    private final int height;

    private Jugador jugador;
    private List<Entidad> entidades;
    private List<Plataforma> plataformas;
    private List<Item> items;
    private Set<KeyCode> keys = new HashSet<>();
    private ArchivoJuego archivoJuego;
    private int contadorItems = 0;

    private AnimationTimer loop;
    private Image background;
    private boolean gameOver = false;


    public Game(int width, int height) {
        this.width = width;
        this.height = height;
        this.canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
        init();
    }

    public Canvas getCanvas() { return canvas; }

    private void init() {
        archivoJuego = new ArchivoJuego("datos/progreso.txt");
        entidades = new ArrayList<>();
        plataformas = new ArrayList<>();
        items = new ArrayList<>();

        // Fondo (opcional)
        try {
            background = new Image("/fondo.png");
        } catch (Exception e) {
            background = null;
            System.out.println("No se pudo cargar el fondo: " + e.getMessage());
        }

        // Crear el jugador (posición original)
        jugador = new Jugador(50, 450, 40, 60);
        entidades.add(jugador);

        // Crear plataformas (las mismas que tenías originalmente)
        Plataforma suelo = new Plataforma(0, 540, 800, 60);
        Plataforma plataforma1 = new Plataforma(150, 150, 180, 20);
        Plataforma plataforma2 = new Plataforma(500, 200, 180, 20);
        Plataforma plataforma3 = new Plataforma(100, 400, 180, 20);
        Plataforma plataforma4 = new Plataforma(450, 350, 180, 20);
        plataformas.add(suelo);
        plataformas.add(plataforma1);
        plataformas.add(plataforma2);
        plataformas.add(plataforma3);
        plataformas.add(plataforma4);

        // Enemigos terrestres (con rutas/sprites y alturas/posiciones como tenías)
        EnemigoTerrestre enemigoTerrestreSuelo = new EnemigoTerrestre(100, 500, 40, 40, 1.5, "/enemy_ground.jpg");
        // Establecer patrulla en suelo (entre 0 y ancho)
        enemigoTerrestreSuelo.setPatrolBounds(0, width);

        EnemigoTerrestre enemigoTerrestrePlataforma = new EnemigoTerrestre(450, 310, 40, 40, 1.2, "/enemy_ground.jpg");
        // Este enemigo debe moverse solo en la plataforma4:
        enemigoTerrestrePlataforma.setPlataforma(plataforma4);

        EnemigoVolador enemigoVolador = new EnemigoVolador(600, 180, 40, 40, 1.5);

        entidades.add(enemigoTerrestreSuelo);
        entidades.add(enemigoTerrestrePlataforma);
        entidades.add(enemigoVolador);

        // Items (con rutas originales)
        items.add(new Item(150, 350, 20, 20, "/path/to/item_sprite.png"));
        items.add(new Item(580, 150, 20, 20, "/path/to/item_sprite.png"));
        items.add(new Item(150, 100, 20, 20, "/path/to/item_sprite.png"));

        // Loop
        loop = new AnimationTimer() {
            private long last = 0;
            @Override
            public void handle(long now) {
                if (last == 0) last = now;
                double delta = (now - last) / 1e9;
                actualizar(delta);
                dibujar();
                last = now;
            }
        };

        // Cargar progreso
        try {
            ArchivoJuego.Progreso p = archivoJuego.cargar();
            if (p != null) {
                jugador.setPuntaje(p.puntaje);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupInput(Scene scene) {
        // Mantener inputs del jugador (izq, der, espacio)
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            keys.add(e.getCode());
            if (e.getCode() == KeyCode.S) {
                guardar();
            }
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, e -> keys.remove(e.getCode()));
        scene.setOnKeyPressed(e -> {
            if (gameOver) return; // ⛔ no permitir mover al jugador

            switch (e.getCode()) {
                case A -> jugador.moverIzquierda();
                case D -> jugador.moverDerecha();
                case W -> jugador.saltar();
            }
        });

    }

    public void start() { loop.start(); }

    private void actualizar(double delta) {
        // Movimiento del jugador por teclas
        if (keys.contains(KeyCode.LEFT)) jugador.moverIzquierda();
        if (keys.contains(KeyCode.RIGHT)) jugador.moverDerecha();
        if (keys.contains(KeyCode.SPACE)) jugador.saltar();

        // Actualizar entidades
        for (Entidad en : entidades) en.update();

        // Aplicar gravedad al jugador
        jugador.applyGravity();
        boolean onPlatform = false;
        for (Plataforma p : plataformas) {
            if (jugador.getBounds().intersects(p.getBounds())) {
                jugador.landOn(p);
                onPlatform = true;
            }
        }
        if (!onPlatform) jugador.setEnSuelo(false);

        // Colisiones con enemigos
        for (Entidad en : entidades) {
            if (en instanceof Enemigo) {
                if (jugador.getBounds().intersects(en.getBounds())) {
                    if (jugador.isVivo()) {
                        jugador.reducirVida(30);
                        // aplicar empujón: si el enemigo está a la izquierda, empuja a la derecha y viceversa
                        double fuerza = (jugador.getX() < en.getX()) ? 10 : -10;
                        // usamos el método empujon (compatibilidad)
                        jugador.empujon((int) fuerza);
                    }
                    if (!jugador.isVivo()) {
                        gameOver = true;
                    }
                    if (gameOver) {
                        return;  // ❄️ DETIENE TODA LA LÓGICA DEL JUEGO
                    }

                }
            }
        }

        // Colisiones con items
        List<Item> itemsParaEliminar = new ArrayList<>();
        for (Item item : items) {
            if (jugador.getBounds().intersects(item.getBounds())) {
                contadorItems++;
                jugador.recuperarVida(30);
                itemsParaEliminar.add(item);
            }
        }
        items.removeAll(itemsParaEliminar);
    }

    private void dibujar() {
        // fondo
        if (background != null) {
            gc.drawImage(background, 0, 0, width, height);
        } else {
            gc.setFill(Color.web("#1e1e1e"));
            gc.fillRect(0, 0, width, height);
        }

        // plataformas
        for (Plataforma p : plataformas) p.draw(gc);

        // entidades
        for (Entidad e : entidades) e.draw(gc);

        // items
        for (Item item : items) item.draw(gc);

        // HUD
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(18));
        gc.fillText("Puntaje: " + jugador.getPuntaje(), 20, 30);
        gc.fillText("Humanidades: " + contadorItems, 20, 55);
        gc.fillText("Presiona 'S' para guardar", 20, 80);

        jugador.drawHealthBar(gc);

        if (!jugador.isVivo()) {
            gc.setFill(Color.color(0, 0, 0, 0.6));
            gc.fillRect(0, 0, width, height);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(36));
            gc.fillText("YOU DIED", width / 2 - 100, height / 2);
        }
    }

    private void guardar() {
        try {
            archivoJuego.guardar(new ArchivoJuego.Progreso(jugador.getPuntaje(), "player"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
