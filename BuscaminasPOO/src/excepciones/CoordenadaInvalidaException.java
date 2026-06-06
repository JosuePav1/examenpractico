package excepciones;

public class CoordenadaInvalidaException extends Exception {
    public CoordenadaInvalidaException() {
        super("Coordenada inválida. Usa formato como A5 o M A5");
    }
}