package ar.edu.unq.epergeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateMediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateUbicacionDAO;
import ar.edu.unq.epersgeist.servicios.impl.EspirituServiceImpl;
import ar.edu.unq.epersgeist.servicios.impl.MediumServiceImpl;
import ar.edu.unq.epersgeist.servicios.impl.UbicacionServiceImpl;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UbicacionServiceTest {
    private UbicacionServiceImpl ubicacionService;
    private EspirituServiceImpl espirituService;
    private MediumServiceImpl mediumService;
    private HibernateUbicacionDAO ubicacionDAO;
    private HibernateEspirituDAO espirituDAO;
    private HibernateMediumDAO mediumDAO;
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
    private TestService limpiador;

    @BeforeEach
    void prepare() {
        this.ubicacionDAO = new HibernateUbicacionDAO();
        this.espirituDAO = new HibernateEspirituDAO();
        this.mediumDAO = new HibernateMediumDAO();
        this.ubicacionUno = new Ubicacion("Bernal");
        this.ubicacionDos = new Ubicacion("Bernal");
        this.ubicacionTres = new Ubicacion("Avellaneda");
        this.ubicacionService = new UbicacionServiceImpl(this.ubicacionDAO, this.espirituDAO, this.mediumDAO);
        this.espirituService = new EspirituServiceImpl(this.espirituDAO,new HibernateMediumDAO());
        this.mediumService = new MediumServiceImpl(this.mediumDAO);
        this.limpiador = new TestService();
        this.angelUno = new Angel(10, "angelical");
        this.angelDos = new Angel(20, "angel");
        this.demonioUno = new Demonio(10, "demonio");
        this.demonioDos = new Demonio(20, "demoni");
        this.mediumUno = new Medium("Fabian", 100, 10);
        this.mediumDos = new Medium("Franco", 200, 50);
        this.mediumTres = new Medium("Tobias", 300, 60);
    }

    @Test
    void sePersisteUnaUbicacionEnLaBaseDeDatos() {
        Ubicacion ubicacionPersistida =this.ubicacionService.crear(ubicacionUno);

        assertEquals(this.ubicacionUno.getId(), ubicacionPersistida.getId());
        assertEquals(this.ubicacionUno.getNombre(), ubicacionPersistida.getNombre());
    }

    @Test
    void sePersisteDosUbicacionesConElMismoNombre_entoncesNoSePuedePersistir() {
        this.ubicacionService.crear(this.ubicacionUno);

        assertThrows(PersistenceException.class, () -> this.ubicacionService.crear(this.ubicacionDos));
    }

    @Test
    void seIntentaRecuperarUnaUbicacionNoPersistidaYNoDevuelveNada() {
        this.ubicacionUno.setId(1L);

        assertFalse(this.ubicacionService.recuperar(this.ubicacionUno.getId()).isPresent());
    }

    @Test
    void seActualizaUnaUbicacionPersistidaEnLaBaseDeDatos() {
        Ubicacion ubicacionPersistida = this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionUno.setNombre("Quilmes");
        this.ubicacionService.actualizar(this.ubicacionUno);

        assertEquals(this.ubicacionUno.getNombre(), ubicacionPersistida.getNombre());
        assertEquals(this.ubicacionUno.getId(), ubicacionPersistida.getId());
    }

    @Test
    void seIntentaActualizarElNombredeUnaUbicacionYaRegistradaEnLaBaseDeDatos_entoncesLanzaUnaExcepcion() {
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.crear(this.ubicacionTres);
        this.ubicacionTres.setNombre("Bernal");

        assertThrows(PersistenceException.class, () -> this.ubicacionService.actualizar(this.ubicacionTres));
    }

    @Test
    void seEliminaUnaUbicacionPersistidaEnLaBaseDeDatosPorLoQueYaNoExiste() {
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.eliminar(this.ubicacionUno.getId());

        assertFalse(this.ubicacionService.recuperar(this.ubicacionUno.getId()).isPresent());
    }

    @Test
    void seRecuperanTodasLasUbicacionesPersistidasDeLaBasesDeDatos() {
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionDos.setNombre("Quilmes");
        this.ubicacionService.crear(this.ubicacionDos);
        this.ubicacionService.crear(this.ubicacionTres);
        List<Ubicacion> ubicaciones = this.ubicacionService.recuperarTodos();

        assertEquals(ubicaciones.getFirst().getId(), this.ubicacionUno.getId());
        assertEquals(ubicaciones.get(1).getId(), this.ubicacionDos.getId());
        assertEquals(ubicaciones.getLast().getId(), this.ubicacionTres.getId());
        assertEquals(3, ubicaciones.size());
    }

    @Test
    void seRecuperanLasUbicacionesDeUnaBaseDeDatosVaciaYNoDevuelveNingunaUbicacion() {
        List<Ubicacion> ubicaciones = this.ubicacionService.recuperarTodos();

        assertEquals(0, ubicaciones.size());
    }

    @Test
    void seObtieneLosEspiritusExistentesEnDistintasUbicaciones() {
        this.ubicacionService.crear(this.ubicacionUno);
        this.ubicacionService.crear(this.ubicacionTres);
        this.espirituService.crear(this.angelUno);
        this.espirituService.crear(this.angelDos);
        this.espirituService.crear(this.demonioUno);
        this.espirituService.crear(this.demonioDos);
        this.angelUno.setUbicacion(this.ubicacionUno);
        this.angelDos.setUbicacion(this.ubicacionUno);
        this.demonioUno.setUbicacion(this.ubicacionTres);
        this.demonioDos.setUbicacion(this.ubicacionUno);
        this.espirituService.actualizar(this.angelUno);
        this.espirituService.actualizar(this.angelDos);
        this.espirituService.actualizar(this.demonioUno);
        this.espirituService.actualizar(this.demonioDos);

        assertEquals(3, this.ubicacionService.espiritusEn(ubicacionUno.getId()).size());
        assertEquals(this.angelUno.getId(), this.ubicacionService.espiritusEn(ubicacionUno.getId()).get(1).getId());
        assertEquals(this.angelDos.getId(), this.ubicacionService.espiritusEn(ubicacionUno.getId()).getFirst().getId());
        assertEquals(this.demonioDos.getId(), this.ubicacionService.espiritusEn(ubicacionUno.getId()).getLast().getId());
        assertEquals(1, this.ubicacionService.espiritusEn(ubicacionTres.getId()).size());
        assertEquals(this.demonioUno.getId(), this.ubicacionService.espiritusEn(ubicacionTres.getId()).getFirst().getId());
    }

    @Test
    void seObtieneLosMediumsDeUnaUbicacionQueNoTenganEspiritusConectados() {
        this.ubicacionService.crear(this.ubicacionUno);
        this.mediumService.crear(this.mediumUno);
        this.mediumService.crear(this.mediumDos);
        this.mediumService.crear(this.mediumTres);
        this.mediumUno.setUbicacion(this.ubicacionUno);
        this.mediumDos.setUbicacion(this.ubicacionUno);
        this.mediumTres.setUbicacion(this.ubicacionUno);
        this.mediumUno.conectarseAEspiritu(this.angelUno);
        this.mediumDos.conectarseAEspiritu(this.demonioUno);
        this.mediumService.actualizar(this.mediumUno);
        this.mediumService.actualizar(this.mediumDos);
        this.mediumService.actualizar(this.mediumTres);
        List<Medium> mediumsSinEspiritus = this.ubicacionService.mediumsSinEspiritusEn(this.ubicacionUno.getId());

        assertEquals(1, mediumsSinEspiritus.size());
        assertEquals(this.mediumTres.getId(),mediumsSinEspiritus.getFirst().getId());
    }

    @AfterEach
    void cleanUp() {
        this.limpiador.eliminarModelo(Espiritu.class);
        this.limpiador.eliminarModelo(Medium.class);
        this.limpiador.eliminarModelo(Ubicacion.class);
    }
}