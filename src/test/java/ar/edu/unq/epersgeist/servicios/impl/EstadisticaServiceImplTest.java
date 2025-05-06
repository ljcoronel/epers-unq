package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
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

import java.util.NoSuchElementException;

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

    private Medium medium;
    private Espiritu angel1;

    private Ubicacion santuarioUno;
    private Ubicacion santuarioDos;
    private Ubicacion cementerio;

    @Autowired
    private TestService testService;

    @BeforeEach
    void crearInstancias() {
        this.santuarioUno = new Santuario("Bernal");
        this.santuarioDos = new Cementerio("Quilmes");
        this.cementerio = new Cementerio("Ezpeleta");
        this.angel1 = new Angel(10,"Angel1", this.santuarioUno);
        this.demonio1 = new Demonio(10, "Pepito", this.santuarioDos);
        this.demonio2 = new Demonio(90, "Diavolo", this.santuarioDos);
        this.demonio3 = new Demonio(50, "Sebastián", this.santuarioDos);
        this.medium = new Medium("Brujita", 100, 50, this.santuarioUno);
    }

    @Test
    void testing(){
        ubicacionService.crear(santuarioUno);
        mediumService.crear(medium);
        espirituService.crear(demonio1);
        espirituService.crear(demonio2);
        espirituService.crear(demonio3);
        espirituService.crear(angel1);

        espirituService.conectar(demonio1.getId(),medium.getId());

        //int numero = estadisticaService.santuarioCorrupto(ubicacion.getId());// cambiar por reporte

        //assertEquals(2,numero);// cambiar a reporte.getdemonioslibres
    }

    @Test
    void testing2(){
        this.ubicacionService.crear(this.santuarioUno);
        this.ubicacionService.crear(this.santuarioDos);
        this.espirituService.crear(this.angel1);
        this.espirituService.crear(this.demonio1);
        this.espirituService.crear(this.demonio2);
        this.espirituService.crear(this.demonio3);

        //assertEquals("Bernal", this.estadisticaService.test().getNombre());
        //en caso de empatar con cantidad de demonios sobre angeles entre dos ubicaciones devuelve el id mas reciente
        //si hay un solo santuario pero que no tenga espiritus no lo devolvera solo si hay al menos un angel en el mismo, ya que debe estar en la db de espiritu
    }

    @AfterEach
    void eliminarModelo() {
        testService.eliminarMediums();
        testService.eliminarEspiritus();
        testService.eliminarUbicaciones();
    }

}
