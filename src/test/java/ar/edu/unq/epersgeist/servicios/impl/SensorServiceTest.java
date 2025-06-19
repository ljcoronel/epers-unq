package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Lectura.Lectura;
import ar.edu.unq.epersgeist.modelo.Lectura.LecturaDeTemperatura;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.servicios.interfaces.SensorService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import ar.edu.unq.epersgeist.testUtils.TestService;
import com.influxdb.query.FluxRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class SensorServiceTest {
    @Autowired
    private SensorService sensorService;
    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private TestService limpiador;

    private Ubicacion ubicacionUno;

    @BeforeEach
    public void setUp() {
        ubicacionUno = new Santuario("iglesia", 60);

    }

    @Test
    void seGuardaUnaLecturaYApareceEnLaBaseDeDatos() {

        ubicacionService.crear(ubicacionUno);
        Ubicacion ubicacionRecuperada =  ubicacionService.recuperar(ubicacionUno.getId()).get();
        String ubicacionId = ubicacionRecuperada.getId().toString();

        LecturaDeTemperatura lectura =
            new LecturaDeTemperatura(ubicacionId,50.0,Instant.now().toString());

        sensorService.guardarLecturaDeTemperatura(lectura);

        List<LecturaDeTemperatura> lecturasRecuperadas = sensorService.recuperarLecturas();

        assertFalse(lecturasRecuperadas.isEmpty());
        assertEquals(ubicacionId, lecturasRecuperadas.getFirst().getUbicacionId());
        assertEquals(50.0, lecturasRecuperadas.getFirst().getTemperatura());
    }

    @AfterEach
    public void tearDown() {
        this.limpiador.eliminarUbicaciones();
    }
}
