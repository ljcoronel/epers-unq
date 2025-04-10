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
import jakarta.persistence.OptimisticLockException;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class EspirituServiceImplTest {
    private  Medium medium;
    private MediumService mediumService = new MediumServiceImpl(new HibernateMediumDAO());

    private final EspirituServiceImpl espirituService = new EspirituServiceImpl(new HibernateEspirituDAO(),new HibernateMediumDAO());
    private TestService testService  = new TestService();
    private Espiritu espiritu1;
    private Espiritu espiritu2;
    private Espiritu espiritu3;
    private Espiritu demonio1;


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
        espiritu1 = new Angel(10,"Angel1");
        espiritu2 = new Angel(10,"Angel2");
        espiritu3 = new Angel(10,"Angel3");
        demonio1 = new Demonio(10,"Demonio1");

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
        espirituService.crear(espiritu1);
        espirituService.crear(espiritu2);
        espirituService.crear(espiritu3);
        List<Espiritu> espiritus_recuperados = espirituService.recuperarTodos();
        assertEquals(3, espiritus_recuperados.size());
    }

    @Test
    void alEliminarUnEspirituDejaDeEstarPersistido() {
        espirituService.crear(espiritu1);
        espirituService.eliminar(espiritu1.getId());
        Optional<Espiritu> espirituInexistente = espirituService.recuperar(espiritu1.getId());
        assertFalse(espirituInexistente.isPresent());
    }

    @Test
    void alEliminarUnEspirituQueNoFueCreadoNoHaceNada() { // está de más testear esto?
        espirituService.crear(espiritu1);
        assertDoesNotThrow( () -> {
            espirituService.eliminar(espiritu1.getId());
                }
        );
    }

    @Test
    void alActualizarUnEspirituSeCambiaCorrectamente() {
        espirituService.crear(espiritu1);
        espiritu1.setNombre("Nuevo Nombre");
        espirituService.actualizar(espiritu1);

        var espiritu_recuperado = espirituService.recuperar(espiritu1.getId()).get();
        assertEquals("Nuevo Nombre", espiritu_recuperado.getNombre());

    }

    @Test
    void alCrearYLuegoRecuperarSeObtieneObjetosSimilares() {

        espirituService.crear(espiritu1);
        var otroEspiritu = espirituService.recuperar(espiritu1.getId()).get();
        assertEquals(espiritu1.getNombre(), otroEspiritu.getNombre());
        assertEquals(espiritu1.getNivelDeConexion(), otroEspiritu.getNivelDeConexion());
        assertEquals(espiritu1.getId(), otroEspiritu.getId());
        Assertions.assertNotEquals(espiritu1, otroEspiritu);
    }

    @Test
    void seAgregaElIdAlEspirituAlCrearUnEspiritu() {
        assertNull(espiritu1.getId());
        espirituService.crear(espiritu1);
        assertNotNull(espiritu1.getId());
    }

    @Test
    void alRecuperarTodosEspíritusPeroNoHayNingunoDevuelveListaVacía() {
        assertTrue(espirituService.recuperarTodos().isEmpty());
    }

    @Test
    void alConectarUnEspirituSeGuardaEnSuRespectivaListaDelMediumAumentandoSuConexionYSePersiste() {

        espirituService.crear(espiritu1);
        espirituService.crear(demonio1);

        mediumService.crear(medium);

        espirituService.conectar(demonio1.getId(), medium.getId());
        espirituService.conectar(espiritu1.getId(), medium.getId());


        Espiritu angelRecuperado = espirituService.recuperar(espiritu1.getId()).get();
        Espiritu demonioRecuperado = espirituService.recuperar(demonio1.getId()).get();

        //List<Espiritu> espiritusRecuperados = mediumService.espiritus(medium.getId());//descomentar cuando se haga el metodo espiritus

        assertEquals(20, angelRecuperado.getNivelDeConexion());
        assertEquals(20, demonioRecuperado.getNivelDeConexion());

        //assertEquals(2, espiritusRecuperados.size());//descomentar cuando se haga el metodo espiritus, se supone que el metodo espiritus trae espiritus de ambas listas

    }
    @Test
    void alIntentarConectarConUnEspirituQueYaSeConectoNoHaceNada(){

        espirituService.crear(espiritu1);
        mediumService.crear(medium);


        espirituService.conectar(espiritu1.getId(), medium.getId());

        Espiritu angelRecuperado = espirituService.recuperar(espiritu1.getId()).get();

        assertDoesNotThrow( () -> {
                    espirituService.conectar(espiritu1.getId(), medium.getId());
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

        espirituService.crear(espiritu1);

        assertThrows(NoSuchElementException.class, () -> {
            espirituService.conectar(espiritu1.getId(), -1L);
        });
    }

    @AfterEach
    void eliminarModelo() {
        testService.eliminarModelo(Espiritu.class);
        testService.eliminarModelo(Medium.class);
    }
}