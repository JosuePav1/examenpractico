package principal;

import controlador.ControladorJuego;

public class Main {
    public static void main(String[] args) {
        ControladorJuego juego = new ControladorJuego();
        juego.iniciar();
    }
}