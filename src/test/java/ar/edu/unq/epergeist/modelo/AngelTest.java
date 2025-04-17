package ar.edu.unq.epergeist.modelo;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AngelTest {
    private Espiritu angel;
    private Ubicacion ubicacion = new Ubicacion("Washington");


    @BeforeEach
    void crearEspiritu() {
        angel = new Angel(
                0,
                "pepe", ubicacion

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



}