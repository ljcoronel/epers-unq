package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.LecturaRepository;
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
public class LecturaRepositoryImpl implements LecturaRepository {

    private final InfluxDBClient influxDBClient;

    public LecturaRepositoryImpl(InfluxDBClient influxDBClient) {
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
    public Long recuperarIdDeUbicacionMasPeligrosa() {
        String query =
                "from(bucket: \"lecturas\")\n" +
                        "  |> range(start: -30d)\n" +
                        "  |> filter(fn: (r) => r._measurement == \"temperaturas\" and r._field == \"temp\")\n" +
                        "  |> group(columns: [\"ubicacion\"])\n" +
                        "  |> sort(columns: [\"_time\"], desc: true)\n" +
                        "  |> unique(column: \"ubicacion\")\n" +
                        "  |> group()\n" +
                        "  |> sort(columns: [\"_value\"])\n" +
                        "  |> limit(n: 1)";

        List<FluxTable> tables = influxDBClient.getQueryApi().query(query);

        if (tables.isEmpty() || tables.get(0).getRecords().isEmpty()) {
            throw new EntidadInexistenteException("No se encontraron lecturas de temperatura.");
        }

        FluxRecord record = tables.get(0).getRecords().get(0);
        String ubicacionStr = (String) record.getValueByKey("ubicacion");

        return Long.valueOf(ubicacionStr);
    }

    @Override
    public Long recuperarIdDeUbicacionMasConMasTransitoDeEspiritus(){
/*
        String query = """
            from(bucket: "lecturas")
              |> range(start: -30d)
              |> filter(fn: (r) => r._measurement == "temperaturas" and r._field == "temp")
              |> group(columns: ["ubicacion"])
              |> sort(columns: ["_time"], desc: true)
              |> difference(nonNegative: true)
              |> map(fn: (r) => ({ r with _value: if exists r._value then 1 else 0 }))
              |> group(columns: ["ubicacion"])
              |> sum()
              |> sort(columns: ["_value"], desc: true)
              |> limit(n: 1)
            """;

        List<FluxTable> tables = influxDBClient.getQueryApi().query(query);

        if (tables.isEmpty() || tables.get(0).getRecords().isEmpty()) {
            throw new EntidadInexistenteException("No se encontraron lecturas de temperaturas.");
        }*/

        return null;
    }
}