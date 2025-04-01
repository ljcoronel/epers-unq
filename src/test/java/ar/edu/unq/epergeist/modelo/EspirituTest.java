/*package ar.edu.unq.epergeist.modelo;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EspirituTest {
    public static final Medium MEDIUM = new Medium("Brujita", null, null);
    private Espiritu espiritu;

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
        espiritu = new Espiritu(
                "Fantasma",
                "Pepe"
        );
    }

    @Test
    void alAumentarElNivelDeConexionDeUnEspirituEstaAumentaEn10() {
        // empíeza en 0
        espiritu.aumentarConexion(MEDIUM);

        assertEquals(10, espiritu.getNivelDeConexion());
    }

    @Test
    void alCrearEspirituNoPuedeTenerMasDe100DeNivelDeConexion() {
        espiritu = new Espiritu(
                "Fantasma",
                250,
                "Pepe"
        );

        assertEquals(100, espiritu.getNivelDeConexion());
    }

    @Test
    void alAumentarConexionConEspirituNoPuedeTenerMasDe100DeNivelDeConexion() {
        espiritu = new Espiritu(
                "Fantasma",
                100,
                "Pepe"
        );
        espiritu.aumentarConexion(MEDIUM);



        assertEquals(100, espiritu.getNivelDeConexion());
    }

    @Test
    void alCrearEspirituNoPuedeTenerMenosDe0DeNivelDeConexion() {
        espiritu = new Espiritu(
                "Fantasma",
                -50,
                "Pepe"
        );

        assertEquals(0, espiritu.getNivelDeConexion());
    }



    @AfterEach
    void emilinarModelo() {
        JDBCConnector.getInstance().execute(conn -> {
            try {
                var ps = conn.prepareStatement("DELETE from espiritu");
                ps.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return null;
        });
    }
}*/