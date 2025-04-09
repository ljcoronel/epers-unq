package ar.edu.unq.epergeist.persistencia.dao.hibernate;

import ar.edu.unq.epergeist.servicios.impl.TestService;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class HibernateEspirituDAOTest {
    public static final Medium MEDIUM = new Medium("Brujita", null, null);
    private EspirituDAO espirituDao = new HibernateEspirituDAO();
    private Espiritu espiritu1 =  new Angel(10,"Angelito1");
    private Espiritu espiritu2=  new Angel(20,"Angelito2");
    private TestService testService = new TestService();


    @BeforeEach
    void crearEntidades() {

        HibernateTransactionRunner.runTrx(() -> {
            espirituDao.crear(espiritu1);
            espirituDao.crear(espiritu2);
            return null;
        });
    }

    @Test
    void SePuedeRecuperarAlEspirituCreadoConLaMismaInformacion() {

        Espiritu espirituRecuperado = HibernateTransactionRunner.runTrx(()->{
            return espirituDao.recuperar(espiritu1.getId()).get();
        });

        assertEquals(espiritu1.getNombre(), espirituRecuperado.getNombre());
        assertEquals(espiritu1.getNivelDeConexion(), espirituRecuperado.getNivelDeConexion());
        assertEquals(espiritu1.getId(), espirituRecuperado.getId());
        Assertions.assertNotEquals(espiritu1, espirituRecuperado);

    }

    @Test
    void AlRecuperarUnEspituQueNoExisteQuedaVacio(){
        Long id_inventado = 9999L;
        var espirituInexistente = HibernateTransactionRunner.runTrx(()->{
            return espirituDao.recuperar(id_inventado);
        });
        assertFalse(espirituInexistente.isPresent());
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
        }).get();
        assertEquals(Espiritu_recuperado.getNombre(), "Nuevo Nombre");
    }

    @Test
    void sePuedenRecuperarMuchosEspiritus(){
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
            espirituDao.eliminar(espiritu1.getId());
            return null;
        });

        Optional<Espiritu> espirituBorradoRecuperado = HibernateTransactionRunner.runTrx(() -> espirituDao.recuperar(espiritu1.getId()));
        assertFalse(espirituBorradoRecuperado.isPresent());

    }

    @Test
    void alEliminarAUnEspirituQueNoExisteNoHaceNada() {

        assertDoesNotThrow( () -> {
                    HibernateTransactionRunner.runTrx(() -> {
                        espirituDao.eliminar(999L);
                        return null;
                    });
                }
        );
        List<Espiritu> espiritus = HibernateTransactionRunner.runTrx(() -> {
            return  espirituDao.recuperarTodos();
        });

        assertEquals(2, espiritus.size());
    }

    @Test
    void alCrearAlEspirituEsteRecibeUnId() {
        assertNotNull(espiritu1.getId());
    }

    // test para actualizar un espíritu pero que tire error porque no existe todavía
    @Test
    void AlActualizarUnEspirituInexistenteNoActualizaNada() {
        Espiritu espiritu_inexistente =  new Angel(10,"NoExiste");

        HibernateTransactionRunner.runTrx(()->{
            espirituDao.actualizar(espiritu_inexistente);
            return null;
        });
        assertNull(espiritu_inexistente.getId());

    }

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


//
//    @Test
//    void alRecuperarTodosEspíritusPeroNoHayNingunoDevuelveListaVacía() {
//
//        assertTrue(espirituDao.recuperarTodos().isEmpty());
//    }
//
//

//
//
//    }


    @AfterEach
    void emilinarModelo() {
        testService.eliminarModelo(Espiritu.class);
    }
}
