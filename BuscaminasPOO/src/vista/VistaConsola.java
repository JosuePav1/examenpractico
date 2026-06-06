package vista;

public class VistaConsola {

    public void mostrarBienvenida() {
        System.out.println("=====================================");
        System.out.println("     BIENVENIDO AL BUSCAMINAS");
        System.out.println("=====================================");
        System.out.println("Tablero 10x10 - 10 minas");
    }

    public void mostrarMenu() {
        System.out.println("\nOpciones:");
        System.out.println("   → Ingrese coordenada (ejemplo: A5)");
        System.out.println("   → Escriba 'M A5' para marcar/desmarcar");
        System.out.println("   → Escriba 'S' para salir");
        System.out.print("Ingrese su movimiento: ");
    }

    public void mostrarVictoria() {
        System.out.println("\n🎉 ¡FELICIDADES! ¡Has ganado el juego! 🎉");
    }

    public void mostrarDerrota() {
        System.out.println("\n💥 ¡BOOM! Has explotado una mina. Game Over.");
    }

    public void mostrarError(String mensaje) {
        System.out.println("❌ Error: " + mensaje);
    }
}