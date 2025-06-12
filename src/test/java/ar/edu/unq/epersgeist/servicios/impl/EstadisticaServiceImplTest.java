package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Reporte.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;
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
import org.springframework.data.geo.Point;

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
        ));
        this.ubicacion2 = new Santuario("Quilmes",60,List.of(
                new Coordenada(3, 0),
                new Coordenada(4, 0),
                new Coordenada(4, 1),
                new Coordenada(3, 1),
                new Coordenada(3, 0)
        ));
        this.ubicacion3 = new Cementerio("Solano",60,List.of(
                new Coordenada(6, 0),
                new Coordenada(7, 0),
                new Coordenada(7, 1),
                new Coordenada(6, 1),
                new Coordenada(6, 0)
        ));

        angel1 = new Angel(10,"Angel1",new Coordenada(0.5,0.5), this.ubicacion);
        demonio1 = new Demonio(10, "Pepito",new Coordenada(0.5,0.5), this.ubicacion);
        demonio2 = new Demonio(90, "Diavolo",new Coordenada(0.5,0.5), this.ubicacion);
        demonio3 = new Demonio(50, "Sebastián",new Coordenada(0.5,0.5), this.ubicacion);
        demonio4 = new Demonio(50, "Jorge",new Coordenada(0.5,0.5), this.ubicacion);
        demonio5 = new Demonio(50, "Jose",new Coordenada(3.5,0.5), this.ubicacion2);
        demonio6 = new Demonio(40, "Dracula",new Coordenada(6.5,0.5), this.ubicacion3);

        medium = new Medium("Brujita", 100, 50, this.ubicacion, new Coordenada(0.5,0.5));
        medium2 = new Medium("Pepe", 100, 60, this.ubicacion, new Coordenada(0.5,0.5));

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
    }

    @Test
    public void seObtieneUnValorNuloSiNoHaySnapshotsCreados() {
        assertNull(this.estadisticaService.obtenerSnapshot(LocalDate.now()));
    }

    @AfterEach
    void eliminarModelo() {
        testService.eliminarEspiritus();
        testService.eliminarMediums();
        testService.eliminarUbicaciones();
        testService.eliminarSnapshots();
    }

}
