package ar.edu.unq.epergeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AngelTest {
    public static final Medium MEDIUM = new Medium("Brujita", null, null);
    private Espiritu angel;

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
        angel = new Angel(
                0,
                "pepe"

        );
    }





    @Test
    void alCrearAngelNoPuedeTenerMasDe100DeNivelDeConexion() {
        angel = new Angel(
                250,
                "Pepe"
        );

        assertEquals(100, angel.getNivelDeConexion());
    }

    @Test
    void alCrearAngelNoPuedeTenerMenosDe0DeNivelDeConexion() {
        angel = new Angel(
                -50,
                "Pepe"
        );

        assertEquals(0, angel.getNivelDeConexion());
    }

//    @Test
//    void alAumentarElNivelDeConexionDeUnEspirituEstaAumentaEn10() {
//        // empíeza en 0
//        espiritu.aumentarConexion(MEDIUM);
//
//        assertEquals(10, espiritu.getNivelDeConexion());
//    }

//
//    @Test
//    void alAumentarConexionConEspirituNoPuedeTenerMasDe100DeNivelDeConexion() {
//        espiritu = new Espiritu(
//                "Fantasma",
//                100,
//                "Pepe"
//        );
//        espiritu.aumentarConexion(MEDIUM);
//
//
//
//        assertEquals(100, espiritu.getNivelDeConexion());
//    }
//


//
//
//

}