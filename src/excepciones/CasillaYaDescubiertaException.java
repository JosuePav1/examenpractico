package excepciones;

public class CasillaYaDescubiertaException extends Exception {
    public CasillaYaDescubiertaException() {
        super("Esta casilla ya ha sido descubierta");
    }
}