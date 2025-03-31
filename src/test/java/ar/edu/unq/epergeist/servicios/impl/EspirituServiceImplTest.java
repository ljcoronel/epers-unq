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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class EspirituServiceImplTest {
    public static final Medium MEDIUM = new Medium("Brujita", null, null);
    private final EspirituServiceImpl espirituService = new EspirituServiceImpl(new HibernateEspirituDAO());
    private Espiritu espiritu;

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
        espiritu = new Angel(
                10,
                "Pepe"

        );
    }

    @Test
    void alEliminarUnEspirituDejaDeEstarPersistido() {
        espirituService.crear(espiritu);
        espirituService.eliminar(espiritu);

       assertNull(espirituService.recuperar(espiritu.getId()));
    }

    @Test
    void alEliminarUnEspirituQueFueEliminadoLanzaError() {
        espirituService.crear(espiritu);

        espirituService.eliminar(espiritu);

        assertThrows(OptimisticLockException.class, () -> { // optimisticLock es una excepción cuando ya se borró algo
            espirituService.eliminar(espiritu);
                }
        );

    }

    @Test
    void alEliminarUnEspirituQueNoFueGuardadoNoHaceNada() { // está de más testear esto?
        espirituService.crear(espiritu);

        assertDoesNotThrow( () -> {
            espirituService.eliminar(espiritu);
                }
        );


    }

    @Test
    void alGuardarYLuegoRecuperarSeObtieneObjetosSimilares() {

        espirituService.crear(espiritu);

        var otroEspiritu = espirituService.recuperar(espiritu.getId());

        assertEquals(espiritu.getNombre(), otroEspiritu.getNombre());
        assertEquals(espiritu.getNivelDeConexion(), otroEspiritu.getNivelDeConexion());
//         no puedo comparar ID porque otroEspiritu no tiene ID setteado todavía


        Assertions.assertNotEquals(espiritu, otroEspiritu);

    }

    @Test
    void alGuardarAlEspirituEsteRecibeUnId() {

        espirituService.crear(espiritu);

        assertNotNull(espiritu.getId());

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