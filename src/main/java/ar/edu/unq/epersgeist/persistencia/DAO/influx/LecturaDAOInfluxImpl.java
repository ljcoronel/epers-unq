package ar.edu.unq.epersgeist.persistencia.DAO.influx;

import ar.edu.unq.epersgeist.modelo.Lectura.Lectura;
import ar.edu.unq.epersgeist.modelo.Lectura.LecturaDeTemperatura;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LecturaDAOInfluxImpl implements LecturaDAOInflux{

    private final InfluxDBClient influxDBClient;

    public LecturaDAOInfluxImpl(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }


    @Override
    public List<LecturaDeTemperatura> recuperarLecturas() {
        String query = "from(bucket: \"lecturas\")\n" +
                "  |> range(start: -10m)\n" +
                "  |> filter(fn: (r) => r._measurement == \"temperaturas\" and r._field == \"temp\")\n" +
                "  |> keep(columns: [\"_time\", \"_value\", \"ubicacion\"])" ;

        List<FluxTable> tables = influxDBClient.getQueryApi().query(query);

        List<LecturaDeTemperatura> lecturas = new ArrayList<>();

        tables.forEach(table -> { lecturas.addAll( getLecturas(table.getRecords()));});

        return lecturas;
    }

    private List<LecturaDeTemperatura> getLecturas(List<FluxRecord> records) {
        return records.stream()
                .map(LecturaDeTemperatura::getLectura)
                .toList();
    }

    @Override
    public void guardarLectura(LecturaDeTemperatura lectura) {
//        Point lecturaPoint = Point.measurement("temperaturas")
//                .addTag("ubicacion", lectura.getUbicacionId())
//                .addField("temp", lectura.getTemperatura())
//                .time(Instant.parse(lectura.getTimeStamp()), WritePrecision.MS);

        influxDBClient.getWriteApiBlocking().writePoint(LecturaDeTemperatura.getPoint(lectura));
    }

}
