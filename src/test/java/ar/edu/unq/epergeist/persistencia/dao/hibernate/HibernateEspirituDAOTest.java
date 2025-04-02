package ar.edu.unq.epergeist.persistencia.dao.hibernate;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HibernateEspirituDAOTest {
    public static final Medium MEDIUM = new Medium("Brujita", null, null);
    private EspirituDAO espirituDao = new HibernateEspirituDAO();
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
        HibernateTransactionRunner.runTrx(() -> {
            espirituDao.guardar(espiritu);
            return null;
        });

        HibernateTransactionRunner.runTrx(() -> {
            espirituDao.eliminar(espiritu);
            return null;
        });

        assertNull(HibernateTransactionRunner.runTrx(() -> espirituDao.recuperar(espiritu.getId())));

    }

    @Test
    void alEliminarUnEspirituQueNoFueGuardadoNoHaceNada() { // está de más testear esto?
        HibernateTransactionRunner.runTrx(() -> {
            espirituDao.guardar(espiritu);
            return null;
        });

        assertDoesNotThrow( () -> {
                    HibernateTransactionRunner.runTrx(() -> {
                        espirituDao.eliminar(espiritu);
                        return null;
                    });
                }
        );


    }

    @Test
    void alEliminarUnEspirituQueFueEliminadoLanzaError() {
        HibernateTransactionRunner.runTrx(() -> {
            espirituDao.guardar(espiritu);
            return null;
        });

        HibernateTransactionRunner.runTrx(() -> {
            espirituDao.eliminar(espiritu);
            return null;
        });

        assertThrows(OptimisticLockException.class, () -> { // optimisticLock es una excepción cuando ya se borró algo
                HibernateTransactionRunner.runTrx(() -> {
                    espirituDao.eliminar(espiritu);
                    return null;
                });
        }
                );



    }



    @Test
    void alGuardarYLuegoRecuperarSeObtienenObjetosSimilares() {

        HibernateTransactionRunner.runTrx(() -> {
            espirituDao.guardar(espiritu);
            return null;
        });

        var otroEspiritu = HibernateTransactionRunner.runTrx(() -> espirituDao.recuperar(espiritu.getId()));

        assertEquals(espiritu.getNombre(), otroEspiritu.getNombre());
        assertEquals(espiritu.getNivelDeConexion(), otroEspiritu.getNivelDeConexion());
//         no puedo comparar ID porque otroEspiritu no tiene ID setteado todavía


        Assertions.assertNotEquals(espiritu, otroEspiritu);

    }

    @Test
    void alGuardarAlEspirituEsteRecibeUnId() {

        HibernateTransactionRunner.runTrx(() -> {
            espirituDao.guardar(espiritu);
            return null;
        });

        assertNotNull(espiritu.getId());

    }

    // test para actualizar un espíritu pero que tire error porque no existe todavía


//
//    @Test
//    void alGuardarActualizarYLuegoRecuperarSeObtieneObjetosSimilares() {
//
//        espirituDao.crear(espiritu);
//        espirituDao.actualizar(espiritu);
//
//        var otroEspiritu = espirituDao.recuperar(espiritu.getId());
//
//        assertEquals(espiritu.getTipo(), otroEspiritu.getTipo());
//        assertEquals(espiritu.getNombre(), otroEspiritu.getNombre());
//        assertEquals(espiritu.getNivelDeConexion(), otroEspiritu.getNivelDeConexion());
////         no puedo comparar ID porque otroEspiritu no tiene ID setteado todavía
//
//
//        Assertions.assertNotEquals(espiritu, otroEspiritu);
//
//    }

//    @Test
//    void alRecuperarAUnEspirituConectadoSuNivelDeConexionPersisteSiSeLoActualiza() {
//
//        espirituDao.crear(espiritu);
//        espiritu.aumentarConexion(MEDIUM);
//        espirituDao.actualizar(espiritu);
//
//        var otroEspiritu = espirituDao.recuperar(espiritu.getId());
//
//        assertEquals(10, espiritu.getNivelDeConexion());
//        Assertions.assertNotEquals(espiritu, otroEspiritu);
//
//    }


//    @Test
//    void alRecuperarUnEspirituQueNoExisteDevuelveNull() {
//
//        assertNull(HibernateTransactionRunner.runTrx(() -> espirituDao.recuperar(-1L)));
//    }

//
//    @Test
//    void alRecuperarTodosEspíritusPeroNoHayNingunoDevuelveListaVacía() {
//
//        assertTrue(espirituDao.recuperarTodos().isEmpty());
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
//        espirituDao.crear(espiritu);
//        espirituDao.crear(gonzalo);
//        espirituDao.crear(jaimito);
//
//        ArrayList<Long> idDeTodos = new ArrayList<Long>();
//
//        espirituDao.recuperarTodos().forEach( (espiritu) -> {
//            idDeTodos.add(espiritu.getId());
//        });
//
//        assertEquals(Arrays.asList(gonzalo.getId(), jaimito.getId(), espiritu.getId()), idDeTodos);
//
//
//
//    }




    @AfterEach
    void emilinarModelo() {
        HibernateTransactionRunner.runTrx(() -> {
            Session session = HibernateTransactionRunner.getCurrentSession();
            session.createQuery("delete from " + "Espiritu").executeUpdate();
            return null;
        });
    }
}