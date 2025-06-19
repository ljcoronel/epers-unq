package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Lectura.Lectura;
import ar.edu.unq.epersgeist.modelo.Lectura.LecturaDeTemperatura;
import ar.edu.unq.epersgeist.servicios.interfaces.SensorService;
import com.influxdb.query.FluxRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class SensorServiceTest {
    @Autowired
    private SensorService sensorService;

    @Test
    void seGuardaUnaLecturaYApareceEnLaBaseDeDatos() {

        LecturaDeTemperatura lectura =
                new LecturaDeTemperatura("1",50.0,Instant.now().toString());

        sensorService.guardarLecturaDeTemperatura(lectura);

        List<LecturaDeTemperatura> lecturasRecuperadas = sensorService.recuperarLecturas();

        assertFalse(lecturasRecuperadas.isEmpty());
        assertEquals("1", lecturasRecuperadas.getFirst().getUbicacionId());
        assertEquals(50.0, lecturasRecuperadas.getFirst().getTemperatura());
    }
}
