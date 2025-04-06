package ar.edu.unq.epergeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateUbicacionDAO;
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
    private UbicacionServiceImpl service;
    private HibernateUbicacionDAO ubicacionDAO;
    private Ubicacion ubicacionUno;
    private Ubicacion ubicacionDos;
    private Ubicacion ubicacionTres;
    private Espiritu espirituUno;
    private Medium mediumUno;
    private Espiritu espirituDos;
    private Medium mediumDos;
    private Espiritu espirituTres;
    private Medium mediumTres;
    private Service limpiador;

    @BeforeEach
    void prepare() {
        this.ubicacionDAO = new HibernateUbicacionDAO();
        this.ubicacionUno = new Ubicacion("Bernal");
        this.ubicacionDos = new Ubicacion("Bernal");
        this.ubicacionTres = new Ubicacion("Avellaneda");
        this.service = new UbicacionServiceImpl(this.ubicacionDAO);
        this.limpiador = new Service();
    }

    @Test
    void sePersisteUnaUbicacionEnLaBaseDeDatos() {
        this.service.crear(this.ubicacionUno);
        Ubicacion ubicacionGuardada = this.service.recuperar(this.ubicacionUno.getId()).get();

        assertEquals(this.ubicacionUno.getId(), ubicacionGuardada.getId());
        assertEquals(this.ubicacionUno.getNombre(), ubicacionGuardada.getNombre());
        assertNotEquals(this.ubicacionUno, ubicacionGuardada);
    }

    @Test
    void sePersisteDosUbicacionesConElMismoNombre_entoncesNoSePuedePersistir() {
        this.service.crear(this.ubicacionUno);

        assertThrows(PersistenceException.class, () -> this.service.crear(this.ubicacionDos));
    }

    @Test
    void seIntentaRecuperarUnaUbicacionNoPersistida_entoncesLanzaUnaExcepcion() {
        this.ubicacionUno.setId(1L);

        assertFalse(this.service.recuperar(this.ubicacionUno.getId()).isPresent());
    }

    @Test
    void seActualizaUnaUbicacionPersistidaEnLaBaseDeDatos() {
        this.service.crear(this.ubicacionUno);
        this.ubicacionUno.setNombre("Quilmes");
        this.service.actualizar(this.ubicacionUno);
        Ubicacion ubicacionGuardada = this.service.recuperar(this.ubicacionUno.getId()).get();

        assertEquals(this.ubicacionUno.getNombre(), ubicacionGuardada.getNombre());
        assertEquals(this.ubicacionUno.getId(), ubicacionGuardada.getId());
    }

    @Test
    void seIntentaActualizarElNombredeUnaUbicacionYaRegistradaEnLaBaseDeDatos_entoncesLanzaUnaExcepcion() {
        this.service.crear(this.ubicacionUno);
        this.service.crear(this.ubicacionTres);
        this.ubicacionTres.setNombre("Bernal");

        assertThrows(PersistenceException.class, () -> this.service.actualizar(this.ubicacionTres));
    }

    @Test
    void seEliminaUnaUbicacionPersistidaEnLaBaseDeDatos() {
        this.service.crear(this.ubicacionUno);
        this.service.eliminar(this.ubicacionUno);

        assertFalse(this.service.recuperar(this.ubicacionUno.getId()).isPresent());
    }

    @Test
    void seRecuperanTodasLasUbicacionesPersistidasDeLaBasesDeDatos() {
        this.service.crear(this.ubicacionUno);
        this.ubicacionDos.setNombre("Quilmes");
        this.service.crear(this.ubicacionDos);
        this.service.crear(this.ubicacionTres);
        List<Ubicacion> ubicaciones = this.service.recuperarTodos();

        assertEquals(ubicaciones.getFirst().getId(), this.ubicacionUno.getId());
        assertEquals(ubicaciones.get(1).getId(), this.ubicacionDos.getId());
        assertEquals(ubicaciones.getLast().getId(), this.ubicacionTres.getId());
        assertEquals(3, ubicaciones.size());
    }

    @Test
    void seRecuperanLasUbicacionesDeUnaBaseDeDatosVacia() {
        List<Ubicacion> ubicaciones = this.service.recuperarTodos();

        assertEquals(0, ubicaciones.size());
    }

    @AfterEach
    void cleanUp() {
        this.limpiador.eliminarModelo(Ubicacion.class);
    }
}