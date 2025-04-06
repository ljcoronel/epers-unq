package ar.edu.unq.epergeist.modelo;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DemonioTest {
    public static final Medium MEDIUM = new Medium("Brujita", null, null);
    private Espiritu demonio;

//    Descomentar para ver logs.

//    @BeforeAll
//    static void setupLogging() {
//        try {
//            // Cargamos la configuración de logging
//            getLogManager().readConfiguration(
//                JDBCPersonajeDAOTest.class.getClassLoader().getResourceAsStream("logging.properties")
//            );
//        } catch (Exception e) {
//            System.err.println("No se pudo cargar la configuración de logging: " + e.getMessage());
//        }
//    }

    @BeforeEach
    void crearEspiritu() {
        demonio = new Demonio(
                0,
                "Pepardo"

        );
    }

    @Test
    void alCrearAngelNoPuedeTenerMasDe100DeNivelDeConexion() {
        demonio = new Demonio(
                250,
                "Pepe"
        );

        assertEquals(100, demonio.getNivelDeConexion());
    }

    @Test
    void alCrearAngelNoPuedeTenerMenosDe0DeNivelDeConexion() {
        demonio = new Demonio(
                -50,
                "Pepe"
        );

        assertEquals(0, demonio.getNivelDeConexion());
    }

    @Test
    void alAumentarElNivelDeConexionDeUnEspirituEstaAumentaEn10() {
        // empíeza en 0
        demonio.aumentarConexion(MEDIUM);

        assertEquals(10, demonio.getNivelDeConexion());
    }


//
    @Test
    void alAumentarConexionConEspirituNoPuedeTenerMasDe100DeNivelDeConexion() {
        demonio = new Demonio(
                100,
                "Pepe"
        );
        demonio.aumentarConexion(MEDIUM);

        assertEquals(100, demonio.getNivelDeConexion());
    }
}