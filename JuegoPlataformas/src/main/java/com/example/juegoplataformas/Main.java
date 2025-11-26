package com.example.juegoplataformas;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear una instancia de la clase Game
        Game game = new Game(800, 600); // Puedes cambiar el tamaño de la ventana si lo deseas

        // Crear el contenedor raíz (StackPane) y agregar el Canvas del juego
        StackPane root = new StackPane(game.getCanvas());

        // Crear la escena y pasar el contenedor raíz
        Scene scene = new Scene(root, 800, 600);

        // Configurar el manejo de las entradas (teclado) para el juego
        game.setupInput(scene);

        // Configuración de la ventana del juego
        primaryStage.setTitle("Juego Plataforma 2D - JavaFX");
        primaryStage.setScene(scene);  // Establecer la escena en el stage
        primaryStage.setWidth(800);    // Establecer el ancho de la ventana
        primaryStage.setHeight(600);   // Establecer la altura de la ventana
        primaryStage.show();          // Mostrar la ventana

        // Iniciar el juego
        game.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
