package modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CasillaTest {

    @Test
    void testCasillaInicial() {
        Casilla casilla = new Casilla(0, 0);
        assertFalse(casilla.isDescubierta());
        assertFalse(casilla.isMarcada());
        assertFalse(casilla.esMina());
        assertEquals(0, casilla.getNumeroAdyacentes());
    }

    @Test
    void testMarcarCasilla() {
        Casilla casilla = new Casilla(0, 0);
        casilla.toggleMarcada();
        assertTrue(casilla.isMarcada());
        
        casilla.toggleMarcada();
        assertFalse(casilla.isMarcada());
    }

    @Test
    void testDescubrirCasilla() {
        Casilla casilla = new Casilla(0, 0);
        casilla.descubrir();
        assertTrue(casilla.isDescubierta());
    }

    @Test
    void testSetMina() {
        Casilla casilla = new Casilla(0, 0);
        casilla.setMina(true);
        assertTrue(casilla.esMina());
    }
}