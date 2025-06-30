package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.PortalDemonios;
import ar.edu.unq.epersgeist.modelo.geo.Coordenada;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Reporte.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.influx.EventoPortalDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import ar.edu.unq.epersgeist.testUtils.TestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EstadisticaServiceImplTest {

    @Autowired
    private EstadisticaService estadisticaService;

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private EspirituService espirituService;

    @Autowired
    private MediumService mediumService;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    private Espiritu demonio1;
    private Espiritu demonio2;
    private Espiritu demonio3;
    private Espiritu demonio4;
    private Espiritu demonio5;
    private Espiritu demonio6;

    private Medium medium;
    private Medium medium2;
    private Espiritu angel1;

    private Ubicacion ubicacion;
    private Ubicacion ubicacion2;
    private Ubicacion ubicacion3;

    @Autowired
    private TestService testService;

    @BeforeEach
    void crearInstancias() {
        this.ubicacion = new Santuario("Bernal", 60, List.of(
                new Coordenada(0, 0),
                new Coordenada(1, 0),
                new Coordenada(1, 1),
                new Coordenada(0, 1),
                new Coordenada(0, 0)
        ), 5);
        this.ubicacion2 = new Santuario("Quilmes",60,List.of(
                new Coordenada(3, 0),
                new Coordenada(4, 0),
                new Coordenada(4, 1),
                new Coordenada(3, 1),
                new Coordenada(3, 0)
        ), 10);
        this.ubicacion3 = new Cementerio("Solano",60,List.of(
                new Coordenada(6, 0),
                new Coordenada(7, 0),
                new Coordenada(7, 1),
                new Coordenada(6, 1),
                new Coordenada(6, 0)
        ), -5);

        angel1 = new Angel(10,"Angel1",new Coordenada(0.5,0.5));
        demonio1 = new Demonio(10, "Pepito",new Coordenada(0.5,0.5));
        demonio2 = new Demonio(90, "Diavolo",new Coordenada(0.5,0.5));
        demonio3 = new Demonio(50, "Sebastián",new Coordenada(0.5,0.5));
        demonio4 = new Demonio(50, "Jorge",new Coordenada(0.5,0.5));
        demonio5 = new Demonio(50, "Jose",new Coordenada(3.5,0.5));
        demonio6 = new Demonio(40, "Dracula",new Coordenada(6.5,0.5));

        medium = new Medium("Brujita", 100, 50, new Coordenada(0.5,0.5));
        medium2 = new Medium("Pepe", 100, 60, new Coordenada(0.5,0.5));

    }

    @Test
    void seObtieneUnReporteDeUnSantuarioQueTieneEspiritusYMediums() {
        ubicacionService.crear(ubicacion);
        ubicacionService.crear(ubicacion2);

        mediumService.crear(medium);
        mediumService.crear(medium2);

        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);
        espirituService.crear(demonio4);
        espirituService.crear(demonio5);
        espirituService.crear(angel1);

        espirituService.conectar(demonio1.getId(),medium2.getId());
        espirituService.conectar(demonio2.getId(),medium2.getId());
        espirituService.conectar(demonio3.getId(),medium.getId());
        espirituService.conectar(angel1.getId(),medium.getId());

        ReporteSantuarioMasCorrupto reporte = estadisticaService.santuarioCorrupto();

        assertEquals("Bernal",reporte.getNombreSantuario());
        assertEquals(medium2.getId(),reporte.getMedium().getId());
        assertEquals(4,reporte.getCantidadDeDemonios());
        assertEquals(1,reporte.getCantidadDeDemoniosLibres());

    }

    @Test
    void seObtieneUnReporteDondeNoHayEspiritusEnElSantuario() {
        this.ubicacionService.crear(this.ubicacion3);
        this.espirituService.crear(this.demonio6);
        ReporteSantuarioMasCorrupto reporte = estadisticaService.santuarioCorrupto();

        assertTrue(reporte.getNombreSantuario().isEmpty());
        assertNull(reporte.getMedium());
        assertEquals(0,reporte.getCantidadDeDemonios());
        assertEquals(0,reporte.getCantidadDeDemoniosLibres());
    }

    @Test
    public void seObtieneUnReporteCuandoNoHaySantuario() {
        ReporteSantuarioMasCorrupto reporte = this.estadisticaService.santuarioCorrupto();

        assertTrue(reporte.getNombreSantuario().isEmpty());
        assertNull(reporte.getMedium());
        assertEquals(0,reporte.getCantidadDeDemonios());
        assertEquals(0,reporte.getCantidadDeDemoniosLibres());
    }

    @Test
    public void seObtieneUnSnapshotDeLaFechaDeHoy() {
        this.ubicacionService.crear(this.ubicacion);
        this.espirituService.crear(this.angel1);
        this.mediumService.crear(this.medium);

        this.estadisticaService.crearSnapshot();

        assertEquals(LocalDate.now().getYear(), this.estadisticaService.obtenerSnapshot(LocalDate.now()).getFecha().getYear());
    }

    @Test
    public void seObtieneElSnapshotMasRecienteDeLaFechaDeHoy() {
        this.ubicacionService.crear(this.ubicacion);
        this.mediumService.crear(this.medium);
        this.espirituService.crear(this.angel1);
        this.espirituService.crear(this.demonio1);
        this.espirituService.crear(this.demonio2);

        this.espirituService.conectar(this.angel1.getId(), this.medium.getId());
        this.espirituService.conectar(this.demonio2.getId(), this.medium.getId());
        this.espirituService.conectar(this.demonio1.getId(), this.medium.getId());
        this.estadisticaService.crearSnapshot();

        this.ubicacionService.crear(this.ubicacion2);
        this.ubicacionService.crear(this.ubicacion3);

        this.ubicacionService.conectar(this.ubicacion.getId(), this.ubicacion2.getId());
        this.ubicacionService.conectar(this.ubicacion.getId(), this.ubicacion3.getId());
        this.estadisticaService.crearSnapshot();

        this.ubicacionService.conectar(this.ubicacion2.getId(), this.ubicacion3.getId());
        this.ubicacionService.conectar(this.ubicacion3.getId(), this.ubicacion2.getId());
        this.estadisticaService.crearSnapshot();
        Snapshot snapshot = this.estadisticaService.obtenerSnapshot(LocalDate.now());

        assertEquals(3, snapshot.getNeo4j().size());
        assertEquals(7, snapshot.getMongo().size());
        assertEquals(7, snapshot.getSql().size());
    }

    @Test
    public void noSePuedeObtenerUnSnapshotMasViejoParaElMismoDia() {
        this.ubicacionService.crear(this.ubicacion);
        this.mediumService.crear(this.medium);
        this.espirituService.crear(this.angel1);
        this.espirituService.crear(this.demonio1);
        this.espirituService.crear(this.demonio2);

        this.espirituService.conectar(this.angel1.getId(), this.medium.getId());
        this.espirituService.conectar(this.demonio2.getId(), this.medium.getId());
        this.espirituService.conectar(this.demonio1.getId(), this.medium.getId());
        LocalDate fechaVieja = LocalDate.now();
        this.estadisticaService.crearSnapshot();

        this.ubicacionService.crear(this.ubicacion2);
        this.ubicacionService.crear(this.ubicacion3);

        this.ubicacionService.conectar(this.ubicacion.getId(), this.ubicacion2.getId());
        this.ubicacionService.conectar(this.ubicacion.getId(), this.ubicacion3.getId());
        this.estadisticaService.crearSnapshot();

        this.ubicacionService.conectar(this.ubicacion2.getId(), this.ubicacion3.getId());
        this.ubicacionService.conectar(this.ubicacion3.getId(), this.ubicacion2.getId());
        this.estadisticaService.crearSnapshot();
        Snapshot snapshot = this.estadisticaService.obtenerSnapshot(fechaVieja);

        assertEquals(3, snapshot.getNeo4j().size());
        assertEquals(7, snapshot.getMongo().size());
        assertEquals(7, snapshot.getSql().size());
    }

    @Test
    public void seObtieneUnValorNuloSiNoHaySnapshotsCreados() {
        assertNull(this.estadisticaService.obtenerSnapshot(LocalDate.now()));
    }

    @Test
    public void cuandoNoHayEventosDePortalesOcurridosDaListaVacia(){

        List<EventoPortalDTO> eventosPortal = estadisticaService.eventosDePortalesOcurridos();
        assertTrue(eventosPortal.isEmpty());
    }

    @Test
    public void seConsiguenLosEventosDePortalesOcurridosConUnSoloPortalYUnaUbicacion() {
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ubicacion.setTemperatura(20);
        this.ubicacionService.crear(ubicacion);
        espirituService.crear(demonio2);

        Long id1 = this.ubicacion.getId();

        ubicacionRepository.setTemperatura(ubicacion.getId(),17.8);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        espirituService.crearPortal(new PortalDemonios(new Coordenada(0.5, 0.5)));

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<EventoPortalDTO> eventosPortal = estadisticaService.eventosDePortalesOcurridos();

        assertEquals(1,eventosPortal.size());

        EventoPortalDTO evento = eventosPortal.get(0);

        assertEquals(id1, evento.getUbicacionId());

        assertTrue(evento.getIntensidad() == -5);

    }

    @Test
    public void seConsiguenLosEventosDePortalesOcurridosCon2PortalesOcurridosEnDistintasUbicacion() {
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ubicacion.setTemperatura(20);
        ubicacion2.setTemperatura(20);
        this.ubicacionService.crear(ubicacion);
        this.ubicacionService.crear(ubicacion2);
        demonio3.setCoordenada(new Coordenada(3.5, 0.5));
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        Long id1 = this.ubicacion.getId();
        Long id2 = this.ubicacion2.getId();

        ubicacionRepository.setTemperatura(ubicacion.getId(),17.8);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ubicacionRepository.setTemperatura(ubicacion2.getId(),17.8);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        espirituService.crearPortal(new PortalDemonios(new Coordenada(0.5, 0.5)));

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        espirituService.crearPortal(new PortalDemonios(new Coordenada(3.5, 0.5)));

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<EventoPortalDTO> eventosPortal = estadisticaService.eventosDePortalesOcurridos();

        assertEquals(2,eventosPortal.size());

        EventoPortalDTO evento1 = eventosPortal.get(0);
        EventoPortalDTO evento2 = eventosPortal.get(1);

        assertEquals(id1, evento1.getUbicacionId());
        assertEquals(id2, evento2.getUbicacionId());

        assertTrue(evento1.getIntensidad() == -5);
        assertTrue(evento2.getIntensidad() == -5);

    }

    @Test
    public void seConsiguenLosEventosDePortalesOcurridosCon2PortalesOcurridosEnLaMismaUbicacion() {
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ubicacion.setTemperatura(20);
        this.ubicacionService.crear(ubicacion);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);

        Long id1 = this.ubicacion.getId();

        ubicacionRepository.setTemperatura(ubicacion.getId(),17.8);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        espirituService.crearPortal(new PortalDemonios(new Coordenada(0.5, 0.5)));

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        espirituService.crearPortal(new PortalDemonios(new Coordenada(0.5, 0.5)));

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<EventoPortalDTO> eventosPortal = estadisticaService.eventosDePortalesOcurridos();

        assertEquals(2,eventosPortal.size());

        EventoPortalDTO evento1 = eventosPortal.get(0);
        EventoPortalDTO evento2 = eventosPortal.get(1);

        assertEquals(id1, evento1.getUbicacionId());
        assertEquals(id1, evento2.getUbicacionId());

        assertTrue(evento1.getIntensidad() == -5);
        assertTrue(evento2.getIntensidad() == -5);

    }

    @AfterEach
    void eliminarModelo() {
        testService.eliminarEspiritus();
        testService.eliminarMediums();
        testService.eliminarUbicaciones();
        testService.eliminarSnapshots();
    }

}
