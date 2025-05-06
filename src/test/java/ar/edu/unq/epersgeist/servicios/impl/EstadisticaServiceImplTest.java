package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
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

    private Medium medium;
    private Espiritu angel1;

    private Santuario ubicacion;

    @Autowired
    private TestService testService;

    @BeforeEach
    void crearInstancias() {
        this.ubicacion = new Santuario("Bernal");
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
        medium = new Medium("Brujita", 100, 50, this.ubicacion);

    }

    @Test
    void testing(){
        ubicacionService.crear(ubicacion);
        mediumService.crear(medium);
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);
        espirituService.crear(angel1);

        espirituService.conectar(demonio1.getId(),medium.getId());

        //int numero = estadisticaService.santuarioCorrupto(ubicacion.getId());// cambiar por reporte

        //assertEquals(2,numero);// cambiar a reporte.getdemonioslibres
    }

    @AfterEach
    void eliminarModelo() {
        testService.eliminarMediums();
        testService.eliminarEspiritus();
        testService.eliminarUbicaciones();
    }

}
