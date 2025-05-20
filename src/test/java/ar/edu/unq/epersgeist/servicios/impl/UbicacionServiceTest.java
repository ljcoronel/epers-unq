package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.exception.NombreRepetidoException;
import ar.edu.unq.epersgeist.modelo.exception.EntidadTodaviaReferenciadaException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.persistencia.dao.neo4j.UbicacionDAONEO;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import ar.edu.unq.epersgeist.testUtils.TestService;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class UbicacionServiceTest {

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private EspirituService espirituService;

    @Autowired
    private MediumService mediumService;

    @Autowired
    private UbicacionDAONEO ubicacionDAONEO;

    private Ubicacion ubicacionUno;
    private Ubicacion ubicacionDos;
    private Ubicacion ubicacionTres;

    private Angel angelUno;
    private Angel angelDos;

    private Demonio demonioUno;
    private Demonio demonioDos;

    private Medium mediumUno;
    private Medium mediumDos;
    private Medium mediumTres;

    @Autowired
    private TestService limpiador;

    @BeforeEach
    void prepare() {
        this.ubicacionUno = new Santuario("Bernal", 50);
        this.ubicacionDos = new Santuario("Bernal", 50);
        this.ubicacionTres = new Santuario("Avellaneda", 50);

        this.angelUno = new Angel(10, "angelical", this.ubicacionUno);
        this.angelDos = new Angel(20, "angel", this.ubicacionUno);

        this.demonioUno = new Demonio(10, "demonio", this.ubicacionUno);
        this.demonioDos = new Demonio(20, "demoni",this.ubicacionUno);

        this.mediumUno = new Medium("Fabian", 100, 10, this.ubicacionUno);
        this.mediumDos = new Medium("Franco", 200, 50, this.ubicacionUno);
        this.mediumTres = new Medium("Tobias", 300, 60, this.ubicacionUno);
    }

    @Test
    public void sePersisteUnaUbicacionEnLaBaseDeDatos() {
        Ubicacion ubicacionPersistida = this.ubicacionService.crear(ubicacionUno);
        assertEquals(this.ubicacionUno.getId(), ubicacionPersistida.getId());
        assertEquals(this.ubicacionUno.getNombre(), ubicacionPersistida.getNombre());

    }

    @Test
    public void sePersisteDosUbicacionesConElMismoNombreEntoncesNoSePuedePersistir() {
        this.ubicacionService.crear(this.ubicacionUno);

        assertThrows(NombreRepetidoException.class, () -> this.ubicacionService.crear(this.ubicacionDos));
    }

    @Test
    public void seIntentaRecuperarUnaUbicacionNoPersistidaYNoDevuelveNada() {
        this.ubicacionUno.setId(1L);

        assertFalse(this.ubicacionService.recuperar(this.ubicacionUno.getId()).isPresent());
    }

    @Test
    public void seActualizaUnaUbicacionPersistidaEnLaBaseDeDatos() {
        Ubicacion ubicacionPersistida = this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionUno.setNombre("Quilmes");
        this.ubicacionService.actualizar(this.ubicacionUno);

        assertEquals(this.ubicacionUno.getNombre(), ubicacionPersistida.getNombre());
        assertEquals(this.ubicacionUno.getId(), ubicacionPersistida.getId());
    }

    @Test
    public void seIntentaActualizarElNombredeUnaUbicacionYaRegistradaEnLaBaseDeDatosEntoncesLanzaUnaExcepcion() {
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.crear(this.ubicacionTres);
        this.ubicacionTres.setNombre("Bernal");

        assertThrows(NombreRepetidoException.class, () -> this.ubicacionService.actualizar(this.ubicacionTres));
    }

    @Test
    public void seIntentaActualizarUnaUbicacionQueNoExisteEnLaBaseDeDatosEntoncesNoSeActualiza() {
        this.ubicacionUno.setId(1L);
        this.ubicacionUno.setNombre("Quilmes");
        this.ubicacionService.actualizar(this.ubicacionUno);

        assertFalse(this.ubicacionService.recuperar(this.ubicacionUno.getId()).isPresent());
    }

    @Test
    public void seEliminaUnaUbicacionPersistidaEnLaBaseDeDatosPorLoQueYaNoExiste() {
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.eliminar(this.ubicacionUno.getId());

        assertFalse(this.ubicacionService.recuperar(this.ubicacionUno.getId()).isPresent());
    }

    @Test
    public void noSePuedeEliminarUnaUbicacionTodaviaReferenciada() {
        this.ubicacionService.crear(this.ubicacionUno);
        this.mediumService.crear(mediumUno);

        assertThrows(EntidadTodaviaReferenciadaException.class, () -> this.ubicacionService.eliminar(this.ubicacionUno.getId()));
    }

    @Test
    public void seRecuperanTodasLasUbicacionesPersistidasDeLaBasesDeDatos() {
        this.ubicacionDos.setNombre("Quilmes");

        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.crear(this.ubicacionDos);
        this.ubicacionService.crear(this.ubicacionTres);

        List<Ubicacion> ubicaciones = this.ubicacionService.recuperarTodos();

        assertEquals(ubicaciones.getFirst().getId(), this.ubicacionUno.getId());
        assertEquals(ubicaciones.get(1).getId(), this.ubicacionDos.getId());
        assertEquals(ubicaciones.getLast().getId(), this.ubicacionTres.getId());
        assertEquals(3, ubicaciones.size());
    }

    @Test
    public void seRecuperanLasUbicacionesDeUnaBaseDeDatosVaciaYNoDevuelveNingunaUbicacion() {
        List<Ubicacion> ubicaciones = this.ubicacionService.recuperarTodos();

        assertEquals(0, ubicaciones.size());
    }


    @Test
    public void seObtieneLosMediumsDeUnaUbicacionQueNoTenganEspiritusConectados() {
        this.ubicacionService.crear(this.ubicacionUno);
        this.mediumService.crear(this.mediumUno);
        this.mediumService.crear(this.mediumDos);
        this.mediumService.crear(this.mediumTres);

        this.mediumService.mover(mediumUno.getId(), ubicacionUno.getId());
        this.mediumService.mover(mediumDos.getId(), ubicacionUno.getId());
        this.mediumService.mover(mediumTres.getId(), ubicacionUno.getId());

        espirituService.crear(this.angelUno);
        espirituService.crear(this.demonioUno);

        espirituService.conectar(this.angelUno.getId(), mediumUno.getId());
        espirituService.conectar(this.demonioUno.getId(), mediumDos.getId());

        this.mediumService.actualizar(this.mediumUno);
        this.mediumService.actualizar(this.mediumDos);
        this.mediumService.actualizar(this.mediumTres);

        List<Medium> mediumsSinEspiritus = this.ubicacionService.mediumsSinEspiritusEn(this.ubicacionUno.getId());

        assertEquals(1, mediumsSinEspiritus.size());
        assertEquals(this.mediumTres.getId(),mediumsSinEspiritus.getFirst().getId());
    }

    @Test
    public void seVerificaQueUnAngelNoPuedeCambiarDeUbicacionSiEstaConectadoAUnMedium() {
        Ubicacion ubicacion1 = new Santuario("ElClerigo",50);
        Ubicacion ubicacion2 = new Santuario("Masedonia",50);
        ubicacionService.crear(ubicacion1);
        ubicacionService.crear(ubicacion2);
        Medium medium = new Medium("Franco", 200, 50, ubicacion1);
        Espiritu espiritu = new Angel(10, "angelical", ubicacion1);
        espirituService.crear(espiritu);
        mediumService.crear(medium);

        espirituService.conectar(espiritu.getId(), medium.getId());
        espiritu.mover(ubicacion2);

        assertNotEquals(espiritu.getUbicacion().getNombre(), ubicacion1.getNombre());
    }

    @Test
    public void seIntentaObtenerLosEspiritusDeUnaUbicacionQueNoExisteEnLaBaseDeDatosYDevuelveUnaListaVacia() {
        this.ubicacionUno.setId(1L);

        assertTrue(this.ubicacionService.espiritusEn(this.ubicacionUno.getId()).isEmpty());
    }

    @Test
    public void seIntentaObtenerLosMediumsSinEspiritusDeUnaUbicacionQueNoExisteEnLaBaseDeDatosYDevuelveUnaListaVacia() {
        this.ubicacionUno.setId(1L);

        assertTrue(this.ubicacionService.mediumsSinEspiritusEn(this.ubicacionUno.getId()).isEmpty());
    }

    @Test
    public void unaUbicacionNoPuedeSerActualizadaConMasDe100DeEnergia() {
        Ubicacion ubicacion = this.ubicacionService.crear(this.ubicacionUno);
        ubicacion.setEnergia(900);
        ubicacionService.actualizar(ubicacion);
        Ubicacion ubicacionActualizada = ubicacionService.recuperar(ubicacion.getId()).get();

        assertEquals(100, ubicacionActualizada.getEnergia());
    }

    @Test
    public void unaUbicacionNoPuedeSerActualizadaConMenosDe0DeEnergia() {
        Ubicacion ubicacion = this.ubicacionService.crear(this.ubicacionUno);
        ubicacion.setEnergia(-50);
        ubicacionService.actualizar(ubicacion);
        Ubicacion ubicacionActualizada = ubicacionService.recuperar(ubicacion.getId()).get();

        assertEquals(0, ubicacionActualizada.getEnergia());
    }

    @Test
    public void unaUbicacionPersisteSuEnergia() {
        Ubicacion ubicacion = this.ubicacionService.crear(new Santuario("Chocolandia", 75));
        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(ubicacion.getId()).get();

        assertEquals(75, ubicacionRecuperada.getEnergia());
    }

    @AfterEach
    public void tearDown() {
        this.limpiador.eliminarEspiritus();
        this.limpiador.eliminarMediums();
        this.limpiador.eliminarUbicaciones();
        this.limpiador.eliminarUbicacionesNeo();
    }
}