package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Reporte.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private Demonio demonio1;
    private Demonio demonio2;
    private Demonio demonio3;
    private Demonio demonio4;
    private Demonio demonio5;

    private Medium medium;
    private Medium medium2;
    private Espiritu angel1;

    private Santuario ubicacion;
    private Santuario ubicacion2;

    @Autowired
    private TestService testService;

    @BeforeEach
    void crearInstancias() {
        this.ubicacion = new Santuario("Bernal");
        this.ubicacion2 = new Santuario("Quilmes");
        angel1 = new Angel(10,"Angel1", this.ubicacion);
        demonio1 = new Demonio(
                10,
                "Pepito", this.ubicacion
        );
        demonio2 = new Demonio(
                90,
                "Diavolo", this.ubicacion

        );
        demonio3 = new Demonio(
                50,
                "Sebastián", this.ubicacion

        );
        demonio4 = new Demonio(
                50,
                "Jorge", this.ubicacion

        );
        demonio5 = new Demonio(
                50,
                "Jose", this.ubicacion2

        );
        medium = new Medium("Brujita", 100, 50, this.ubicacion);
        medium2 = new Medium("Pepe", 100, 60, this.ubicacion);

    }

    @Test
    void testing(){
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


    @AfterEach
    void eliminarModelo() {
        testService.eliminarMediums();
        testService.eliminarEspiritus();
        testService.eliminarUbicaciones();
    }

}
