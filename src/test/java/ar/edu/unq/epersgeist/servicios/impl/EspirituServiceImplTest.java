package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.testUtils.TestService;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.EspirituLibreException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionDistintaException;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EspirituServiceImplTest {

    @Autowired
    private EspirituService espirituService;

    @Autowired
    private MediumService mediumService;

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private TestService testService;

    private Espiritu angel1;
    private Medium medium;
    private Espiritu angel2;
    private Espiritu angel3;

    private Demonio demonio1;
    private Demonio demonio2;
    private Demonio demonio3;

    private Ubicacion ubicacion;



    @BeforeEach
    void crearInstancias() {
        this.ubicacion = new Santuario("Bernal", 50);
        angel1 = new Angel(10,"Angel1", this.ubicacion);
        angel2 = new Angel(10,"Angel2", this.ubicacion);
        angel3 = new Angel(10,"Angel3", this.ubicacion);
        demonio1 = new Demonio(
                10,
                "Pepito", this.ubicacion
        );
        demonio2 = new Demonio(
                90,
                "Diavolo", this.ubicacion

        );
        demonio3 = new Demonio(
                50,
                "Sebastián", this.ubicacion

        );
        medium = new Medium("Brujita", 100, 50, this.ubicacion);

    }

//    @Test
//    void noSePuedenCrearAngelesODemonioSinNombres() { // TODO implementar esto
//        Ubicacion quilmes = new Santuario("quilmes", 50);
//        ubicacionService.crear(quilmes);
//        Demonio demonioSinNombre = new Demonio(10, "", quilmes);
//        Angel angelSinNombre = new Angel(10, "", quilmes);
//        assertThrows(ParametroIncorrectoException.class, () -> espirituService.crear(angelSinNombre));
//        assertThrows(ParametroIncorrectoException.class, () -> espirituService.crear(demonioSinNombre));
//    }

    @Test
    void alRecuperarUnIdInexistenteNoTraeNada(){

        Long idInexistente = 50L;
        Long idInexistente2 = -50L;
        espirituService.recuperar(idInexistente);
        assertFalse(espirituService.recuperar(idInexistente).isPresent());
        assertFalse(espirituService.recuperar(idInexistente2).isPresent());
    }

    @Test
    void alConectarseConUnMediumEnUnaUbicacionDiferenteDaErrorUbicacionDistintaException() {
        Ubicacion quilmes = new Santuario("Quilmes", 50);
        Ubicacion bernal = new Santuario("Bernal", 50);
        ubicacionService.crear(quilmes);
        ubicacionService.crear(bernal);
        Medium mediumDeBernal = new Medium("Brujita", 100, 50, bernal);
        mediumService.crear(mediumDeBernal);
        Angel espirituDeQuilmes = new Angel(10, "AngelDeQuilmes", quilmes);
        espirituService.crear(espirituDeQuilmes);
        assertThrows(UbicacionDistintaException.class, ()->espirituService.conectar(espirituDeQuilmes.getId(), mediumDeBernal.getId()));
    }

    @Test
    void permiteConectarseAlMediunEnLaMismaUbicacion() {
        Ubicacion bernal = new Santuario("Bernal", 50);
        ubicacionService.crear(bernal);
        Medium mediumDeBernal = new Medium("Brujita", 100, 50, bernal);
        mediumService.crear(mediumDeBernal);
        Angel espirituDeBernal = new Angel(10, "AngelDeQuilmes", bernal);
        espirituService.crear(espirituDeBernal);
        assertDoesNotThrow(()->espirituService.conectar(espirituDeBernal.getId(), mediumDeBernal.getId()));

    }

    @Test
    void SiUnEspirituaNoEstaLibreDaErrorEspirituLibreException(){
        ubicacionService.crear(this.ubicacion);
        Medium mediumConEspiritu = new Medium("Brujita", 100, 50, this.ubicacion);
        Medium mediumLibre = new Medium("JonWick", 100, 50, this.ubicacion);
        mediumService.crear(mediumLibre);
        mediumService.crear(mediumConEspiritu);

        Angel espirituConectado = new Angel(10, "AngelDeQuilmes", this.ubicacion);
        espirituService.crear(espirituConectado);
        espirituService.conectar(espirituConectado.getId(), mediumConEspiritu.getId());

        assertThrows(EspirituLibreException.class, ()->espirituService.conectar(espirituConectado.getId(), mediumLibre.getId()));

    }


    @Test
    void alConectarSiElMediumNoExisteLanzaError(){
        ubicacionService.crear(this.ubicacion);

        int conexionAngel = 30;
        Espiritu angelRafael = new Angel(conexionAngel, "Rafael", this.ubicacion);
        espirituService.crear(angelRafael);

        assertThrows(EntidadInexistenteException.class, () -> espirituService.conectar(angelRafael.getId() , -1L));

    }

    @Test
    void alConectarConUnEspirituSeFortaleceElNivelDeConexion20delMedium(){
        ubicacionService.crear(this.ubicacion);
        int manaMedium = 50;
        Medium medium = new Medium("JonWick", 100, manaMedium, this.ubicacion);
        mediumService.crear(medium);
        int conexionAngel = 30;
        Espiritu angelRafael = new Angel(conexionAngel, "Rafael", this.ubicacion);
        espirituService.crear(angelRafael);

        int conexionBuscada = ((int) Math.round(manaMedium* 0.20) ) + conexionAngel;
        espirituService.conectar(angelRafael.getId() , medium.getId());
        Optional<Espiritu> angelRecuperado = espirituService.recuperar(angelRafael.getId());
        assertEquals(angelRecuperado.get().getNivelDeConexion(), conexionBuscada );
    }

    @Test
    void sePuedenRecuperarTodosLosEspiritusCreados(){
        this.ubicacionService.crear(this.ubicacion);
        espirituService.crear(angel1);
        espirituService.crear(angel2);
        espirituService.crear(angel3);
        List<Espiritu> espiritusRecuperados = espirituService.recuperarTodos();
        assertEquals(3, espiritusRecuperados.size());
    }

    @Test
    void alEliminarUnEspirituDejaDeEstarPersistido() {
        this.ubicacionService.crear(this.ubicacion);
        espirituService.crear(angel1);
        espirituService.eliminar(angel1.getId());
        Optional<Espiritu> espirituInexistente = espirituService.recuperar(angel1.getId());
        assertFalse(espirituInexistente.isPresent());
    }

    @Test
    void alEliminarUnEspirituQueNoFueCreadoNoHaceNada() { // está de más testear esto?
        this.ubicacionService.crear(this.ubicacion);
        espirituService.crear(angel1);
        assertDoesNotThrow( () -> {
            espirituService.eliminar(angel1.getId());
                }
        );
    }

    @Test
    void alActualizarUnEspirituSeCambiaCorrectamente() {
        this.ubicacionService.crear(this.ubicacion);
        espirituService.crear(angel1);
        angel1.setNombre("Nuevo Nombre");
        espirituService.actualizar(angel1);

        var espirituRecuperado = espirituService.recuperar(angel1.getId()).get();
        assertEquals("Nuevo Nombre", espirituRecuperado.getNombre());

    }

    @Test
    void alCrearYLuegoRecuperarSeObtieneObjetosSimilares() {

        this.ubicacionService.crear(this.ubicacion);
        espirituService.crear(angel1);
        var otroEspiritu = espirituService.recuperar(angel1.getId()).get();
        assertEquals(angel1.getNombre(), otroEspiritu.getNombre());
        assertEquals(angel1.getNivelDeConexion(), otroEspiritu.getNivelDeConexion());
        assertEquals(angel1.getId(), otroEspiritu.getId());
        Assertions.assertNotEquals(angel1, otroEspiritu);
    }

    @Test
    void seAgregaElIdAlEspirituAlCrearUnEspiritu() {
        this.ubicacionService.crear(this.ubicacion);
        assertNull(angel1.getId());
        espirituService.crear(angel1);
        assertNotNull(angel1.getId());
    }

    @Test
    void alRecuperarTodosEspíritusPeroNoHayNingunoDevuelveListaVacía() {
        assertTrue(espirituService.recuperarTodos().isEmpty());
    }

    @Test
    void alConectarUnEspirituSeGuardaEnSuRespectivaListaDelMediumAumentandoSuConexionYSePersiste() {

        this.ubicacionService.crear(this.ubicacion);
        espirituService.crear(angel1); // 10 de conexión
        espirituService.crear(demonio1); // 10 de conexión
        mediumService.crear(medium); // 50 de maná

        espirituService.conectar(demonio1.getId(), medium.getId()); // aumenta en 10 la conex, 20% de 50 es 10
        espirituService.conectar(angel1.getId(), medium.getId()); // aumenta en 10 la conex, 20% de 50 es 10

        Espiritu angelRecuperado = espirituService.recuperar(angel1.getId()).get();
        Espiritu demonioRecuperado = espirituService.recuperar(demonio1.getId()).get();

        List<Espiritu> espiritusRecuperados = mediumService.espiritus(medium.getId());

        assertEquals(20, angelRecuperado.getNivelDeConexion());
        assertEquals(20, demonioRecuperado.getNivelDeConexion());
        assertEquals(2, espiritusRecuperados.size());

    }

    @Test
    void alIntentarConectarConUnEspirituQueNoExisteNoHaceNada(){
        this.ubicacionService.crear(this.ubicacion);
        mediumService.crear(medium);

        espirituService.conectar(-1L, medium.getId());

        List<Espiritu> espiritus = mediumService.espiritus(medium.getId());

        assertTrue(espiritus.isEmpty());
    }

    @Test
    void alIntentarConectarConUnMediumQueNoExisteLanzaError(){
        this.ubicacionService.crear(this.ubicacion);
        espirituService.crear(angel1);

        assertThrows(EntidadInexistenteException.class, () -> {
            espirituService.conectar(angel1.getId(), -1L);
        });
    }

    @Test
    void alRecuperarTodosLosEspiritusDemoniosNoDebenHaberDeOtroTipo() {
        this.ubicacionService.crear(this.ubicacion);
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(angel1); //es un angel


        // hay 3 en total, 2 son ángeles y 1 es demonio

        List<Demonio> demonios = espirituService.espiritusDemoniacos(Sort.Direction.ASC, 0, 2);

        assertEquals(2, demonios.size());

    }

    @Test
    void alRecuperarTodosLosDemoniosPeroNoHaberDevuelveListaVacía() {
        this.ubicacionService.crear(this.ubicacion);
        espirituService.crear(angel1);

        List<Demonio> demonios = espirituService.espiritusDemoniacos(Sort.Direction.DESC, 0, 2);// agarro sólo los demonios

        assertTrue(demonios.isEmpty());

    }

    @Test
    void alRecuperarTodosLosDemoniosEspecificandoOrdenDescendenteDebenEstarEnOrdenDeNivelDeConexiónDescendente() {
        this.ubicacionService.crear(this.ubicacion);
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        List<Long> expected = Arrays.asList(demonio2.getId(), demonio3.getId(), demonio1.getId());
        //  2 3 1

        List<Long> demoniosIds =
                espirituService.espiritusDemoniacos(Sort.Direction.DESC, 0, 3)
                .stream().map(Espiritu::getId).toList();


        assertEquals(expected, demoniosIds);




    }

    @Test
    void alRecuperarTodosLosDemoniosEspecificandoOrdenAscendenteDebenEstarEnOrdenDeNivelDeConexiónAscendente() {
        this.ubicacionService.crear(this.ubicacion);
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        List<Long> expected = Arrays.asList(demonio1.getId(), demonio3.getId(), demonio2.getId());
        //  1 3 2

        List<Long> demoniosIds = espirituService.espiritusDemoniacos(Sort.Direction.ASC, 0, 3)
                                    .stream().map(Espiritu::getId).toList();

        assertEquals(expected, demoniosIds);

    }

    @Test
    void alRecuperarATodosLosDemoniosSiSeUsanValoresNegativosParaPáginaDebeFallar() {

        assertThrows(ParametroIncorrectoException.class, () -> espirituService.espiritusDemoniacos(Sort.Direction.ASC, -1, 3));

    }

    @Test
    void alRecuperarATodosLosDemoniosSiSeUsanValoresNegativosParaCantidadPorPáginaDebeFallar() {

        assertThrows(ParametroIncorrectoException.class, () -> espirituService.espiritusDemoniacos(Sort.Direction.ASC, 0, -1));

    }

    @Test
    void alRecuperarATodosLosDemoniosSiSeUsa0ParaCantidadPorPáginaDebeFallar() {

        assertThrows(ParametroIncorrectoException.class, () -> espirituService.espiritusDemoniacos(Sort.Direction.ASC, 0, 0));

    }

    @Test
    void alRecuperarMenosDemoniosDeLosTotalesDebeTraerLaCantidadEspecificada() {
        this.ubicacionService.crear(this.ubicacion);
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        List<Demonio> demonios = espirituService.espiritusDemoniacos(Sort.Direction.ASC, 0, 2);

        assertEquals(2, demonios.size());

    }

    @Test
    void alRecuperarDemoniosDeUnaPáginaMásAvanzadaDondeHayDemoniosDebeTraerLosDeEsaPáginaSolamente() {
        this.ubicacionService.crear(this.ubicacion);
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        List<Demonio> demonios = espirituService.espiritusDemoniacos(Sort.Direction.ASC, 1, 2);

        //dem 1 -> pag 0
        //dem 2 -> pag 0
        //dem 3 -> pag 1

        assertEquals(1, demonios.size());

    }

    @Test
    void alRecuperarDemoniosDeUnaPáginaMásAvanzadaDondeNoHayDemoniosNoTraeNada() {
        this.ubicacionService.crear(this.ubicacion);
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        List<Demonio> demonios = espirituService.espiritusDemoniacos(Sort.Direction.ASC, 1, 5);

        //dem 1 -> pag 0
        //dem 2 -> pag 0
        //dem 3 -> pag 0
        // no hay más

        assertTrue(demonios.isEmpty());

    }

    @Test
    void alRecuperarDemoniosDeUnaPáginaDondeNoHayMismaCantidadDeDemoniosQueLaCantidadPorPáginaTraeTodosLosQueHayaEnEsaPágina() {
        this.ubicacionService.crear(this.ubicacion);
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        List<Demonio> demonios = espirituService.espiritusDemoniacos(Sort.Direction.ASC, 1, 2);

        //dem 1 -> pag 0
        //dem 2 -> pag 0
        //dem 3 -> pag 1
        // no hay más

        assertEquals(1, demonios.size());

    }

    @Test
    void alRecuperarTodosLosDemoniosOrdenDescendenteDebenEstarEnOrdenDeNivelDeConexiónDescendenteEntreTodosLosDemonios() {
        this.ubicacionService.crear(this.ubicacion);
        Demonio demonio4 = new Demonio(
                99,
                "Enrique", this.ubicacion

        );
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);
        espirituService.crear(demonio4);

        // el orden por inserción es: 1, 2, 3, 4

        List<Long> demoniosIds = espirituService.espiritusDemoniacos(Sort.Direction.DESC, 0, 3)
                                    .stream().map((Demonio::getId)).toList();

        List<Long> demoniosIdsEsperados = Arrays.asList(demonio4.getId(), demonio2.getId(), demonio3.getId());

        // 4 2 3

        assertEquals(demoniosIdsEsperados, demoniosIds);

    }

    @Test
    void alRecuperarTodosLosDemoniosDeUnTipoEspecificandoOrdenAscendenteDebenEstarEnOrdenDeNivelDeConexiónAscendenteEntreTodosLosDemonios() {
        this.ubicacionService.crear(this.ubicacion);
        Demonio demonio4 = new Demonio(
                2,
                "Enrique", this.ubicacion

        );
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);
        espirituService.crear(demonio4);

        // el orden por inserción es: 1, 2, 3, 4

        List<Long> demoniosIds = espirituService.espiritusDemoniacos(Sort.Direction.ASC, 0, 3)
                                    .stream().map((Demonio::getId)).toList();

        List<Long> demoniosIdsEsperados = Arrays.asList(demonio4.getId(), demonio1.getId(), demonio3.getId());

        // 4 1 3

        assertEquals(demoniosIdsEsperados, demoniosIds);

    }


    @AfterEach
    void eliminarModelo() {
        testService.eliminarEspiritus();
        testService.eliminarMediums();
        testService.eliminarUbicaciones();
    }
}