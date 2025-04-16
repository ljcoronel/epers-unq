package ar.edu.unq.epergeist.persistencia.dao.hibernate;

import ar.edu.unq.epergeist.servicios.testUtils.TestService;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateMediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateUbicacionDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MediumDAOTest {

    private MediumDAO mediumDAO;
    private Medium unMedium;
    private Ubicacion ubicacion = new Ubicacion("Washington");
    private UbicacionDAO ubicacionDao = new HibernateUbicacionDAO();
    private TestService limpiador = new TestService();

    @BeforeEach
    void setUp() {
        mediumDAO = new HibernateMediumDAO();
        unMedium = new Medium("Max",100,50, ubicacion);

        HibernateTransactionRunner.runTrx(()-> {
                    mediumDAO.crear(unMedium);
                    ubicacionDao.crear(ubicacion);
                    return null;
                }
        );
    }



    @Test
    void alRecuperarUnMediumCreadoSeObtienenLosMismosDatos() {

        Medium mediumRecuperado = HibernateTransactionRunner.runTrx(()->mediumDAO.recuperar(unMedium.getId()).get());

        assertEquals(unMedium.getId(), mediumRecuperado.getId());
        assertEquals(unMedium.getMana(),mediumRecuperado.getMana());
        assertEquals(unMedium.getManaMax(), mediumRecuperado.getManaMax());
        assertEquals(unMedium.getNombre(), mediumRecuperado.getNombre());
    }

    @Test
    void alRecuperarUnMediumQueNoEstaGuardadoDevuelveEmpty() {

        Long idInventado = 115L;
        Optional<Medium> recuperado = HibernateTransactionRunner.runTrx(()->mediumDAO.recuperar(idInventado));

        assertTrue(recuperado.isEmpty());
    }

    @Test
    void alActualizarUnMediumYLuegoRecuperarloLosCambiosSeVenReflejados() {
        unMedium.setNombre("nombreModificado");
        unMedium.setMana(60);
        unMedium.setManaMax(800);

        HibernateTransactionRunner.runTrx(()->{
            mediumDAO.actualizar(unMedium);
            return null;
        });

        var mediumActualizado = HibernateTransactionRunner.runTrx(()->mediumDAO.recuperar(unMedium.getId()).get());

        assertEquals("nombreModificado",mediumActualizado.getNombre());
        assertEquals(60,mediumActualizado.getMana());
        assertEquals(800,mediumActualizado.getManaMax());

    }

    @AfterEach
    void cleanUp() {
        this.limpiador.eliminarModelo(Espiritu.class);
        this.limpiador.eliminarModelo(Medium.class);
        this.limpiador.eliminarModelo(Ubicacion.class);
    }
}
