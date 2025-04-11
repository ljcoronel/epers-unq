package ar.edu.unq.epergeist.modelo;

import ar.edu.unq.epersgeist.modelo.RandomGenerator.RandomGenerator;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.ExorcistaSinAngelesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MediumTest {
    private Ubicacion ubicacion = new Ubicacion("Washington");

    private Angel angel1;
    private Angel angel2;
    private Demonio demonio1;
    private Demonio demonio2;

    Medium medium;
    Medium medium2;

    @BeforeEach
    void crearMedium() {
        medium = new Medium("Francisco", 50, 30,ubicacion);
        medium2 = new Medium("Rafa", 50, 15,ubicacion);

        angel1 = new Angel(60, "Uriel",ubicacion);
        angel2 = new Angel(5, "Ariel",ubicacion);

        demonio1 = new Demonio(50, "Azazel",ubicacion);
        demonio2 = new Demonio(3, "Dismas",ubicacion);
    }

    @Test
    void alConectarConUnEspirituElMediumAgregaEseEspirituALosSuyos() {
        medium.conectarseAEspiritu(angel1);

        assertEquals(1, medium.getAngeles().size());
        assertTrue(medium.getAngeles().contains(angel1));

    }
    @Test
    void alDescansarAumentaSuManaEn15YElNivelDeConexionDeSusEspiritusEn5(){

        medium.conectarseAEspiritu(angel1);

        medium.descansar();

        assertEquals(45,medium.getMana());
        assertEquals(75,angel1.getNivelDeConexion());
    }

    @Test
    void alDesvincularseDeUnAngelDejaDeEstarEnLaListaDelMedium(){
        medium.conectarseAEspiritu(angel1);

        medium.desvincularseDeEspiritu(angel1);

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

        medium.conectarseAEspiritu(angel1);

        medium.desvincularseDeEspiritu(demonio1);

        assertEquals(1, medium.getAngeles().size());

        assertTrue(medium.getDemonios().isEmpty());
    }

    @Test
    void alExorcizarAUnMediumElAngelGanaYDisminuyeNivelDeConexionDelDemonio(){

        RandomGenerator.addValorFijo(10);
        RandomGenerator.addValorFijo(1);

        medium.conectarseAEspiritu(angel1);
        medium2.conectarseAEspiritu(demonio1);

        medium.exorcizarA(medium2);

        assertEquals(25,demonio1.getNivelDeConexion());
        assertEquals(70,angel1.getNivelDeConexion());

    }

    @Test
    void alExorcizarAUnMediumElAngelPierdeYDisminuyeNivelDeConexionDelAngel(){

        RandomGenerator.addValorFijo(1);
        RandomGenerator.addValorFijo(100);

        medium.conectarseAEspiritu(angel1);
        medium2.conectarseAEspiritu(demonio1);

        medium.exorcizarA(medium2);

        assertEquals(60,demonio1.getNivelDeConexion());
        assertEquals(65,angel1.getNivelDeConexion());


    }

    @Test
    void alExorcizarAUnMediumElPrimerAngelVenceAlPrimerDemonioYElSegundoAngelPierdeContraElSegundoDemonio(){

        RandomGenerator.addValorFijo(10);
        RandomGenerator.addValorFijo(1);
        RandomGenerator.addValorFijo(1);
        RandomGenerator.addValorFijo(100);

        medium.conectarseAEspiritu(angel1);
        medium.conectarseAEspiritu(angel2);

        medium2.conectarseAEspiritu(demonio2);
        medium2.conectarseAEspiritu(demonio1);

        assertEquals(2,medium2.getDemonios().size());

        medium.exorcizarA(medium2);

        assertEquals(0,demonio2.getNivelDeConexion());
        assertEquals(70,angel1.getNivelDeConexion());

        assertEquals(60,demonio1.getNivelDeConexion());
        assertEquals(10,angel2.getNivelDeConexion());

        assertEquals(1,medium2.getDemonios().size());


    }

    @Test
    void alIntentarExorcizarSinAngelesLanzaUnError(){
        assertThrows(ExorcistaSinAngelesException.class, () -> {
            medium.exorcizarA(medium2);
        });
    }
}