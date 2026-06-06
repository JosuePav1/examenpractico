package modelo;

import java.util.Random;

public class Tablero {
    private Casilla[][] casillas;
    private final int FILAS = 10;
    private final int COLUMNAS = 10;
    private final int MINAS = 10;

    public Tablero() {
        casillas = new Casilla[FILAS][COLUMNAS];
        inicializar();
        colocarMinas();
        calcularNumeros();
    }

    private void inicializar() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                casillas[i][j] = new Casilla(i, j);
            }
        }
    }

    private void colocarMinas() {
        Random rand = new Random();
        int colocadas = 0;
        while (colocadas < MINAS) {
            int f = rand.nextInt(FILAS);
            int c = rand.nextInt(COLUMNAS);
            if (!casillas[f][c].esMina()) {
                casillas[f][c].setMina(true);
                colocadas++;
            }
        }
    }

    private void calcularNumeros() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (!casillas[i][j].esMina()) {
                    casillas[i][j].setNumeroAdyacentes(contarMinasAlrededor(i, j));
                }
            }
        }
    }

    private int contarMinasAlrededor(int fila, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int nf = fila + i, nc = col + j;
                if (nf >= 0 && nf < FILAS && nc >= 0 && nc < COLUMNAS && casillas[nf][nc].esMina()) {
                    count++;
                }
            }
        }
        return count;
    }

    public void descubrir(int fila, int col) {
        if (fila < 0 || fila >= FILAS || col < 0 || col >= COLUMNAS) return;
        
        Casilla casilla = casillas[fila][col];
        if (casilla.isDescubierta() || casilla.isMarcada()) return;

        casilla.descubrir();

        if (casilla.getNumeroAdyacentes() == 0 && !casilla.esMina()) {
            floodFill(fila, col);
        }
    }

    private void floodFill(int fila, int col) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nf = fila + i;
                int nc = col + j;
                if (nf >= 0 && nf < FILAS && nc >= 0 && nc < COLUMNAS) {
                    Casilla casilla = casillas[nf][nc];
                    if (!casilla.isDescubierta() && !casilla.isMarcada() && !casilla.esMina()) {
                        casilla.descubrir();
                        if (casilla.getNumeroAdyacentes() == 0) {
                            floodFill(nf, nc);   // Revelado en cadena
                        }
                    }
                }
            }
        }
    }

    public Casilla getCasilla(int fila, int col) {
        return casillas[fila][col];
    }

    public boolean esVictoria() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (!casillas[i][j].esMina() && !casillas[i][j].isDescubierta()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void mostrar() {
        System.out.println("\n" + "=".repeat(45));
        System.out.println("              🚩 BUSCAMINAS 🚩");
        System.out.println("=".repeat(45));
        
        System.out.print("    ");
        for (int j = 1; j <= COLUMNAS; j++) {
            System.out.printf("%2d ", j);
        }
        System.out.println();

        for (int i = 0; i < FILAS; i++) {
            System.out.print((char)('A' + i) + "  ");
            for (int j = 0; j < COLUMNAS; j++) {
                System.out.printf("%2s ", casillas[i][j].getSimbolo());
            }
            System.out.println();
        }
        System.out.println("=".repeat(45));
    }
}