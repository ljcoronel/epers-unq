package ar.edu.unq.epersgeist.persistencia.DAO.influx;

import ar.edu.unq.epersgeist.persistencia.DTOs.influx.LecturaDeSensorDTO;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.Bucket;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LecturaDAOInfluxImpl implements LecturaDAOInflux{

    private final InfluxDBClient influxDBClient;

    public LecturaDAOInfluxImpl(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    @Override
    public void guardarLectura(Point influxPoint) {
        influxDBClient.getWriteApiBlocking().writePoint(influxPoint);
    }

    @Override
    public List<FluxRecord> recuperarLecturas() {
        String query = "from(bucket: \"lecturas\")\n" +
                "  |> range(start: -10m)\n" +
                "  |> filter(fn: (r) => r._measurement == \"temperaturas\" and r._field == \"temp\")\n" +
                "  |> keep(columns: [\"_time\", \"_value\", \"ubicacion\"])" ;

        List<FluxTable> tables = influxDBClient.getQueryApi().query(query);

        List<FluxRecord> lecturas = new ArrayList<>();

        tables.forEach(table -> { lecturas.addAll(table.getRecords());});

        return lecturas;
    }

    @Override
    public void deleteAll() {
        String bucket =  "lecturas";
        String org = "Dev Team";
        OffsetDateTime start = OffsetDateTime.parse("2025-05-18T00:00:00Z");
        OffsetDateTime stop = OffsetDateTime.now();

        String predicate = "_measurement=\"temperaturas\"";

        influxDBClient.getDeleteApi().delete(start,stop,predicate,bucket,org);
    }
}
