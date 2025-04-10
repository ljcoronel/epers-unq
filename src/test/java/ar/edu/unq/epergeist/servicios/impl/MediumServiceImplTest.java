package ar.edu.unq.epergeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateMediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateUbicacionDAO;
import ar.edu.unq.epersgeist.servicios.impl.EspirituServiceImpl;
import ar.edu.unq.epersgeist.servicios.impl.MediumServiceImpl;
import ar.edu.unq.epersgeist.servicios.impl.TestService;
import ar.edu.unq.epersgeist.servicios.impl.UbicacionServiceImpl;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class MediumServiceImplTest {

    private MediumServiceImpl mediumService;
    private TestService testService= new TestService();
    private EspirituServiceImpl espirituService;

    private Medium medium;
    private Medium medium2;

    private Angel angel1;
    private Ubicacion ubicacion;
    private UbicacionServiceImpl ubicacionService;
    private MediumServiceImpl mediumServiceImpl;
    private HibernateUbicacionDAO ubicacionDAO;
    private HibernateEspirituDAO espirituDAO;
    private HibernateMediumDAO mediumDAO;

    @BeforeEach
    void crearMedium(){
        this.ubicacion = new Ubicacion("Bernal");
        mediumService = new MediumServiceImpl(new HibernateMediumDAO());
        espirituService = new EspirituServiceImpl(new HibernateEspirituDAO(),new HibernateMediumDAO());

        medium = new Medium("Max",100,50, this.ubicacion);
        medium2 = new Medium("Pepe",100,0, this.ubicacion);

        angel1 = new Angel(10,"gabriel", this.ubicacion);
        this.ubicacionDAO = new HibernateUbicacionDAO();
        this.espirituDAO = new HibernateEspirituDAO();
        this.mediumDAO = new HibernateMediumDAO();
        this.ubicacionService = new UbicacionServiceImpl(this.ubicacionDAO, this.espirituDAO, this.mediumDAO);


    }
    @Test
    void seRecuperanTodosLosMediumsGuardados(){
        this.ubicacionService.crear(this.ubicacion);
        this.
        mediumService.crear(medium);
        mediumService.crear(medium2);

        var mediums = mediumService.recuperarTodos();

        assertEquals(2, mediums.size());
    }

    @Test
    void alRecuperarTodosLosMediumsEstandoLaBaseDeDatosVaciaRetornaListaVacia(){

        var mediums = mediumService.recuperarTodos();

        assertTrue(mediums.isEmpty());
    }

    @Test
    void seEliminaUnMediumDejaDeEstarPersistiendo(){
        this.ubicacionService.crear(this.ubicacion);
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
        this.ubicacionService.crear(this.ubicacion);
        mediumService.crear(medium);

        Medium mediumRecuperado =  mediumService.recuperar(medium.getId()).get();

        assertEquals(medium.getId(), mediumRecuperado.getId());
        assertEquals(medium.getMana(),mediumRecuperado.getMana());
        assertEquals(medium.getManaMax(), mediumRecuperado.getManaMax());
        assertEquals(medium.getNombre(), mediumRecuperado.getNombre());
    }

    @Test
    void alCrearUnMediumSeLeAgregaUnId() {
        this.ubicacionService.crear(this.ubicacion);
        assertNull(medium.getId());

        mediumService.crear(medium);

        assertNotNull(medium.getId());
    }
    
    
    //feature actualizar

    @Test
    void seActualizanUnMediumYAlRecuperarloLosCambiosSeVenReflejados() {
        this.ubicacionService.crear(this.ubicacion);
        mediumService.crear(medium);
        medium.setNombre("nombreModificado");

        mediumService.actualizar(medium);
        Medium recuperado = mediumService.recuperar(medium.getId()).get();

        assertEquals("nombreModificado", recuperado.getNombre());
    }

    @Test
    void seActualizaUnMediumInexistenteYNoSeLanzaNingunaExcepcion() {
        this.ubicacionService.crear(this.ubicacion);
        assertDoesNotThrow(() -> mediumService.actualizar(medium));
    }

    //Test feature recuperar


    @Test
    void alRecuperarUnMediumQueNoEstaGuardadoSeRetornaEmpty() {
        Long idInexistente = 333L;

        assertTrue(mediumService.recuperar(idInexistente).isEmpty());

    }

    @Test
    void seHaceDescansarAUnMediumRecuperandoManaYSuEspirituRecuperaConexion(){
        this.ubicacionService.crear(this.ubicacion);
        mediumService.crear(medium);
        espirituService.crear(angel1);

        espirituService.conectar(angel1.getId(), medium.getId());//el nivel de conexion del angel es de 20 por la conexion
        //podria agregarse un espiritu mas para probar


        mediumService.descansar(medium.getId());

        Medium recuperado = mediumService.recuperar(medium.getId()).get();
        Espiritu recuperado1 = espirituService.recuperar(angel1.getId()).get();

        assertEquals(65,recuperado.getMana());

        assertEquals(25, recuperado1.getNivelDeConexion());
    }

    @Test
    void seIntentaHacerDescansarAUnMediumQueNoFuePersistidoYLanzaExepcion(){
        assertThrows(NoSuchElementException.class, () -> {
            mediumService.descansar(-1L);
        });
    }

    @AfterEach
    void eliminarModelo() {
        testService.eliminarModelo(Espiritu.class);
        testService.eliminarModelo(Medium.class);
        testService.eliminarModelo(Ubicacion.class);
    }


}
