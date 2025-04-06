package ar.edu.unq.epergeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateMediumDAO;
import ar.edu.unq.epersgeist.servicios.impl.MediumServiceImpl;
import ar.edu.unq.epersgeist.servicios.impl.TestService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MediumServiceImplTest {

    private MediumServiceImpl mediumService;
    private TestService testService= new TestService();

    private Medium medium;
    private Medium medium2;

    @BeforeEach
    void crearMedium(){

        mediumService = new MediumServiceImpl(new HibernateMediumDAO());

        medium = new Medium("Max",100,50);
        medium2 = new Medium("Pepe",100,0);
    }
    @Test
    void seRecuperanTodosLosMediumsGuardados(){
        mediumService.crear(medium);
        mediumService.crear(medium2);

        var mediums = mediumService.recuperarTodos();

        assertEquals(2, mediums.size());
        assertTrue(mediums.contains(medium));
        assertTrue(mediums.contains(medium2));
    }

    @Test
    void alRecuperarTodosLosMediumsEstandoLaBaseDeDatosVaciaRetornaListaVacia(){

        var mediums = mediumService.recuperarTodos();

        assertTrue(mediums.isEmpty());
    }

    @Test
    void seEliminaUnMediumDejaDeEstarPersistiendo(){
        mediumService.crear(medium);

        mediumService.eliminar(medium);

        assertNull(mediumService.recuperar(medium.getId()));
    }

    @Test
    void alEliminarUnMediumNoGuardadoNoHaceNada() {

        assertDoesNotThrow(() -> mediumService.eliminar(medium));
    }

    @AfterEach
    void eliminarModelo() {
        testService.eliminarModelo(Medium.class);
    }
}
