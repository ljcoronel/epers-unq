package ar.edu.unq.epergeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MediumTest {
    private final Angel espiritu = new Angel(79, "Poltergeist");
    private final Angel espiritu1 = new Angel(60, "Fantasma");
    private final Demonio demonio1 = new Demonio(50, "Fantasma");
    Medium medium;

    @BeforeEach
    void crearMedium() {
        medium = new Medium("Francisco", 50, 30);
    }


    @Test
    void alConectarConUnEspirituElMediumAgregaEseEspirituALosSuyos() {
        medium.conectarseAEspiritu(espiritu);

        assertEquals(1, medium.getAngeles().size());
        assertTrue(medium.getAngeles().contains(espiritu));

    }
    @Test
    void alDescansarAumentaSuManaEn15YElNivelDeConexionDeSusEspiritusEn5(){
        medium.conectarseAEspiritu(espiritu1);

        medium.descansar();

        assertEquals(45,medium.getMana());
        assertEquals(75,espiritu1.getNivelDeConexion());
    }

    @Test
    void alDesvincularseDeUnAngelDejaDeEstarEnLaListaDelMedium(){
        medium.conectarseAEspiritu(espiritu1);

        medium.desvincularseDeEspiritu(espiritu1);

        assertTrue(medium.getAngeles().isEmpty());
    }

    @Test
    void alDesvincularseDeUnDemonioDejaDeEstarEnLaListaDelMedium(){
        medium.conectarseAEspiritu(demonio1);

        medium.desvincularseDeEspiritu(demonio1);

        assertTrue(medium.getDemonios().isEmpty());

    }

    @Test
    void alIntentarDesvincularseDeUnEspirituQueNoEstaConectadoAlMediumNoHaceNada(){

        medium.conectarseAEspiritu(espiritu1);

        medium.desvincularseDeEspiritu(demonio1);

        assertEquals(1, medium.getAngeles().size());

        assertTrue(medium.getDemonios().isEmpty());
    }
}