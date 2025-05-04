package ar.edu.unq.epersgeist.servicios.impl;
import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.testUtils.TestService;
import ar.edu.unq.epersgeist.testUtils.FakeRandomStrategyImpl;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.RandomGenerator.RandomGenerator;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.ExorcistaSinAngelesException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MediumServiceImplTest {

    @Autowired
    private MediumServiceImpl mediumService;
    @Autowired
    private TestService testService;
    @Autowired
    private EspirituServiceImpl espirituService;
    @Autowired
    private UbicacionServiceImpl ubicacionService;

    private Medium medium;
    private Medium medium2;

    private Angel angelDebil;
    private Angel angelFuerte;

    private Demonio demonioDebil;
    private Demonio demonioFuerte;


    private Ubicacion ubicacion;
    private Ubicacion ubicacion2;





    private FakeRandomStrategyImpl fakeRandomStrategy;



    @BeforeEach
    void crearMedium(){
        this.ubicacion = new Ubicacion("Bernal");
        this.ubicacion2 = new Ubicacion("Quilmes");

        ubicacionService.crear(ubicacion);
        ubicacionService.crear(ubicacion2);
        medium = new Medium("Max",100,50, this.ubicacion);
        medium2 = new Medium("Pepe",100,0, this.ubicacion);

        angelDebil = new Angel(10,"gabriel",this.ubicacion);
        angelFuerte = new Angel(60,"Rafael",this.ubicacion);

        demonioDebil = new Demonio(5,"Rufi",this.ubicacion);
        demonioFuerte = new Demonio(50,"Adam",this.ubicacion);

        fakeRandomStrategy = new FakeRandomStrategyImpl();
        RandomGenerator.getInstance().setStrategy(fakeRandomStrategy);

    }
    @Test
    void seRecuperanTodosLosMediumsGuardados(){
        mediumService.crear(medium);
        mediumService.crear(medium2);

        var mediums = mediumService.recuperarTodos();

        assertEquals(2, mediums.size());
    }

    @Test
    void alRecuperarTodosLosMediumsEstandoLaBaseDeDatosVaciaRetornaListaVacia(){

        var mediums = mediumService.recuperarTodos();

        assertTrue(mediums.isEmpty());
    }

    @Test
    void seEliminaUnMediumDejaDeEstarPersistiendo(){
        mediumService.crear(medium);

        mediumService.eliminar(medium.getId());

        assertTrue(mediumService.recuperar(medium.getId()).isEmpty());
    }

    @Test
    void alEliminarUnMediumNoGuardadoNoHaceNada() {

        assertDoesNotThrow(() -> mediumService.eliminar(15L));
    }

    @Test
    void alCrearYRecuperarMediumSeObtienenObjetosSimilares() {
        mediumService.crear(medium);

        Medium mediumRecuperado =  mediumService.recuperar(medium.getId()).get();

        assertEquals(medium.getId(), mediumRecuperado.getId());
        assertEquals(medium.getMana(),mediumRecuperado.getMana());
        assertEquals(medium.getManaMax(), mediumRecuperado.getManaMax());
        assertEquals(medium.getNombre(), mediumRecuperado.getNombre());
    }

    @Test
    void alCrearUnMediumSeLeAgregaUnId() {

        assertNull(medium.getId());

        mediumService.crear(medium);

        assertNotNull(medium.getId());
    }



    @Test
    void seActualizanUnMediumYAlRecuperarloLosCambiosSeVenReflejados() {
        mediumService.crear(medium);
        medium.setNombre("nombreModificado");

        mediumService.actualizar(medium);
        Medium recuperado = mediumService.recuperar(medium.getId()).get();

        assertEquals("nombreModificado", recuperado.getNombre());
    }

    @Test
    void seActualizaUnMediumInexistenteYNoSeLanzaNingunaExcepcion() {
        medium.setId(-1L);

        assertThrows(EntidadInexistenteException.class, () -> {
            mediumService.actualizar(medium);
        });
    }



    @Test
    void alRecuperarUnMediumQueNoEstaGuardadoSeRetornaEmpty() {
        Long idInexistente = 333L;

        assertTrue(mediumService.recuperar(idInexistente).isEmpty());

    }

    @Test
    void seHaceDescansarAUnMediumRecuperandoManaYSuEspirituRecuperaConexion(){

        mediumService.crear(medium);
        espirituService.crear(angelDebil);

        espirituService.conectar(angelDebil.getId(), medium.getId());


        mediumService.descansar(medium.getId());

        Medium recuperado = mediumService.recuperar(medium.getId()).get();
        Espiritu recuperado1 = espirituService.recuperar(angelDebil.getId()).get();

        assertEquals(65,recuperado.getMana());

        assertEquals(25, recuperado1.getNivelDeConexion());
    }

    @Test
    void seIntentaHacerDescansarAUnMediumQueNoFuePersistidoYLanzaExcepcion(){
        assertThrows(EntidadInexistenteException.class, () -> {
            mediumService.descansar(-1L);
        });
    }

    @Test
    void alConectarConUnEspirituElMediumAgregaEseEspirituALosSuyos() {
        espirituService.crear(angelDebil);
        mediumService.crear(medium);
        espirituService.conectar(angelDebil.getId(), medium.getId());
        List<Espiritu> espiritus = mediumService.espiritus(medium.getId());
        assertEquals(1,espiritus.size());

    }


    @Test
    void seRecuperanTodosLosEspiritusConLosQueEstaConectadoUnMedium() {
        mediumService.crear(medium);
        espirituService.crear(angelDebil);
        espirituService.crear(demonioDebil);

        espirituService.conectar(angelDebil.getId(), medium.getId());
        espirituService.conectar(demonioDebil.getId(), medium.getId());


        List<Espiritu> espirituRecuperados = mediumService.espiritus(medium.getId());

        assertEquals(2, espirituRecuperados.size());

    }

    @Test
    void seIntentaRecuperarLosEspiritusDeUnMediumQueNoExiseEntoncesSeDevuelveUnaExepcion(){

        assertThrows(EntidadInexistenteException.class, () -> {
            mediumService.espiritus(-1L);
        });
    }

    @Test
    void seIntentaRecuperarTodosLosEspiritusDeUnMediumQueNoTieneEspiritusEntoncesSeDevuelveUnaListaVacia() {
        mediumService.crear(medium);

        List<Espiritu> espiritus = mediumService.espiritus(medium.getId());

        assertTrue(espiritus.isEmpty());
    }

    @Test
    void alExorcizarAUnMediumElAngelGanaYDisminuyeNivelDeConexionDelDemonio() {

        fakeRandomStrategy.addValorFijo(10);
        fakeRandomStrategy.addValorFijo(1);


        mediumService.crear(medium);
        mediumService.crear(medium2);

        espirituService.crear(angelFuerte);
        espirituService.crear(demonioFuerte);

        espirituService.conectar(angelFuerte.getId(),medium.getId());

        espirituService.conectar(demonioFuerte.getId(),medium2.getId());

        mediumService.exorcisar(medium.getId(),medium2.getId());

        Espiritu demonioRecuperado = espirituService.recuperar(demonioFuerte.getId()).get();
        Espiritu angelRecuperado = espirituService.recuperar(angelFuerte.getId()).get();

        assertEquals(15,demonioRecuperado.getNivelDeConexion());
        assertEquals(70,angelRecuperado.getNivelDeConexion());

    }

    @Test
    void alExorcizarAUnMediumElAngelPierdeYDisminuyeNivelDeConexionDelAngel(){

        fakeRandomStrategy.addValorFijo(1);
        fakeRandomStrategy.addValorFijo(100);


        mediumService.crear(medium);
        mediumService.crear(medium2);

        espirituService.crear(angelDebil);
        espirituService.crear(demonioFuerte);

        espirituService.conectar(angelDebil.getId(),medium.getId());

        espirituService.conectar(demonioFuerte.getId(),medium2.getId());

        mediumService.exorcisar(medium.getId(),medium2.getId());

        Espiritu demonioRecuperado = espirituService.recuperar(demonioFuerte.getId()).get();
        Espiritu angelRecuperado = espirituService.recuperar(angelDebil.getId()).get();

        assertEquals(50,demonioRecuperado.getNivelDeConexion());
        assertEquals(15,angelRecuperado.getNivelDeConexion());


    }

    @Test
    void alExorcizarAUnMediumElPrimerAngelVenceAlPrimerDemonioYElSegundoAngelPierdeContraElSegundoDemonio(){

        fakeRandomStrategy.addValorFijo(10);
        fakeRandomStrategy.addValorFijo(1);
        fakeRandomStrategy.addValorFijo(1);
        fakeRandomStrategy.addValorFijo(100);


        mediumService.crear(medium);
        mediumService.crear(medium2);

        espirituService.crear(angelFuerte);
        espirituService.crear(angelDebil);

        espirituService.crear(demonioFuerte);
        espirituService.crear(demonioDebil);

        espirituService.conectar(angelFuerte.getId(),medium.getId());
        espirituService.conectar(angelDebil.getId(),medium.getId());

        espirituService.conectar(demonioDebil.getId(),medium2.getId());
        espirituService.conectar(demonioFuerte.getId(),medium2.getId());

        mediumService.exorcisar(medium.getId(),medium2.getId());


        Espiritu angelFuerteRecuperado = espirituService.recuperar(angelFuerte.getId()).get();
        Espiritu angelDebilRecuperado = espirituService.recuperar(angelDebil.getId()).get();
        Espiritu demonioFuerteRecuperado = espirituService.recuperar(demonioFuerte.getId()).get();
        Espiritu demonioDebilRecuperado = espirituService.recuperar(demonioDebil.getId()).get();
        List<Espiritu> espiritusDeMedium2 = mediumService.espiritus(medium2.getId());

        assertEquals(0,demonioDebilRecuperado.getNivelDeConexion());
        assertEquals(50,demonioFuerteRecuperado.getNivelDeConexion());

        assertEquals(15,angelDebilRecuperado.getNivelDeConexion());
        assertEquals(70,angelFuerteRecuperado.getNivelDeConexion());

        assertEquals(1, espiritusDeMedium2.size());
        assertTrue(demonioDebilRecuperado.estaLibre());
    }

    @Test
    void alIntentarExorcizarSinAngelesLanzaUnError(){
        mediumService.crear(medium);
        mediumService.crear(medium2);

        assertThrows(ExorcistaSinAngelesException.class, () -> {
            mediumService.exorcisar(medium.getId(),medium2.getId());
        });
    }

    @Test
    void seIntentaExorcizarAUnMediumQueNoFuePersistidoYLanzaUnaExepcion(){

        mediumService.crear(medium);
        espirituService.crear(angelDebil);

        espirituService.conectar(angelDebil.getId(),medium.getId());

        Integer angelDebilNivelDeConex = angelDebil.getNivelDeConexion();

        assertThrows(EntidadInexistenteException.class, () -> {
            mediumService.exorcisar(medium.getId(),-1L);
        });

        assertEquals(angelDebilNivelDeConex, angelDebil.getNivelDeConexion());
    }

    @Test
    void unMediumNoPersistidoIntentaExorcizarAOtroEntoncesNoPasaNada(){

        mediumService.crear(medium2);
        espirituService.crear(demonioDebil);

        espirituService.conectar(demonioDebil.getId(),medium2.getId());

        Integer demonioDebilNivelDeConex = demonioDebil.getNivelDeConexion();

        assertThrows(EntidadInexistenteException.class, () -> {
            mediumService.exorcisar(-1L,medium2.getId());
        });

        assertEquals(demonioDebilNivelDeConex, demonioDebil.getNivelDeConexion());
    }

    @Test
    void unMediumPersistidoInvocaAUnEspirituQueNoEstaLibreYSuUbicacionCambia() {

        mediumService.crear(medium);
        espirituService.crear(angelDebil);
        int manaInicial = medium.getMana();

        //seteo una ubicacion al angel, que es diferente a la del medium
        angelDebil.setUbicacion(ubicacion2);
        espirituService.actualizar(angelDebil);

        mediumService.invocar(medium.getId(), angelDebil.getId());

        Medium mediumRecuperado = mediumService.recuperar(medium.getId()).get();
        Espiritu espirituRecuperado = espirituService.recuperar(angelDebil.getId()).get();

        Ubicacion ubicacionMedium =  ubicacionService.recuperar(mediumRecuperado.getUbicacion().getId()).get();
        Ubicacion ubicacionEspiritu = ubicacionService.recuperar(espirituRecuperado.getUbicacion().getId()).get();

        assertEquals(ubicacionMedium.getId(), ubicacionEspiritu.getId());
        assertEquals( manaInicial - 10 , mediumRecuperado.getMana());
    }


    @Test
    void unMediumIntentainvocarSinManaSuficienteEntoncesSuManaYLaUbicacionDelEspirituNoCambian() {
        mediumService.crear(medium);
        medium.setMana(5);
        int manaInicial = medium.getMana();

        espirituService.crear(angelDebil);

        //seteo una ubicacion al angel, que es diferente a la de el medium
        angelDebil.setUbicacion(ubicacion2);
        espirituService.actualizar(angelDebil);

        mediumService.actualizar(medium);

        mediumService.invocar(medium.getId(), angelDebil.getId());

        Medium mediumRecuperado = mediumService.recuperar(medium.getId()).get();
        Espiritu espirituRecuperado = espirituService.recuperar(angelDebil.getId()).get();

        Ubicacion ubicacionMedium =  ubicacionService.recuperar(mediumRecuperado.getUbicacion().getId()).get();
        Ubicacion ubicacionEspiritu = ubicacionService.recuperar(espirituRecuperado.getUbicacion().getId()).get();

        assertNotEquals(ubicacionMedium.getId(), ubicacionEspiritu.getId());
        assertEquals( manaInicial , mediumRecuperado.getMana());

    }

    @Test
    void alRecibirMuchoDanhoElEspirituSeDesviculaDelMedium() {

        fakeRandomStrategy.addValorFijo(1);
        fakeRandomStrategy.addValorFijo(100);


        mediumService.crear(medium);
        mediumService.crear(medium2);

        espirituService.crear(angelDebil);
        espirituService.crear(demonioFuerte);

        espirituService.conectar(angelDebil.getId(),medium.getId());

        espirituService.conectar(demonioFuerte.getId(),medium2.getId());

        mediumService.exorcisar(medium.getId(),medium2.getId());

        assertTrue(medium.getAngeles().isEmpty());


    }


    @AfterEach
    void eliminarModelo() {
        testService.eliminarEspiritus();
        testService.eliminarMediums();
        testService.eliminarUbicaciones();
        fakeRandomStrategy.cleanUp();
    }


}
