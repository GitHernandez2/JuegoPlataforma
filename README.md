# JuegoPlataforma
2-Dark Souls
Juego de Plataformas 2D - JavaFX
Descripción

Juego 2D en JavaFX basado en la saga de juegos Dark Souls donde controlas a un jugador que salta entre plataformas, evita enemigos y recoge items.
El jugador tiene vida, y el juego termina cuando su vida llega a 0.

Clases principales

Main.java: Inicializa el juego y la ventana.

Game.java: Controla la lógica del juego, loop, colisiones y renderizado.

Jugador.java: Representa al jugador, con movimiento, salto, vida y barra de salud.

Plataforma.java: Representa plataformas donde el jugador y enemigos pueden caminar.

Enemigo.java: Clase base de enemigos.

EnemigoTerrestre.java: Enemigo que patrulla una plataforma.

EnemigoVolador.java: Enemigo que se mueve en patrón sinusoidal en el aire.

Item.java: Objeto recolectable que restaura vida y aumenta el contador de items.

ArchivoJuego.java: Guarda y carga el progreso del jugador (puntaje y nombre).

Entidad.java: Clase base para todos los objetos dibujables y actualizables.

Controles

Izquierda / Derecha: Mover jugador

Espacio: Saltar

S: Guardar progreso

Características

Barra de vida del jugador.

Items que recuperan vida.

Enemigos terrestres y voladores con movimiento propio.

Requisitos:
-Java 17 o superior
-JavaFX 17 o superior
-IDE compatible con JavaFX (IntelliJ IDEA, Eclipse, NetBeans)
