package ar.edu.unq.epergeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.RandomGenerator.RandomGenerator;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.ExorcistaSinAngelesException;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
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

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class MediumServiceImplTest {

    private MediumServiceImpl mediumService;
    private TestService testService = new TestService();
    private EspirituServiceImpl espirituService;

    private Medium medium;
    private Medium medium2;

    private Angel angelDebil;
    private Angel angelFuerte;

    private Demonio demonioDebil;
    private Demonio demonioFuerte;


    private Ubicacion ubicacion;
    private UbicacionServiceImpl ubicacionService;
    private HibernateUbicacionDAO ubicacionDAO;
    private HibernateEspirituDAO espirituDAO;
    private HibernateMediumDAO mediumDAO;




    @BeforeEach
    void crearMedium(){
        this.ubicacionDAO = new HibernateUbicacionDAO();
        this.espirituDAO = new HibernateEspirituDAO();
        this.mediumDAO = new HibernateMediumDAO();


        this.ubicacion = new Ubicacion("Bernal");
        mediumService = new MediumServiceImpl(mediumDAO);
        espirituService = new EspirituServiceImpl(espirituDAO,mediumDAO);
        this.ubicacionService = new UbicacionServiceImpl(this.ubicacionDAO, this.espirituDAO, this.mediumDAO);

        ubicacionService.crear(ubicacion);

        medium = new Medium("Max",100,50, this.ubicacion);
        medium2 = new Medium("Pepe",100,0, this.ubicacion);

        mediumService = new MediumServiceImpl(new HibernateMediumDAO(),new HibernateEspirituDAO());
        espirituService = new EspirituServiceImpl(new HibernateEspirituDAO(),new HibernateMediumDAO());

        angelDebil = new Angel(10,"gabriel",this.ubicacion);
        angelFuerte = new Angel(60,"Rafael",this.ubicacion);

        demonioDebil = new Demonio(5,"Rufi",this.ubicacion);
        demonioFuerte = new Demonio(50,"Adam",this.ubicacion);



    }
    @Test
    void seRecuperanTodosLosMediumsGuardados(){
        this.mediumService.crear(medium);
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



    @Test
    void alRecuperarUnMediumQueNoEstaGuardadoSeRetornaEmpty() {
        Long idInexistente = 333L;

        assertTrue(mediumService.recuperar(idInexistente).isEmpty());

    }

    @Test
    void seHaceDescansarAUnMediumRecuperandoManaYSuEspirituRecuperaConexion(){

        mediumService.crear(medium);
        espirituService.crear(angelDebil);

        espirituService.conectar(angelDebil.getId(), medium.getId());


        mediumService.descansar(medium.getId());

        Medium recuperado = mediumService.recuperar(medium.getId()).get();
        Espiritu recuperado1 = espirituService.recuperar(angelDebil.getId()).get();

        assertEquals(65,recuperado.getMana());

        assertEquals(25, recuperado1.getNivelDeConexion());
    }

    @Test
    void seIntentaHacerDescansarAUnMediumQueNoFuePersistidoYLanzaExepcion(){
        assertThrows(NoSuchElementException.class, () -> {
            mediumService.descansar(-1L);
        });
    }


    @Test
    void seRecuperanTodosLosEspiritusConLosQueEstaConectadoUnMedium() {
        mediumService.crear(medium);
        espirituService.crear(angelDebil);
        espirituService.crear(demonioDebil);

        medium.conectarseAEspiritu(angelDebil);
        angelDebil.setMedium(medium);
        medium.conectarseAEspiritu(demonioDebil);
        demonioDebil.setMedium(medium);

        mediumService.actualizar(medium);
        espirituService.actualizar(angelDebil);
        espirituService.actualizar(demonioDebil);

        List<Espiritu> espirituRecuperados = mediumService.espiritus(medium.getId());

        assertEquals(2, espirituRecuperados.size());

    }

    @Test
    void alExorcizarAUnMediumElAngelGanaYDisminuyeNivelDeConexionDelDemonio(){

        RandomGenerator.addValorFijo(10);
        RandomGenerator.addValorFijo(1);

        mediumService.crear(medium);
        mediumService.crear(medium2);

        espirituService.crear(angelFuerte);
        espirituService.crear(demonioFuerte);

        espirituService.conectar(angelFuerte.getId(),medium.getId());

        espirituService.conectar(demonioFuerte.getId(),medium2.getId());

        mediumService.exorcisar(medium.getId(),medium2.getId());

        Espiritu demonioRecuperado = espirituService.recuperar(demonioFuerte.getId()).get();
        Espiritu angelRecuperado = espirituService.recuperar(angelFuerte.getId()).get();

        assertEquals(25,demonioRecuperado.getNivelDeConexion());
        assertEquals(70,angelRecuperado.getNivelDeConexion());

    }

    @Test
    void alExorcizarAUnMediumElAngelPierdeYDisminuyeNivelDeConexionDelAngel(){

        RandomGenerator.addValorFijo(1);
        RandomGenerator.addValorFijo(100);

        mediumService.crear(medium);
        mediumService.crear(medium2);

        espirituService.crear(angelDebil);
        espirituService.crear(demonioFuerte);

        espirituService.conectar(angelDebil.getId(),medium.getId());

        espirituService.conectar(demonioFuerte.getId(),medium2.getId());

        mediumService.exorcisar(medium.getId(),medium2.getId());

        Espiritu demonioRecuperado = espirituService.recuperar(demonioFuerte.getId()).get();
        Espiritu angelRecuperado = espirituService.recuperar(angelDebil.getId()).get();

        assertEquals(60,demonioRecuperado.getNivelDeConexion());
        assertEquals(15,angelRecuperado.getNivelDeConexion());


    }

    @Test
    void alExorcizarAUnMediumElPrimerAngelVenceAlPrimerDemonioYElSegundoAngelPierdeContraElSegundoDemonio(){

        RandomGenerator.addValorFijo(10);
        RandomGenerator.addValorFijo(1);
        RandomGenerator.addValorFijo(1);
        RandomGenerator.addValorFijo(100);

        mediumService.crear(medium);
        mediumService.crear(medium2);

        espirituService.crear(angelFuerte);
        espirituService.crear(angelDebil);

        espirituService.crear(demonioFuerte);
        espirituService.crear(demonioDebil);

        espirituService.conectar(angelFuerte.getId(),medium.getId());
        espirituService.conectar(angelDebil.getId(),medium.getId());

        espirituService.conectar(demonioDebil.getId(),medium2.getId());
        espirituService.conectar(demonioFuerte.getId(),medium2.getId());

        mediumService.exorcisar(medium.getId(),medium2.getId());


        Espiritu angelFuerteRecuperado = espirituService.recuperar(angelFuerte.getId()).get();
        Espiritu angelDebilRecuperado = espirituService.recuperar(angelDebil.getId()).get();
        Espiritu demonioFuerteRecuperado = espirituService.recuperar(demonioFuerte.getId()).get();
        Espiritu demonioDebilRecuperado = espirituService.recuperar(demonioDebil.getId()).get();


        assertEquals(0,demonioDebilRecuperado.getNivelDeConexion());
        assertEquals(60,demonioFuerteRecuperado.getNivelDeConexion());

        assertEquals(15,angelDebilRecuperado.getNivelDeConexion());
        assertEquals(70,angelFuerteRecuperado.getNivelDeConexion());

    }

    @Test
    void alIntentarExorcizarSinAngelesLanzaUnError(){
        mediumService.crear(medium);
        mediumService.crear(medium2);

        assertThrows(ExorcistaSinAngelesException.class, () -> {
            mediumService.exorcisar(medium.getId(),medium2.getId());
        });
    }

    @Test
    void seIntentaExorcizarAUnMediumQueNoFuePersistidoYLanzaUnError(){

        mediumService.crear(medium);
        espirituService.crear(angelDebil);

        espirituService.conectar(angelDebil.getId(),medium.getId());

        assertThrows(NoSuchElementException.class, () -> {
            mediumService.exorcisar(medium.getId(),-1L);
        });
    }

    @Test
    void unMediumNoPersistidoIntentaExorcizarAOtroYLanzaUnError(){

        mediumService.crear(medium2);
        espirituService.crear(demonioDebil);

        espirituService.conectar(demonioDebil.getId(),medium2.getId());

        assertThrows(NoSuchElementException.class, () -> {
            mediumService.exorcisar(-1L,medium2.getId());
        });
    }

    @AfterEach
    void eliminarModelo() {
        testService.eliminarModelo(Espiritu.class);
        testService.eliminarModelo(Medium.class);
        testService.eliminarModelo(Ubicacion.class);
    }


}
