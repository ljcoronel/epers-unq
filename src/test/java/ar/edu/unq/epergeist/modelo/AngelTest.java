package ar.edu.unq.epergeist.modelo;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void seAumentaCorrectamenteElNivelDeConexion(){
        int nivelDeConexionDemonio = 60;
        int nivelDeConexionAngel= 20;

        Espiritu demonio = new Demonio( nivelDeConexionDemonio
                ,
                "Pepe", ubicacion
        );
        Espiritu angel = new Angel( nivelDeConexionAngel
                ,
                "angel", ubicacion
        );
        int aumetoDeConexion = 10;

        demonio.aumentarNivelDeConexionEn(aumetoDeConexion);
        angel.aumentarNivelDeConexionEn(aumetoDeConexion);

        assertEquals(demonio.getNivelDeConexion(), aumetoDeConexion + nivelDeConexionDemonio);
        assertEquals(angel.getNivelDeConexion(), aumetoDeConexion + nivelDeConexionAngel);

    }


}
