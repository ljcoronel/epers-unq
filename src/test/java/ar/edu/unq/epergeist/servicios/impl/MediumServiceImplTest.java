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

        mediumService.eliminar(medium.getId());

        assertTrue(mediumService.recuperar(medium.getId()).isEmpty());
    }

    @Test
    void alEliminarUnMediumNoGuardadoNoHaceNada() {

        assertDoesNotThrow(() -> mediumService.eliminar(15L));
    }


    //feature crear
    @Test
    void alCrearYRecuperarMediumSeObtienenObjetosSimilares() {
        mediumService.crear(medium);

        Medium mediumRecuperado =  mediumService.recuperar(medium.getId()).get();

        assertEquals(medium.getId(), mediumRecuperado.getId());
        assertEquals(medium.getMana(),mediumRecuperado.getMana());
        assertEquals(medium.getManaMax(), mediumRecuperado.getManaMax());
        assertEquals(medium.getNombre(), mediumRecuperado.getNombre());
    }

    @Test
    void alCrearUnMediumSeLeAgregaUnId() {

        assertNull(medium.getId());

        mediumService.crear(medium);

        assertNotNull(medium.getId());
    }
    
    
    //feature actualizar

    @Test
    void seActualizanUnMediumYAlRecuperarloLosCambiosSeVenReflejados() {
        mediumService.crear(medium);
        medium.setNombre("nombreModificado");

        mediumService.actualizar(medium);
        Medium recuperado = mediumService.recuperar(medium.getId()).get();

        assertEquals("nombreModificado", recuperado.getNombre());
    }

    @Test
    void seActualizaUnMediumInexistenteYNoSeLanzaNingunaExcepcion() {

        assertDoesNotThrow(() -> mediumService.actualizar(medium));
    }

    //Test feature recuperar


    @Test
    void alRecuperarUnMediumQueNoEstaGuardadoSeRetornaEmpty() {
        Long idInexistente = 333L;

        assertTrue(mediumService.recuperar(idInexistente).isEmpty());

    }

    @AfterEach
    void eliminarModelo() {
        testService.eliminarModelo(Medium.class);
    }


}
