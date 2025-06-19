package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.servicios.interfaces.LecturaDeSensorService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import ar.edu.unq.epersgeist.testUtils.TestService;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class LecturaDeSensorServiceTest {
    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private TestService testService;

    @Test
    void seGuardaUnaLecturaYApareceEnLaBaseDeDatos() {

        ubicacionService.guardarLecturaDeTemperatura("1",50.0,Instant.now().toString());

        List<FluxRecord> lecturasRecuperadas = ubicacionService.recuperarLecturas();

        assertFalse(lecturasRecuperadas.isEmpty());
        assertEquals("1", lecturasRecuperadas.getFirst().getValueByKey("ubicacion"));
        assertEquals(50.0, lecturasRecuperadas.getFirst().getValueByKey("_value"));
    }


}
