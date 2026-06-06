package controlador;

import modelo.Casilla;
import modelo.Tablero;
import vista.VistaConsola;
import excepciones.*;

import java.util.Scanner;

public class ControladorJuego {

    private Tablero tablero;
    private VistaConsola vista;
    private Scanner scanner;

    public ControladorJuego() {
        this.tablero = new Tablero();
        this.vista = new VistaConsola();
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        vista.mostrarBienvenida();
        boolean enJuego = true;

        while (enJuego) {
            tablero.mostrar();
            vista.mostrarMenu();

            try {
                String entrada = scanner.nextLine().trim().toUpperCase();

                if (entrada.equals("S")) {
                    System.out.println("¡Gracias por jugar! 👋");
                    enJuego = false;
                } 
                else if (entrada.startsWith("M ")) {
                    procesarMarcar(entrada);
                } 
                else {
                    procesarDescubrir(entrada);
                }

                if (tablero.esVictoria()) {
                    vista.mostrarVictoria();
                    enJuego = false;
                }

            } catch (Exception e) {
                vista.mostrarError(e.getMessage());
            }
        }
        scanner.close();
    }

    private void procesarDescubrir(String entrada) throws Exception {
        if (entrada.length() < 2) {
            throw new CoordenadaInvalidaException();
        }

        char letra = entrada.charAt(0);
        String numeroStr = entrada.substring(1);

        int fila = letra - 'A';
        int columna = Integer.parseInt(numeroStr) - 1;

        if (fila < 0 || fila > 9 || columna < 0 || columna > 9) {
            throw new CoordenadaInvalidaException();
        }

        Casilla casilla = tablero.getCasilla(fila, columna);

        if (casilla.isDescubierta()) {
            throw new CasillaYaDescubiertaException();
        }

        tablero.descubrir(fila, columna);

        // SI ES MINA → PIERDE Y REVELA TODAS LAS MINAS
        if (casilla.esMina()) {
            revelarTodasLasMinas();
            vista.mostrarDerrota();
            tablero.mostrar();        // Muestra el tablero completo con todas las bombas
            System.exit(0);
        }
    }

    private void procesarMarcar(String entrada) {
        try {
            String[] partes = entrada.split(" ");
            if (partes.length != 2) {
                throw new CoordenadaInvalidaException();
            }

            char letra = partes[1].charAt(0);
            int fila = letra - 'A';
            int columna = Integer.parseInt(partes[1].substring(1)) - 1;

            if (fila < 0 || fila > 9 || columna < 0 || columna > 9) {
                throw new CoordenadaInvalidaException();
            }

            tablero.getCasilla(fila, columna).toggleMarcada();
            System.out.println("✅ Casilla marcada/desmarcada.");
            
        } catch (Exception e) {
            vista.mostrarError("Formato incorrecto. Usa: M A5");
        }
    }

    // REVELA TODAS LAS MINAS CUANDO EL JUGADOR PIERDE
    private void revelarTodasLasMinas() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Casilla casilla = tablero.getCasilla(i, j);
                if (casilla.esMina()) {
                    casilla.descubrir();
                }
            }
        }
    }
}