package ar.edu.unq.epergeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateMediumDAO;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.impl.EspirituServiceImpl;
import ar.edu.unq.epersgeist.servicios.impl.MediumServiceImpl;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.hibernate.query.SortDirection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EspirituServiceImplTest {
    private final EspirituServiceImpl espirituService = new EspirituServiceImpl(new HibernateEspirituDAO(), new HibernateMediumDAO());
    private  Medium medium;
    private MediumService mediumService = new MediumServiceImpl(new HibernateMediumDAO(),new HibernateEspirituDAO());
    private TestService testService  = new TestService();
    private Espiritu angel1;
    private Espiritu angel2;
    private Espiritu angel3;
    private Demonio demonio1;
    private Demonio demonio2;
    private Demonio demonio3;


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
    void crearInstancias() {
        angel1 = new Angel(10,"Angel1");
        angel2 = new Angel(10,"Angel2");
        angel3 = new Angel(10,"Angel3");
        demonio1 = new Demonio(
                10,
                "Pepito"

        );
        demonio2 = new Demonio(
                90,
                "Diavolo"

        );
        demonio3 = new Demonio(
                50,
                "Sebastián"

        );
        medium = new Medium("Brujita", 100, 50);
    }

    @Test
    void alRecuperarUnIdInexistenteNoTraeNada(){
        Long id_Inexistente = 50L;
        Long id_Inexistente2 = -50L;
        espirituService.recuperar(id_Inexistente);
        assertFalse(espirituService.recuperar(id_Inexistente).isPresent());
        assertFalse(espirituService.recuperar(id_Inexistente2).isPresent());
    }


    @Test
    void sePuedenRecuperarTodosLosEspiritusCreados(){
        espirituService.crear(angel1);
        espirituService.crear(angel2);
        espirituService.crear(angel3);
        List<Espiritu> espiritus_recuperados = espirituService.recuperarTodos();
        assertEquals(3, espiritus_recuperados.size());
    }

    @Test
    void alEliminarUnEspirituDejaDeEstarPersistido() {
        espirituService.crear(angel1);
        espirituService.eliminar(angel1.getId());
        Optional<Espiritu> espirituInexistente = espirituService.recuperar(angel1.getId());
        assertFalse(espirituInexistente.isPresent());
    }

    @Test
    void alEliminarUnEspirituQueNoFueCreadoNoHaceNada() { // está de más testear esto?
        espirituService.crear(angel1);
        assertDoesNotThrow( () -> {
            espirituService.eliminar(angel1.getId());
                }
        );
    }

    @Test
    void alActualizarUnEspirituSeCambiaCorrectamente() {
        espirituService.crear(angel1);
        angel1.setNombre("Nuevo Nombre");
        espirituService.actualizar(angel1);

        var espiritu_recuperado = espirituService.recuperar(angel1.getId()).get();
        assertEquals("Nuevo Nombre", espiritu_recuperado.getNombre());

    }


    @Test
    void alCrearYLuegoRecuperarSeObtieneObjetosSimilares() {

        espirituService.crear(angel1);
        var otroEspiritu = espirituService.recuperar(angel1.getId()).get();
        assertEquals(angel1.getNombre(), otroEspiritu.getNombre());
        assertEquals(angel1.getNivelDeConexion(), otroEspiritu.getNivelDeConexion());
        assertEquals(angel1.getId(), otroEspiritu.getId());
        Assertions.assertNotEquals(angel1, otroEspiritu);
    }

    @Test
    void seAgregaElIdAlEspirituAlCrearUnEspiritu() {
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

        espirituService.crear(angel1);
        espirituService.crear(demonio1);

        mediumService.crear(medium);

        espirituService.conectar(demonio1.getId(), medium.getId());
        espirituService.conectar(angel1.getId(), medium.getId());


        Espiritu angelRecuperado = espirituService.recuperar(angel1.getId()).get();
        Espiritu demonioRecuperado = espirituService.recuperar(demonio1.getId()).get();

        //List<Espiritu> espiritusRecuperados = mediumService.espiritus(medium.getId());//descomentar cuando se haga el metodo espiritus

        assertEquals(20, angelRecuperado.getNivelDeConexion());
        assertEquals(20, demonioRecuperado.getNivelDeConexion());

        //assertEquals(2, espiritusRecuperados.size());//descomentar cuando se haga el metodo espiritus, se supone que el metodo espiritus trae espiritus de ambas listas

    }
    @Test
    void alIntentarConectarConUnEspirituQueYaSeConectoNoHaceNada(){

        espirituService.crear(angel1);
        mediumService.crear(medium);


        espirituService.conectar(angel1.getId(), medium.getId());

        Espiritu angelRecuperado = espirituService.recuperar(angel1.getId()).get();

        assertDoesNotThrow( () -> {
                    espirituService.conectar(angel1.getId(), medium.getId());
                }
        );

        assertEquals(20, angelRecuperado.getNivelDeConexion());//como no hizo nada la segunda conexion su nivel se mantuvo en 20

    }

    @Test
    void alIntentarConectarConUnEspirituQueNoExisteLanzaError(){

        mediumService.crear(medium);

        assertThrows(NoSuchElementException.class, () -> {
            espirituService.conectar(-1L, medium.getId());
        });
    }

    @Test
    void alIntentarConectarConUnMediumQueNoExisteLanzaError(){

        espirituService.crear(angel1);

        assertThrows(NoSuchElementException.class, () -> {
            espirituService.conectar(angel1.getId(), -1L);
        });
    }
    @Test
    void alRecuperarTodosLosEspiritusDemoniosNoDebenHaberDeOtroTipo() {
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(angel1); //es un angel


        // hay 3 en total, 2 son ángeles y 1 es demonio

        List<Demonio> demonios = espirituService.espiritusDemoniacos(SortDirection.ASCENDING, 0, 2);

        assertEquals(2, demonios.size());

    }

    @Test
    void alRecuperarTodosLosDemoniosPeroNoHaberDevuelveListaVacía() {

        espirituService.crear(angel1);

        List<Demonio> demonios = espirituService.espiritusDemoniacos(SortDirection.DESCENDING, 0, 2);// agarro sólo los demonios

        assertTrue(demonios.isEmpty());

    }

    @Test
    void alRecuperarTodosLosDemoniosEspecificandoOrdenDescendenteDebenEstarEnOrdenDeNivelDeConexiónDescendente() {

        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        List<Long> expected = Arrays.asList(demonio2.getId(), demonio3.getId(), demonio1.getId());
        //  2 3 1

        List<Long> demoniosIds = HibernateTransactionRunner.runTrx(() ->
                espirituService.espiritusDemoniacos(SortDirection.DESCENDING, 0, 3)
        ).stream().map(Espiritu::getId).toList();


        assertEquals(expected, demoniosIds);




    }

    @Test
    void alRecuperarTodosLosDemoniosEspecificandoOrdenAscendenteDebenEstarEnOrdenDeNivelDeConexiónAscendente() {
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        List<Long> expected = Arrays.asList(demonio1.getId(), demonio3.getId(), demonio2.getId());
        //  1 3 2

        List<Long> demoniosIds = HibernateTransactionRunner.runTrx(() ->
                espirituService.espiritusDemoniacos(SortDirection.ASCENDING, 0, 3)
        ).stream().map(Espiritu::getId).toList();

        assertEquals(expected, demoniosIds);

    }

    @Test
    void alRecuperarATodosLosDemoniosSiSeUsanValoresNegativosParaPáginaDebeFallar() {

        assertThrows(IllegalArgumentException.class, () -> espirituService.espiritusDemoniacos(SortDirection.ASCENDING, -1, 3));

    }

    @Test
    void alRecuperarATodosLosDemoniosSiSeUsanValoresNegativosParaCantidadPorPáginaDebeFallar() {

        assertThrows(IllegalArgumentException.class, () -> espirituService.espiritusDemoniacos(SortDirection.ASCENDING, 0, -1));

    }

    @Test
    void alRecuperarATodosLosDemoniosSiSeUsa0ParaCantidadPorPáginaDebeFallar() {

        assertThrows(IllegalArgumentException.class, () -> espirituService.espiritusDemoniacos(SortDirection.ASCENDING, 0, 0));

    }

    @Test
    void alRecuperarMenosDemoniosDeLosTotalesDebeTraerLaCantidadEspecificada() {
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        List<Demonio> demonios = HibernateTransactionRunner.runTrx(() ->
                espirituService.espiritusDemoniacos(SortDirection.ASCENDING, 0, 2)
        );

        assertEquals(2, demonios.size());

    }

    @Test
    void alRecuperarDemoniosDeUnaPáginaMásAvanzadaDondeHayDemoniosDebeTraerLosDeEsaPáginaSolamente() {
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        List<Demonio> demonios = HibernateTransactionRunner.runTrx(() ->
                espirituService.espiritusDemoniacos(SortDirection.ASCENDING, 1, 2)
        );

        //dem 1 -> pag 0
        //dem 2 -> pag 0
        //dem 3 -> pag 1

        assertEquals(1, demonios.size());

    }

    @Test
    void alRecuperarDemoniosDeUnaPáginaMásAvanzadaDondeNoHayDemoniosNoTraeNada() {
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        List<Demonio> demonios = HibernateTransactionRunner.runTrx(() ->
                espirituService.espiritusDemoniacos(SortDirection.ASCENDING, 1, 5)
        );

        //dem 1 -> pag 0
        //dem 2 -> pag 0
        //dem 3 -> pag 0
        // no hay más

        assertTrue(demonios.isEmpty());

    }

    @Test
    void alRecuperarDemoniosDeUnaPáginaDondeNoHayMismaCantidadDeDemoniosQueLaCantidadPorPáginaTraeTodosLosQueHayaEnEsaPágina() {
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        List<Demonio> demonios = HibernateTransactionRunner.runTrx(() ->
                espirituService.espiritusDemoniacos(SortDirection.ASCENDING, 1, 2)
        );

        //dem 1 -> pag 0
        //dem 2 -> pag 0
        //dem 3 -> pag 1
        // no hay más

        assertEquals(1, demonios.size());

    }

    @Test
    void alRecuperarTodosLosDemoniosOrdenDescendenteDebenEstarEnOrdenDeNivelDeConexiónDescendenteEntreTodosLosDemonios() {
        Demonio demonio4 = new Demonio(
                99,
                "Enrique"

        );
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);
        espirituService.crear(demonio4);

        // el orden por inserción es: 1, 2, 3, 4

        List<Long> demoniosIds = HibernateTransactionRunner.runTrx(() ->
                espirituService.espiritusDemoniacos(SortDirection.DESCENDING, 0, 3)
        ).stream().map((Demonio::getId)).toList();

        List<Long> demoniosIdsEsperados = Arrays.asList(demonio4.getId(), demonio2.getId(), demonio3.getId());

        // 4 2 3

        assertEquals(demoniosIdsEsperados, demoniosIds);

    }

    @Test
    void alRecuperarTodosLosDemoniosDeUnTipoEspecificandoOrdenAscendenteDebenEstarEnOrdenDeNivelDeConexiónAscendenteEntreTodosLosDemonios() {
        Demonio demonio4 = new Demonio(
                2,
                "Enrique"

        );
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);
        espirituService.crear(demonio4);

        // el orden por inserción es: 1, 2, 3, 4

        List<Long> demoniosIds = HibernateTransactionRunner.runTrx(() ->
                espirituService.espiritusDemoniacos(SortDirection.ASCENDING, 0, 3)
        ).stream().map((Demonio::getId)).toList();

        List<Long> demoniosIdsEsperados = Arrays.asList(demonio4.getId(), demonio1.getId(), demonio3.getId());

        // 4 1 3

        assertEquals(demoniosIdsEsperados, demoniosIds);

    }


    @AfterEach
    void eliminarModelo() {
        testService.eliminarModelo(Espiritu.class);
        testService.eliminarModelo(Medium.class);
    }
}