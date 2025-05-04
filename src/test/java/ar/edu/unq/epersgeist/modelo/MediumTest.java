package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.testUtils.FakeRandomStrategyImpl;
import ar.edu.unq.epersgeist.modelo.RandomGenerator.RandomGenerator;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.exception.ExorcistaSinAngelesException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MediumTest {
    private Ubicacion ubicacion = new Ubicacion("Washington");

    private Angel angel1;
    private Angel angel2;
    private Demonio demonio1;
    private Demonio demonio2;
    private Medium medium;
    private Medium medium2;
    private FakeRandomStrategyImpl fakeRandomStrategy;

    @BeforeEach
    void crearMedium() {
        medium = new Medium("Francisco", 50, 30,ubicacion);
        medium2 = new Medium("Rafa", 50, 15,ubicacion);

        angel1 = new Angel(60, "Uriel",ubicacion);
        angel2 = new Angel(5, "Ariel",ubicacion);

        demonio1 = new Demonio(50, "Azazel",ubicacion);
        demonio2 = new Demonio(0, "Dismas",ubicacion);

        fakeRandomStrategy = new FakeRandomStrategyImpl();
        RandomGenerator.getInstance().setStrategy(fakeRandomStrategy);

    }

    @Test
    void alConectarConUnEspirituElMediumAgregaEseEspirituALosSuyos() {
        medium.realizarConexion(angel1);

        assertEquals(1, medium.getAngeles().size());
        assertTrue(medium.getAngeles().contains(angel1));

    }
    @Test
    void alDescansarAumentaSuManaEn15YElNivelDeConexionDeSusEspiritusEn5(){

        angel1.conectarConMedium(Optional.of(medium)); // empieza con 60

        medium.descansar(); // medium empieza con 30 de maná -> 66

        assertEquals(45,medium.getMana()); // + 15 = 45
        assertEquals(71,angel1.getNivelDeConexion()); // + 5 = 71
    }

    @Test
    void alDesvincularseDeUnAngelDejaDeEstarEnLaListaDelMedium(){
        medium.realizarConexion(angel1);

        medium.desvincularseDeEspiritu(angel1);

        assertTrue(medium.getAngeles().isEmpty());
    }

    @Test
    void alDesvincularseDeUnDemonioDejaDeEstarEnLaListaDelMedium(){
        medium.realizarConexion(demonio1);

        medium.desvincularseDeEspiritu(demonio1);

        assertTrue(medium.getDemonios().isEmpty());

    }

    @Test
    void alIntentarDesvincularseDeUnEspirituQueNoEstaConectadoAlMediumNoHaceNada(){

        medium.realizarConexion(angel1);

        medium.desvincularseDeEspiritu(demonio1);

        assertEquals(1, medium.getAngeles().size());

        assertTrue(medium.getDemonios().isEmpty());
    }

    @Test
    void alExorcizarAUnMediumElAngelGanaYDisminuyeNivelDeConexionDelDemonio(){

        fakeRandomStrategy.addValorFijo(10);
        fakeRandomStrategy.addValorFijo(1);


        // medium con 30 de maná
        // medium2 con 15

        angel1.conectarConMedium(Optional.of(medium)); // 60 de conex + 6 = 66
        demonio1.conectarConMedium(Optional.of(medium2)); // 50 de conex + 3 = 53

        Optional<Medium> optionalMedium = Optional.of(medium2);

        medium.exorcizarA(optionalMedium);

        assertEquals(20,demonio1.getNivelDeConexion());
        assertEquals(66,angel1.getNivelDeConexion()); // se mantiene

    }

    @Test
    void alExorcizarAUnMediumElAngelPierdeYDisminuyeNivelDeConexionDelAngel(){
        fakeRandomStrategy.addValorFijo(1);
        fakeRandomStrategy.addValorFijo(100);


        // medium con 30 de maná
        // medium2 con 15

        angel1.conectarConMedium(Optional.of(medium)); // 60 de conex + 6 = 66
        demonio1.conectarConMedium(Optional.of(medium2)); // 50 de conex + 3 = 53

        Optional<Medium> optionalMedium = Optional.of(medium2);

        medium.exorcizarA(optionalMedium);

        assertEquals(53,demonio1.getNivelDeConexion());
        assertEquals(61,angel1.getNivelDeConexion());


    }

    @Test
    void alExorcizarAUnMediumElPrimerAngelVenceAlPrimerDemonioYElSegundoAngelPierdeContraElSegundoDemonio(){

        angel2 = new Angel(20, "Ariel",ubicacion);

        demonio2 = new Demonio(0, "Dismas",ubicacion);

        fakeRandomStrategy.addValorFijo(10);
        fakeRandomStrategy.addValorFijo(1);
        fakeRandomStrategy.addValorFijo(1);
        fakeRandomStrategy.addValorFijo(100);

        // medium con 30 de maná
        // medium2 con 15

        angel1.conectarConMedium(Optional.of(medium)); // 60 de conex + 6 = 66
        angel2.conectarConMedium(Optional.of(medium)); // 20 de conex + 6 = 26

        demonio1.conectarConMedium(Optional.of(medium2)); // 50 de conex + 3 = 53
        demonio2.conectarConMedium(Optional.of(medium2)); // 0 de conex + 3 = 3

        Optional<Medium> optionalMedium = Optional.of(medium2);

        medium.exorcizarA(optionalMedium);

        assertEquals(66,angel1.getNivelDeConexion());
        assertEquals(20,demonio1.getNivelDeConexion());

        assertEquals(21,angel2.getNivelDeConexion());
        assertEquals(3,demonio2.getNivelDeConexion());



    }

    @Test
    void alIntentarExorcizarSinAngelesLanzaUnError(){
        Optional<Medium> optionalMedium = Optional.of(medium2);

        assertThrows(ExorcistaSinAngelesException.class, () -> {
            medium.exorcizarA(optionalMedium);
        });
    }

    @Test
    void seVerificaQueCuandoUnMediumCambiaLaUbicacionSusEspiritusTambienCambiaLaUbicacion(){
        Ubicacion ubicacion1 = new Ubicacion("Magical");
        angel1.conectarConMedium(Optional.of(medium));
        demonio1.conectarConMedium(Optional.of(medium));
        this.medium.setUbicacion(ubicacion1);

        assertEquals(this.angel1.getUbicacion().getNombre(), ubicacion1.getNombre());
        assertEquals(this.demonio1.getUbicacion().getNombre(), ubicacion1.getNombre());
    }

    @AfterEach
    void cleanUp() {
        fakeRandomStrategy.cleanUp();
    }
}