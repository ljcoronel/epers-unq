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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class HibernateEspirituDAOTest {
    public static final Medium MEDIUM = new Medium("Brujita", null, null);
    private EspirituDAO espirituDao = new HibernateEspirituDAO();
    private Espiritu espiritu1 =  new Angel(10,"Angelito1");
    private Espiritu espiritu2=  new Angel(20,"Angelito2");


    @BeforeEach
    void crearEntidades() {

        HibernateTransactionRunner.runTrx(() -> {
            espirituDao.guardar(espiritu1);
            espirituDao.guardar(espiritu2);
            return null;
        });
    }

    @Test
    void SePuedeRecuperarElEspirituCreadoConLaMismaInformacion() {

        Espiritu espirituRecuperado = HibernateTransactionRunner.runTrx(()->{
            return espirituDao.recuperar(espiritu1.getId());
        });

        assertEquals(espiritu1.getNombre(), espirituRecuperado.getNombre());
        assertEquals(espiritu1.getNivelDeConexion(), espirituRecuperado.getNivelDeConexion());
        Assertions.assertNotEquals(espiritu1, espirituRecuperado);

    }

    @Test
    void AlRecuperarUnEspituQueNoExisteQuedaVacio(){
        Long id_inventado = 9999L;
        var espirituInexistente = HibernateTransactionRunner.runTrx(()->{
            return espirituDao.recuperar(id_inventado);
        });
        assertNull(espirituInexistente);
    }

    @Test
    void sePuedenActualizarLosEspiritus(){
        espiritu1.setNombre("Nuevo Nombre");
        HibernateTransactionRunner.runTrx(()->{
            espirituDao.actualizar(espiritu1);
            return null;
        });

        var Espiritu_recuperado = HibernateTransactionRunner.runTrx(()->{
            return espirituDao.recuperar(espiritu1.getId());
        });
        assertEquals(Espiritu_recuperado.getNombre(), "Nuevo Nombre");
    }

    @Test
    void sePuedeRecuperarMuchosEspiritus(){
        List<Espiritu> espiritus = HibernateTransactionRunner.runTrx(() -> {
            return  espirituDao.recuperarTodos();
        });

        List<Long> ids = new ArrayList<>();
        espiritus.forEach(espiritu -> {ids.add(espiritu.getId());});

        assertEquals(2, espiritus.size());
        assertTrue(ids.contains(espiritu1.getId()));
        assertTrue(ids.contains(espiritu2.getId()));
    }



    @Test
    void alEliminarUnEspirituDejaDeEstarPersistido() {

        HibernateTransactionRunner.runTrx(() -> {
            espirituDao.eliminar(espiritu1);
            return null;
        });

        assertNull(HibernateTransactionRunner.runTrx(() -> espirituDao.recuperar(espiritu1.getId())));

    }

    @Test
    void alEliminarUnEspirituQueNoFueGuardadoNoHaceNada() { // está de más testear esto?
        Espiritu nuevo_espiritu =  new Angel(10,"Angel2");

        assertDoesNotThrow( () -> {
                    HibernateTransactionRunner.runTrx(() -> {
                        espirituDao.eliminar(nuevo_espiritu);
                        return null;
                    });
                }
        );
    }

    @Test
    void alEliminarUnEspirituQueFueEliminadoLanzaError() {

        HibernateTransactionRunner.runTrx(() -> {
            espirituDao.eliminar(espiritu1);
            return null;
        });

        assertThrows(OptimisticLockException.class, () -> { // optimisticLock es una excepción cuando ya se borró algo
                HibernateTransactionRunner.runTrx(() -> {
                    espirituDao.eliminar(espiritu1);
                    return null;
                });
        });

    }

    @Test
    void alGuardarAlEspirituEsteRecibeUnId() {
        assertNotNull(espiritu1.getId());
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
