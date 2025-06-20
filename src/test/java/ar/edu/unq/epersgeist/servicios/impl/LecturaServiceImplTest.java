package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import ar.edu.unq.epersgeist.testUtils.TestService;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    private Ubicacion ubicacion1;
    private Ubicacion ubicacion2;

    private List<Coordenada> pointsUbicacionUno;
    private List<Coordenada> pointsUbicacionDos;

    @Autowired
    private LecturaServiceImpl lecturaService;

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private TestService testService;

    @BeforeEach
    void crearDatos(){
        pointsUbicacionUno= List.of(
                new Coordenada(0, 0),
                new Coordenada(1, 0),
                new Coordenada(1, 1),
                new Coordenada(0, 1),
                new Coordenada(0, 0)
        );
        pointsUbicacionDos= List.of(
                new Coordenada(3, 3),
                new Coordenada(4, 3),
                new Coordenada(4, 4),
                new Coordenada(3, 4),
                new Coordenada(3, 3));

        this.ubicacion1 = new Santuario("Bernal", 50);
        this.ubicacion2 = new Santuario("Quilmes", 50);

        ubicacion1.setTerritorio(pointsUbicacionUno);
        ubicacion2.setTerritorio(pointsUbicacionDos);
    }

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

    @Test
    void seRecuperaLaUbicacionMasPeligrosaEnBaseAVariasLecturas(){
        this.ubicacionService.crear(ubicacion1);
        this.ubicacionService.crear(ubicacion2);

        Long id = this.ubicacion1.getId();
        String idString = id.toString();

        Long id2 = this.ubicacion2.getId();
        String idString2 = id2.toString();

        Point lecturaPoint0 = Point.measurement("temperaturas")
                .addTag("ubicacion", "10")
                .addField("temp", 50)
                .time(Instant.now().toEpochMilli(), WritePrecision.MS);
        Point lecturaPoint1 = Point.measurement("temperaturas")
                .addTag("ubicacion", idString)
                .addField("temp", 40)
                .time(Instant.now().toEpochMilli(), WritePrecision.MS);
        Point lecturaPoint2 = Point.measurement("temperaturas")
                .addTag("ubicacion", idString2)
                .addField("temp", 0)
                .time(Instant.now().toEpochMilli(), WritePrecision.MS);
        Point lecturaPoint3 = Point.measurement("temperaturas")
                .addTag("ubicacion", idString)
                .addField("temp", 10)
                .time(Instant.now().toEpochMilli(), WritePrecision.MS);

        lecturaService.recibirLecturaDeTemperatura(lecturaPoint0);
        lecturaService.recibirLecturaDeTemperatura(lecturaPoint1);
        lecturaService.recibirLecturaDeTemperatura(lecturaPoint2);
        lecturaService.recibirLecturaDeTemperatura(lecturaPoint3);

        Ubicacion ubicacionRecuperada = lecturaService.ubicacionMasPeligrosa();

        assertEquals(id2,ubicacionRecuperada.getId());
    }


    @AfterEach
    public void tearDown() {
        this.testService.eliminarEspiritus();
        this.testService.eliminarMediums();
        this.testService.eliminarUbicaciones();
    }
}