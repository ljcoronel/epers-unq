package ar.edu.unq.epersgeist.servicios.impl;

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
public class LecturaServiceImplTest {
    @Autowired
    private LecturaServiceImpl lecturaService;

    @Autowired
    private TestService testService;

    @Test
    void seGuardaUnaLecturaYApareceEnLaBaseDeDatos() {

        Point lecturaPoint = Point.measurement("temperaturas")
                .addTag("ubicacion", "1")
                .addField("temp", 50)
                .time(Instant.now().toEpochMilli(), WritePrecision.MS);

        lecturaService.recibirLecturaDeTemperatura(lecturaPoint);

        List<FluxRecord> lecturasRecuperadas = lecturaService.recuperarLecturas();

        assertEquals("1", lecturasRecuperadas.getFirst().getValueByKey("ubicacion"));
        assertEquals(50L, lecturasRecuperadas.getFirst().getValueByKey("_value"));
    }


}