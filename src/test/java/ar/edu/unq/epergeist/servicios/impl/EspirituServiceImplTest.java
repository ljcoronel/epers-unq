package ar.edu.unq.epergeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
import ar.edu.unq.epersgeist.servicios.impl.EspirituServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EspirituServiceImplTest {
    private final EspirituServiceImpl espirituService = new EspirituServiceImpl(new HibernateEspirituDAO());
    private TestService testService  = new TestService();
    private Espiritu angel1;
    private Espiritu angel2;
    private Espiritu angel3;
    private Demonio demonio1;
    private Demonio demonio2;

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
    void crearEspiritu() {
        angel1 = new Angel(10,"Angel1");
        angel2 = new Angel(10,"Angel2");
        angel3 = new Angel(10,"Angel3");
        demonio1 = new Demonio(
                5,
                "Pepito"

        );
        demonio2 = new Demonio(
                90,
                "Diavolo"

        );
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
    void alRecuperarTodosLosEspiritusDemoniosNoDebenHaberDeOtroTipo() {
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(angel1); //es un angel


        // hay 3 en total, 2 son ángeles y 1 es demonio

        List<Demonio> demonios = espirituService.espiritusDemoniacos(Direccion.DESCENDENTE, 0, 2);

        assertEquals(2, demonios.size());

    }

    @Test
    void alRecuperarTodosLosDemoniosPeroNoHaberDevuelveListaVacía() {

        espirituService.crear(angel1);

        List<Demonio> demonios = espirituService.espiritusDemoniacos(Direccion.DESCENDENTE, 0, 2);// agarro sólo los demonios

        assertTrue(demonios.isEmpty());

    }

    @Test
    void alRecuperarTodosLosDemoniosDebenEstarEnOrdenDeNivelDeConexiónDescendente() {
        Demonio demonio3 = new Demonio(
                10,
                "Sebastián"

        );
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);


        List<Long> expected = Arrays.asList(demonio2.getId(), demonio3.getId(), demonio1.getId());
        //  2 3 1

        List<Long> demoniosIds = espirituService.espiritusDemoniacos(Direccion.DESCENDENTE, 0, 3).stream().map(Espiritu::getId).toList();


        assertEquals(expected, demoniosIds);


    }


    @AfterEach
    void eliminarModelo() {
        testService.eliminarModelo(Espiritu.class);
    }
}