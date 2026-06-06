package modelo;

public class Casilla {
    private int fila;
    private int columna;
    private boolean esMina;
    private int numeroAdyacentes;
    private boolean descubierta;
    private boolean marcada;

    public Casilla(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.esMina = false;
        this.descubierta = false;
        this.marcada = false;
        this.numeroAdyacentes = 0;
    }

    // Getters y Setters
    public boolean esMina() { return esMina; }
    public void setMina(boolean esMina) { this.esMina = esMina; }

    public int getNumeroAdyacentes() { return numeroAdyacentes; }
    public void setNumeroAdyacentes(int numero) { this.numeroAdyacentes = numero; }

    public boolean isDescubierta() { return descubierta; }
    public void descubrir() { this.descubierta = true; }

    public boolean isMarcada() { return marcada; }
    public void toggleMarcada() { this.marcada = !this.marcada; }

    public String getSimbolo() {
        if (!descubierta) {
            return marcada ? "🚩" : "■";
        }
        if (esMina) return "💣";
        if (numeroAdyacentes == 0) return "  ";
        return " " + numeroAdyacentes;
    }
}