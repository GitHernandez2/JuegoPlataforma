package com.example.juegoplataformas;

import java.io.*;

public class ArchivoJuego {
    private final String ruta;

    public ArchivoJuego(String ruta) {
        this.ruta = "/Users/apple/IdeaProjects/JuegoPlataformas/src/main/java/progreso.txt";
    }

    public void guardar(Progreso p) throws IOException {
        try (FileWriter fw = new FileWriter(ruta)) {
            fw.write("puntaje=" + p.puntaje + "\n");
            fw.write("jugador=" + p.nombre + "\n");
        }
    }

    public Progreso cargar() throws IOException {
        File file = new File(ruta);
        if (!file.exists()) return null;
        int puntaje = 0;
        String nombre = "player";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("puntaje=")) puntaje = Integer.parseInt(line.split("=")[1].trim());
                if (line.startsWith("jugador=")) nombre = line.split("=")[1].trim();
            }
        }
        return new Progreso(puntaje, nombre);
    }

    public static class Progreso {
        public int puntaje;
        public String nombre;

        public Progreso(int puntaje, String nombre) {
            this.puntaje = puntaje;
            this.nombre = nombre;
        }
    }
}
