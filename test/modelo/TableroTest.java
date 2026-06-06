package modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableroTest {

    private Tablero tablero;

    @BeforeEach
    void setUp() {
        tablero = new Tablero();
    }

    @Test
    void testTableroSeCreaCon10Minas() {
        int countMinas = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (tablero.getCasilla(i, j).esMina()) {
                    countMinas++;
                }
            }
        }
        assertEquals(10, countMinas);
    }

    @Test
    void testDescubrirCasilla() {
        tablero.descubrir(0, 0);
        assertTrue(tablero.getCasilla(0, 0).isDescubierta());
    }

    @Test
    void testEsVictoriaInicialmenteFalso() {
        assertFalse(tablero.esVictoria());
    }

    @Test
    void testGetCasilla() {
        Casilla casilla = tablero.getCasilla(5, 5);
        assertNotNull(casilla);
    }
}
