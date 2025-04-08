package ar.edu.unq.epergeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MediumTest {
    private final Espiritu espiritu = new Angel(79, "Poltergeist");
    private final Angel espiritu1 = new Angel(60, "Fantasma");
    Medium medium;

    @BeforeEach
    void crearMedium() {
        medium = new Medium("Francisco", 50, 30);
        medium.agregarAngel(espiritu1);
    }


    @Test
    void alConectarConUnEspirituElMediumAgregaEseEspirituALosSuyos() {
        medium.conectarseAEspiritu(espiritu);

        assertEquals(1, medium.getAngeles().size());
        assertTrue(medium.getAngeles().contains(espiritu));

    }
    @Test
    void alDescansarAumentaSuManaEn15YElNivelDeConexionDeSusEspiritusEn5(){
        medium.descansar();
        assertEquals(45,medium.getMana());
        assertEquals(65,espiritu1.getNivelDeConexion());
    }



}