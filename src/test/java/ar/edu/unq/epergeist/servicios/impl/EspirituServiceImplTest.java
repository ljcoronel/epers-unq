package ar.edu.unq.epergeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
import ar.edu.unq.epersgeist.servicios.impl.EspirituServiceImpl;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class EspirituServiceImplTest {
    public static final Medium MEDIUM = new Medium("Brujita", null, null);
    private final EspirituServiceImpl espirituService = new EspirituServiceImpl(new HibernateEspirituDAO());
    private Espiritu espiritu1;
    private Espiritu espiritu2;
    private Espiritu espiritu3;

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
    }

    @Test
    void alRecuperarUnIdInexistenteNoTraeUnNull(){
        Long id_Inexistente = 50L;
        Long id_Inexistente2 = -50L;
        espirituService.recuperar(id_Inexistente);
        assertNull(espirituService.recuperar(id_Inexistente));
        assertNull(espirituService.recuperar(id_Inexistente2));
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
        espirituService.eliminar(espiritu1);
        assertNull(espirituService.recuperar(espiritu1.getId()));
    }


    @Test
    void alEliminarUnEspirituQueFueEliminadoLanzaError() {
        espirituService.crear(espiritu1);
        espirituService.eliminar(espiritu1);

        assertThrows(OptimisticLockException.class, () -> { // optimisticLock es una excepción cuando ya se borró algo
            espirituService.eliminar(espiritu1);
                }
        );

    }

    @Test
    void alEliminarUnEspirituQueNoFueGuardadoNoHaceNada() { // está de más testear esto?
        espirituService.crear(espiritu1);
        assertDoesNotThrow( () -> {
            espirituService.eliminar(espiritu1);
                }
        );
    }

    @Test
    void alActualizarUnEspirituSecambiaCorrectamente() {
        espirituService.crear(espiritu1);
        espiritu1.setNombre("Nuevo Nombre");
        espirituService.actualizar(espiritu1);

        var espiritu_recuperado = espirituService.recuperar(espiritu1.getId());
        assertTrue(espiritu_recuperado.getNombre().equals("Nuevo Nombre"));

    }

    @Test
    void alGuardarYLuegoRecuperarSeObtieneObjetosSimilares() {

        espirituService.crear(espiritu1);
        var otroEspiritu = espirituService.recuperar(espiritu1.getId());
        assertEquals(espiritu1.getNombre(), otroEspiritu.getNombre());
        assertEquals(espiritu1.getNivelDeConexion(), otroEspiritu.getNivelDeConexion());
        Assertions.assertNotEquals(espiritu1, otroEspiritu);

    }

    @Test
    void seAgregaElIdAlEspirituAlGuardarlo() {
        assertNull(espiritu1.getId());
        espirituService.crear(espiritu1);
        assertNotNull(espiritu1.getId());
    }
//
//    @Test
//    void alGuardarActualizarYLuegoRecuperarSeObtieneObjetosSimilares() {
//
//        espirituService.crear(espiritu);
//        espirituService.actualizar(espiritu);
//
//        var otroEspiritu = espirituService.recuperar(espiritu.getId());
//
//        assertEquals(espiritu.getTipo(), otroEspiritu.getTipo());
//        assertEquals(espiritu.getNombre(), otroEspiritu.getNombre());
//        assertEquals(espiritu.getNivelDeConexion(), otroEspiritu.getNivelDeConexion());
////         no puedo comparar ID porque otroPepe no tiene ID setteado todavía
//
//
//        Assertions.assertNotEquals(espiritu, otroEspiritu);
//
//    }
//
//
//    @Test
//    void alConectarUnEspirituSuNivelDeConexionAumentaYSePersiste() {
//        espirituService.crear(espiritu);
//
//        espirituService.conectar(espiritu.getId(), MEDIUM);
//
//        Espiritu espirituRecuperado = espirituService.recuperar(espiritu.getId());
//
//        assertEquals(10, espirituRecuperado.getNivelDeConexion());
//
//    }
//
//
//
//
//
//    @Test
//    void alRecuperarUnEspirituQueNoExisteDevuelveNull() {
//
//        assertNull(espirituService.recuperar(-1L));
//    }
//
//    @Test
//    void alRecuperarTodosEspíritusPeroNoHayNingunoDevuelveListaVacía() {
//
//        assertTrue(espirituService.recuperarTodos().isEmpty());
//    }
//
//
//    @Test
//    void recuperarATodosLosEspiritusVaEnOrdenAscendente() {
//
//        Espiritu gonzalo = new Espiritu(
//                "Fantasma",
//                0,
//                "Gonzalo"
//        );
//
//        Espiritu jaimito = new Espiritu(
//                "Fantasma",
//                0,
//                "Jaimito"
//        );
//
//        espirituService.crear(espiritu);
//        espirituService.crear(gonzalo);
//        espirituService.crear(jaimito);
//
//        ArrayList<Long> idDeTodos = new ArrayList<Long>();
//
//        espirituService.recuperarTodos().forEach( (espiritu) -> {
//            idDeTodos.add(espiritu.getId());
//        });
//
//        assertEquals(Arrays.asList(gonzalo.getId(), jaimito.getId(), espiritu.getId()), idDeTodos);
//
//
//
//    }
//
//
//
//
//
    @AfterEach
    void emilinarModelo() {
        HibernateTransactionRunner.runTrx(() -> {
            Session session = HibernateTransactionRunner.getCurrentSession();
            session.createQuery("delete from " + "Espiritu").executeUpdate();
            return null;
        });
    }
}