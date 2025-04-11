package ar.edu.unq.epergeist.modelo;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AngelTest {
    private Espiritu angel;
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
        angel = new Angel(
                0,
                "pepe",ubicacion

        );
    }

    @Test
    void alCrearAngelNoPuedeTenerMasDe100DeNivelDeConexion() {
        angel = new Angel(
                250,
                "Pepe", ubicacion
        );

        assertEquals(100, angel.getNivelDeConexion());
    }

    @Test
    void alCrearAngelNoPuedeTenerMenosDe0DeNivelDeConexion() {
        angel = new Angel(
                -50,
                "Pepe", ubicacion
        );

        assertEquals(0, angel.getNivelDeConexion());
    }



    //
    @Test
    void alAumentarConexionConEspirituNoPuedeTenerMasDe100DeNivelDeConexion() {
        angel = new Demonio(
                100,
                "Pepe", ubicacion
        );
        angel.aumentarNivelDeConexionEn(10);

        assertEquals(100, angel.getNivelDeConexion());
    }

    @Test
    void seVerificaQueUnAngelPuedeCambiarDeUbicacionSiEstaLibre() {
        Ubicacion ubicacion1 = new Ubicacion("Magical");
        this.angel.setUbicacion(ubicacion1);

        assertEquals(this.angel.getUbicacion().getNombre(), ubicacion1.getNombre());
    }

    @Test
    void seVerificaQueUnAngelNoPuedeCambiarDeUbicacionSiEstaConectadoAUnMedium() {
        Ubicacion ubicacion1 = new Ubicacion("Magical");
        Medium medium = new Medium("Franco", 200, 50, this.ubicacion);
        medium.conectarseAEspiritu(this.angel);
        this.angel.setUbicacion(ubicacion1);

        assertNotEquals(this.angel.getUbicacion().getNombre(), ubicacion1.getNombre());
    }
}