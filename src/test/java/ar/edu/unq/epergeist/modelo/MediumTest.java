package ar.edu.unq.epergeist.modelo;

import ar.edu.unq.epersgeist.modelo.RandomGenerator.RandomGenerator;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exeption.ExorcistaSinAngelesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MediumTest {
    RandomGenerator randomAngelesDaAltoYBajo = new RandomGenerator(12);//9 el acutal da 7 y 3
//    RandomGenerator randomAngelesDa1 = new RandomGenerator(18);
//
    RandomGenerator randomDemoniosDaBajoYAlto = new RandomGenerator(6);//230 el actual da 2 y 7
//    RandomGenerator randomDemoniosDa1 = new RandomGenerator(18);

    RandomGenerator randomFijoAngeles;
    RandomGenerator randomFijoDemonios;

    private Angel angel1;
    private Angel angel2;
    private Demonio demonio1;
    private Demonio demonio2;

    Medium medium;
    Medium medium2;

    @BeforeEach
    void crearMedium() {
        medium = new Medium("Francisco", 50, 30);
        medium2 = new Medium("Rafa", 50, 15);

        randomFijoAngeles = new RandomGenerator();
        randomFijoDemonios = new RandomGenerator();

        angel1 = new Angel(60, "Uriel",randomFijoAngeles);
        angel2 = new Angel(5, "Ariel",randomFijoAngeles);

        demonio1 = new Demonio(50, "Azazel",randomFijoDemonios);
        demonio2 = new Demonio(3, "Dismas",randomFijoDemonios);
    }

    @Test
    void alConectarConUnEspirituElMediumAgregaEseEspirituALosSuyos() {
        medium.conectarseAEspiritu(angel1);

        assertEquals(1, medium.getAngeles().size());
        assertTrue(medium.getAngeles().contains(angel1));

        System.out.println(randomDemoniosDaBajoYAlto.getRandom(1,10));
        System.out.println(randomDemoniosDaBajoYAlto.getRandom(1,10));
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

        randomFijoAngeles.setValorFijo(10);
        randomFijoDemonios.setValorFijo(1);

        medium.conectarseAEspiritu(angel1);
        medium2.conectarseAEspiritu(demonio1);

        medium.exorcizarA(medium2);

        assertEquals(25,demonio1.getNivelDeConexion());
        assertEquals(70,angel1.getNivelDeConexion());

    }

    @Test
    void alExorcizarAUnMediumElAngelPierdeYDisminuyeNivelDeConexionDelAngel(){

        randomFijoAngeles.setValorFijo(1);
        randomFijoDemonios.setValorFijo(100);


        medium.conectarseAEspiritu(angel1);
        medium2.conectarseAEspiritu(demonio1);

        medium.exorcizarA(medium2);

        assertEquals(60,demonio1.getNivelDeConexion());
        assertEquals(65,angel1.getNivelDeConexion());


    }

    @Test
    void alExorcizarAUnMediumElPrimerAngelVenceAlPrimerDemonioYElSegundoAngelPierdeContraElSegundoDemonio(){

        angel1.setRandomGenerator(randomAngelesDaAltoYBajo);
        angel2.setRandomGenerator(randomAngelesDaAltoYBajo);

        demonio1.setRandomGenerator(randomDemoniosDaBajoYAlto);
        demonio2.setRandomGenerator(randomDemoniosDaBajoYAlto);

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