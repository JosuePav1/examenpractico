package pruebaBuscaminas;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;

// Clase que representa una celda del tablero
class Casilla {
    boolean esMina;
    boolean descubierta;
    boolean marcada;
    int minasAdyacentes;

    public Casilla() {
        this.esMina = false;
        this.descubierta = false;
        this.marcada = false;
        this.minasAdyacentes = 0;
    }

    public String obtenerSimbolo(boolean revelarTodo) {
        // Si se acabó el juego, mostramos las minas
        if (revelarTodo && esMina) return "X";
        
        // Si no está descubierta, puede estar marcada
        if (!descubierta) {
            if (marcada) return "?";
            return "-";
        }
        
        // Si está descubierta
        if (esMina) return "X";
        if (minasAdyacentes == 0) return "V"; // Espacio vacío seleccionado
        
        return String.valueOf(minasAdyacentes);
    }
}

// Clase que maneja la lógica de la matriz 10x10
class Tablero {
    int filas = 10;
    int columnas = 10;
    int numMinas = 10;
    Casilla[][] matriz;
    int casillasSegurasRestantes;

    public Tablero() {
        matriz = new Casilla[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = new Casilla();
            }
        }
        casillasSegurasRestantes = (filas * columnas) - numMinas;
        colocarMinas();
        calcularAdyacencias();
    }

    private void colocarMinas() {
        Random rand = new Random();
        int minasColocadas = 0;
        while (minasColocadas < numMinas) {
            int f = rand.nextInt(filas);
            int c = rand.nextInt(columnas);
            if (!matriz[f][c].esMina) {
                matriz[f][c].esMina = true;
                minasColocadas++;
            }
        }
    }

    private void calcularAdyacencias() {
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (matriz[f][c].esMina) continue;
                
                int minas = 0;
                // Revisar las 8 posiciones adyacentes (incluyendo diagonales)
                for (int df = -1; df <= 1; df++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nf = f + df;
                        int nc = c + dc;
                        if (nf >= 0 && nf < filas && nc >= 0 && nc < columnas) {
                            if (matriz[nf][nc].esMina) minas++;
                        }
                    }
                }
                matriz[f][c].minasAdyacentes = minas;
            }
        }
    }

    public void imprimirTablero(boolean revelarTodo) {
        System.out.print("\n    ");
        for (int i = 1; i <= columnas; i++) {
            System.out.printf("%2d ", i);
        }
        System.out.println();
        
        String letras = "ABCDEFGHIJ";
        for (int f = 0; f < filas; f++) {
            System.out.print(letras.charAt(f) + "  ");
            for (int c = 0; c < columnas; c++) {
                System.out.print(" " + matriz[f][c].obtenerSimbolo(revelarTodo) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean revelar(int f, int c) {
        Casilla casilla = matriz[f][c];
        
        if (casilla.descubierta || casilla.marcada) return true; // Sigue el juego
        
        casilla.descubierta = true;
        
        if (casilla.esMina) return false; // Pisó una mina, pierde
        
        casillasSegurasRestantes--;
        
        // Recursividad: Si está vacía, revelar adyacentes automáticamente
        if (casilla.minasAdyacentes == 0) {
            for (int df = -1; df <= 1; df++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int nf = f + df;
                    int nc = c + dc;
                    if (nf >= 0 && nf < filas && nc >= 0 && nc < columnas) {
                        if (!matriz[nf][nc].descubierta) {
                            revelar(nf, nc);
                        }
                    }
                }
            }
        }
        return true;
    }

    public void marcarCasilla(int f, int c) {
        Casilla casilla = matriz[f][c];
        if (!casilla.descubierta) {
            casilla.marcada = !casilla.marcada;
        }
    }
}

// Clase que maneja el flujo de la partida y los archivos
class Juego {
    Tablero tablero;
    boolean jugando;
    boolean victoria;
    Scanner scanner;

    public Juego() {
        tablero = new Tablero();
        jugando = true;
        victoria = false;
        scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("===================================");
        System.out.println("       JUEGO DE BUSCAMINAS         ");
        System.out.println("===================================");

        String comando = "";

        while (jugando) {
            tablero.imprimirTablero(false);
            System.out.println("Instrucciones:");
            System.out.println(" - Para revelar: Escribe coordenada (Ej: A5)");
            System.out.println(" - Para marcar/desmarcar mina: Escribe M seguido de coordenada (Ej: M A5)");
            System.out.println(" - Escribe SALIR para rendirte.\n");
            
            System.out.print("Tu jugada: ");
            comando = scanner.nextLine().trim().toUpperCase();

            if (comando.equals("SALIR")) {
                System.out.println("Te has rendido.");
                break;
            }

            String[] partes = comando.split("\\s+");
            String accion = "REVELAR";
            String coord = "";

            if (partes.length == 2 && partes[0].equals("M")) {
                accion = "MARCAR";
                coord = partes[1];
            } else if (partes.length == 1) {
                coord = partes[0];
            } else {
                System.out.println("Error: Comando no reconocido.\n");
                continue;
            }

            if (coord.length() < 2) {
                System.out.println("Error: Coordenada incompleta.\n");
                continue;
            }

            char letra = coord.charAt(0);
            String numStr = coord.substring(1);
            String letrasValidas = "ABCDEFGHIJ";

            if (letrasValidas.indexOf(letra) == -1 || !numStr.matches("\\d+")) {
                System.out.println("Error: Coordenada inválida (Usa letras de la A-J y números del 1-10).\n");
                continue;
            }

            int f = letrasValidas.indexOf(letra);
            int c = Integer.parseInt(numStr) - 1;

            if (c < 0 || c > 9) {
                System.out.println("Error: Número de columna fuera de rango (1-10).\n");
                continue;
            }

            if (accion.equals("MARCAR")) {
                tablero.marcarCasilla(f, c);
            } else if (accion.equals("REVELAR")) {
                boolean sigueVivo = tablero.revelar(f, c);

                if (!sigueVivo) {
                    jugando = false;
                    victoria = false;
                } else if (tablero.casillasSegurasRestantes == 0) {
                    jugando = false;
                    victoria = true;
                }
            }
        }

        // Fin de la partida
        tablero.imprimirTablero(true);
        if (victoria) {
            System.out.println("¡FELICIDADES! Has descubierto todas las casillas seguras. GANASTE.");
        } else if (!jugando && !comando.equals("SALIR")) {
            System.out.println("¡BOOM! Has pisado una mina (X). FIN DEL JUEGO.");
        }

        guardarRegistro();
        scanner.close();
    }

    private void guardarRegistro() {
        try {
            FileWriter writer = new FileWriter("historial_partidas.txt", true);
            String resultado = victoria ? "VICTORIA" : "DERROTA";
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fecha = ahora.format(formato);
            
            writer.write("[" + fecha + "] Resultado: " + resultado + " | Casillas seguras restantes: " + tablero.casillasSegurasRestantes + "\n");
            writer.close();
            System.out.println("> El resultado de tu partida se ha guardado en 'historial_partidas.txt'.");
        } catch (IOException e) {
            System.out.println("> Error: No se pudo escribir en el archivo de historial.");
        }
    }
}

// Clase Principal que contiene el método main
public class Buscaminas {
    public static void main(String[] args) {
        Juego juego = new Juego();
        juego.iniciar();
    }
}