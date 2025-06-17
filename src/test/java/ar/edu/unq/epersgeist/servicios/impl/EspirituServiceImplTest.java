package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.geo.Coordenada;
import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;
import ar.edu.unq.epersgeist.modelo.exception.espiritu.EspirituDominadoException;
import ar.edu.unq.epersgeist.modelo.exception.espiritu.EspirituMuyLejanoException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.testUtils.TestService;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.espiritu.EspirituLibreException;
import ar.edu.unq.epersgeist.modelo.exception.ubicacion.UbicacionDistintaException;
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
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

//TODO borrar ubicaciones de los constructores de espiritus y mediums cuando se haga el mover con la verificacion del lugar
    @BeforeEach
    void crearInstancias() {
        this.ubicacion = new Santuario("Bernal", 50,Arrays.asList(
                new Coordenada(0, 0),
                new Coordenada(1, 0),
                new Coordenada(1, 1),
                new Coordenada(0, 1),
                new Coordenada(0, 0)));

        angel1 = new Angel(10,"Angel1",new Coordenada(0.5,0.5));
        angel2 = new Angel(10,"Angel2",new Coordenada(1,1));
        angel3 = new Angel(10,"Angel3",new Coordenada(1,1));

        demonio1 = new Demonio(
                10,
                "Pepito",new Coordenada(0.5,0.5)
        );
        demonio2 = new Demonio(
                90,
                "Diavolo",new Coordenada(0.520,0.520)

        );
        demonio3 = new Demonio(
                50,
                "Sebastián",new Coordenada(0.550,0.550)

        );
        medium = new Medium("Brujita", 100, 50, new Coordenada(1,1));

    }


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
        Ubicacion quilmes = new Santuario("Quilmes", 50,Arrays.asList(
                new Coordenada(2, 2),
                new Coordenada(3, 2),
                new Coordenada(3, 3),
                new Coordenada(2, 3),
                new Coordenada(2, 2)));

        Ubicacion bernal = ubicacion;
        ubicacionService.crear(quilmes);
        ubicacionService.crear(bernal);
        Medium mediumDeBernal = new Medium("Brujita", 100, 50, new Coordenada(1,1));
        mediumService.crear(mediumDeBernal);
        Angel espirituDeQuilmes = new Angel(10, "AngelDeQuilmes",new Coordenada(2.5, 2.5));
        espirituService.crear(espirituDeQuilmes);
        assertThrows(UbicacionDistintaException.class, ()->espirituService.conectar(espirituDeQuilmes.getId(), mediumDeBernal.getId()));
    }

    @Test
    void permiteConectarseAlMediunEnLaMismaUbicacion() {
        Ubicacion bernal = ubicacion;
        ubicacionService.crear(bernal);
        Medium mediumDeBernal = new Medium("Brujita", 100, 50, new Coordenada(1,1));
        mediumService.crear(mediumDeBernal);
        Angel espirituDeBernal = new Angel(10, "AngelDeQuilmes",new Coordenada(1,1));
        espirituService.crear(espirituDeBernal);
        assertDoesNotThrow(()->espirituService.conectar(espirituDeBernal.getId(), mediumDeBernal.getId()));

    }

    @Test
    void SiUnEspiritusNoEstaLibreDaErrorEspirituLibreException(){
        ubicacionService.crear(this.ubicacion);
        Medium mediumConEspiritu = new Medium("Brujita", 100, 50, new Coordenada(1,1));
        Medium mediumLibre = new Medium("JonWick", 100, 50, new Coordenada(1,1));
        mediumService.crear(mediumLibre);
        mediumService.crear(mediumConEspiritu);

        Angel espirituConectado = new Angel(10, "AngelDeQuilmes",new Coordenada(1,1));
        espirituService.crear(espirituConectado);
        espirituService.conectar(espirituConectado.getId(), mediumConEspiritu.getId());

        assertThrows(EspirituLibreException.class, ()->espirituService.conectar(espirituConectado.getId(), mediumLibre.getId()));

    }


    @Test
    void alConectarSiElMediumNoExisteLanzaError(){
        ubicacionService.crear(this.ubicacion);

        int conexionAngel = 30;
        Espiritu angelRafael = new Angel(conexionAngel, "Rafael",new Coordenada(1,1));
        espirituService.crear(angelRafael);

        assertThrows(EntidadInexistenteException.class, () -> espirituService.conectar(angelRafael.getId() , -1L));

    }

    @Test
    void alConectarConUnEspirituSeFortaleceElNivelDeConexion20delMedium(){
        ubicacionService.crear(this.ubicacion);
        int manaMedium = 50;
        Medium medium = new Medium("JonWick", 100, manaMedium, new Coordenada(1,1));
        mediumService.crear(medium);
        int conexionAngel = 30;
        Espiritu angelRafael = new Angel(conexionAngel, "Rafael",new Coordenada(1,1));
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
    void alEliminarUnEspirituQueNoFueCreadoNoHaceNada() {
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
                "Enrique",new Coordenada(1,1)

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
                "Enrique",new Coordenada(1,1)

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

    @Test
    void noSePuedeDominarAUnEspirituQueNoExiste() {
        ubicacionService.crear(ubicacion);
        espirituService.crear(demonio1);

        espirituService.dominar(demonio1.getId(), -1L);
        Espiritu dominado = espirituService.recuperar(demonio1.getId()).get();

        assertNull(dominado.getEspirituDominante());
    }


    @Test
    void noPuedeOcurrirUnaDominacionEntreElMismoEspiritu() {
        ubicacionService.crear(ubicacion);
        espirituService.crear(demonio2);

        espirituService.dominar(demonio2.getId(), demonio2.getId());
        Espiritu dominado = espirituService.recuperar(demonio2.getId()).get();

        assertNull(dominado.getEspirituDominante());
    }

    @Test
    void noPuedeOcurrirUnaDominacionAUnEspirituNoLibre() {
        ubicacionService.crear(ubicacion);

        mediumService.crear(medium);

        demonio2.setNivelDeConexion(10);
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.conectar(demonio2.getId(), medium.getId()); // ya no es libre el demonio

        espirituService.dominar(demonio1.getId(), demonio2.getId());
        Espiritu dominado = espirituService.recuperar(demonio2.getId()).get();

        assertNull(dominado.getEspirituDominante());
    }

    @Test
    void noPuedeOcurrirUnaDominacionAUnEspirituCon50DeEnergia() {
        ubicacionService.crear(ubicacion);

        demonio2.setNivelDeConexion(50);
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);

        espirituService.dominar(demonio1.getId(), demonio2.getId());
        Espiritu dominado = espirituService.recuperar(demonio2.getId()).get();

        assertNull(dominado.getEspirituDominante());
    }

    @Test
    void noPuedeOcurrirUnaDominacionAUnEspirituConMasDe50DeEnergia() {
        ubicacionService.crear(ubicacion);

        demonio2.setNivelDeConexion(70);
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);

        espirituService.dominar(demonio1.getId(), demonio2.getId());
        Espiritu dominado = espirituService.recuperar(demonio2.getId()).get();

        assertNull(dominado.getEspirituDominante());
    }

    @Test
    void noSePuedeDominarAUnEspirituSiDominadoQuiereDominarAlQueLoDominaYArrojaException() {
        ubicacionService.crear(ubicacion);

        demonio2.setNivelDeConexion(30); // energía menor a 50
        espirituService.crear(demonio1);
        espirituService.crear(demonio2); // es libre

        espirituService.dominar(demonio1.getId(), demonio2.getId());

        assertThrows(EspirituDominadoException.class,
                () -> espirituService.dominar(demonio2.getId(), demonio1.getId()));
    }

    @Test
    void cumpliendoTodasLasCondicionesSePuedeDominarAOtroEspiritu() {
        ubicacionService.crear(ubicacion);

        demonio2.setNivelDeConexion(30); // energía menor a 50
        espirituService.crear(demonio1);
        espirituService.crear(demonio2); // es libre

        espirituService.dominar(demonio1.getId(), demonio2.getId());
        Espiritu dominado = espirituService.recuperar(demonio2.getId()).get();

        assertEquals(demonio1.getId(), dominado.getEspirituDominante().getId());
    }

    @Test
    void unEspirituDominaAVariosEspiritus() {
        ubicacionService.crear(ubicacion);

        demonio2.setNivelDeConexion(30);
        demonio3.setNivelDeConexion(30);
        demonio3.setCoordenada(new Coordenada(0.520, 0.520)); // lo acerco al demonio1

        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        espirituService.dominar(demonio1.getId(), demonio2.getId());
        espirituService.dominar(demonio1.getId(), demonio3.getId());
        Espiritu dominado2 = espirituService.recuperar(demonio2.getId()).get();
        Espiritu dominado3 = espirituService.recuperar(demonio3.getId()).get();

        assertEquals(demonio1.getId(), dominado2.getEspirituDominante().getId());
        assertEquals(demonio1.getId(), dominado3.getEspirituDominante().getId());
    }

    @Test
    void unEspirituDominaAUnEspirituQueDominaAOtroEspiritu() {
        ubicacionService.crear(ubicacion);

        demonio2.setNivelDeConexion(30);
        demonio3.setNivelDeConexion(30);

        demonio1.setCoordenada(new Coordenada(0.59, 0.520));
        demonio2.setCoordenada(new Coordenada(0.55, 0.520));
        demonio3.setCoordenada(new Coordenada(0.5201, 0.520));

        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        espirituService.dominar(demonio2.getId(), demonio3.getId());
        espirituService.dominar(demonio1.getId(), demonio2.getId());
        Espiritu dominado2 = espirituService.recuperar(demonio2.getId()).get();
        Espiritu dominado3 = espirituService.recuperar(demonio3.getId()).get();

        assertEquals(demonio1.getId(), dominado2.getEspirituDominante().getId());
        assertEquals(demonio2.getId(), dominado3.getEspirituDominante().getId());
    }

    @Test
    void unEspirituNoPuedeSerDominadoCuandoYaEstaDominado() {
        ubicacionService.crear(ubicacion);

        demonio2.setNivelDeConexion(30);
        demonio3.setNivelDeConexion(30);

        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        espirituService.dominar(demonio1.getId(), demonio2.getId());
        espirituService.dominar(demonio3.getId(), demonio2.getId());
        Espiritu dominado2 = espirituService.recuperar(demonio2.getId()).get();

        assertEquals(demonio1.getId(), dominado2.getEspirituDominante().getId());
    }

    @Test
    void unMediumNoPuedeConectarseConUnEspirituDominado() {
        ubicacionService.crear(ubicacion);

        mediumService.crear(medium);

        demonio2.setNivelDeConexion(30);
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);

        espirituService.dominar(demonio1.getId(), demonio2.getId());

        assertThrows(EspirituDominadoException.class,
                () -> espirituService.conectar(demonio2.getId(), medium.getId()));

    }

    @Test
    void unEspirituSeEncuentraAMasDe5KmDeDistanciaDeOtroYNoLoPuedeDominarTirandoUnaException() {
        Ubicacion tailandia = new Santuario("Tailandia", 50,Arrays.asList(
                new Coordenada(20, 20),
                new Coordenada(20, 30),
                new Coordenada(30, 30),
                new Coordenada(30, 20),
                new Coordenada(20, 20)));

        ubicacionService.crear(ubicacion);
        ubicacionService.crear(tailandia);

        demonio2.setNivelDeConexion(30); // energía menor a 50
        demonio2.setCoordenada(new Coordenada(21, 21)); // muy lejos del demonio 1 (como 3000km)
        espirituService.crear(demonio1);
        espirituService.crear(demonio2); // es libre

        assertThrows(EspirituMuyLejanoException.class, () -> {espirituService.dominar(demonio1.getId(), demonio2.getId());});

    }

    @Test
    void unEspirituSeEncuentraAMenosDe2KmDeDistanciaDeOtroYNoLoPuedeDominarTirandoUnaException() {
        ubicacionService.crear(ubicacion);

        demonio2.setNivelDeConexion(30); // energía menor a 50
        demonio2.setCoordenada(new Coordenada(0.001, 0));
        espirituService.crear(demonio1);
        espirituService.crear(demonio2); // es libre


        assertThrows(EspirituMuyLejanoException.class, () -> {espirituService.dominar(demonio1.getId(), demonio2.getId());});

    }

    @Test
    void unPortalSeAbreEnUnaCoordenadaValidaYPermiteGenerar5Minions() {
        ubicacionService.crear(ubicacion);

        demonio2.setNivelDeConexion(50);
        demonio2.setCoordenada(new Coordenada(0.001, 0));

        espirituService.crear(demonio2);

        espirituService.crearPortal(0.001, 0d);

        List<Demonio> demonios = espirituService.espiritusDemoniacos(Sort.Direction.DESC, 0, 10);

        assertEquals(6, demonios.size());
        assertTrue(demonios.stream().anyMatch(dem -> Objects.equals(dem.getNombre(), "minion-de-" + demonio2.getNombre() + "-" + "1")));
        assertTrue(demonios.stream().anyMatch(dem -> Objects.equals(dem.getNombre(), "minion-de-" + demonio2.getNombre() + "-" + "2")));
        assertTrue(demonios.stream().anyMatch(dem -> Objects.equals(dem.getNombre(), "minion-de-" + demonio2.getNombre() + "-" + "3")));
        assertTrue(demonios.stream().anyMatch(dem -> Objects.equals(dem.getNombre(), "minion-de-" + demonio2.getNombre() + "-" + "4")));
        assertTrue(demonios.stream().anyMatch(dem -> Objects.equals(dem.getNombre(), "minion-de-" + demonio2.getNombre() + "-" + "5")));


    }



    @AfterEach
    void eliminarModelo() {
        testService.eliminarEspiritus();
        testService.eliminarMediums();
        testService.eliminarUbicaciones();
    }
}
