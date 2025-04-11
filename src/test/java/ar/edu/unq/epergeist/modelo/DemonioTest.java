package ar.edu.unq.epergeist.modelo;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DemonioTest {
    private Espiritu demonio;
    private Ubicacion ubicacion = new Ubicacion("Washington");

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
                "Pepardo", ubicacion

        );
    }

    @Test
    void alCrearAngelNoPuedeTenerMasDe100DeNivelDeConexion() {
        demonio = new Demonio(
                250,
                "Pepe", ubicacion
        );

        assertEquals(100, demonio.getNivelDeConexion());
    }

    @Test
    void alCrearAngelNoPuedeTenerMenosDe0DeNivelDeConexion() {
        demonio = new Demonio(
                -50,
                "Pepe", ubicacion
        );

        assertEquals(0, demonio.getNivelDeConexion());
    }



//
    @Test
    void alAumentarConexionConEspirituNoPuedeTenerMasDe100DeNivelDeConexion() {
        demonio = new Demonio(
                100,
                "Pepe", ubicacion
        );
        demonio.aumentarNivelDeConexionEn(10);

        assertEquals(100, demonio.getNivelDeConexion());
    }
}