package ar.edu.unq.epergeist.persistencia.dao.hibernate;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateMediumDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MediumDAOTest {

    private MediumDAO mediumDAO;
    private Medium unMedium;

    @BeforeEach
    void setUp() {
        mediumDAO = new HibernateMediumDAO();
        unMedium = new Medium("Max",100,50);

        HibernateTransactionRunner.runTrx(()->mediumDAO.crear(unMedium));
    }



    @Test
    void alRecuperarUnMediumCreadoSeObtienenLosMismosDatos() {

        Medium mediumRecuperado = HibernateTransactionRunner.runTrx(()->mediumDAO.recuperar(unMedium.getId()).get());

        assertEquals(unMedium.getId(), mediumRecuperado.getId());
        assertEquals(unMedium.getMana(),mediumRecuperado.getMana());
        assertEquals(unMedium.getManaMax(), mediumRecuperado.getManaMax());
        assertEquals(unMedium.getNombre(), mediumRecuperado.getNombre());
    }
}
