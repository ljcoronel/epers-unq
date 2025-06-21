package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
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
import java.util.stream.Collectors;

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
                "  |> range(start: -10s)\n" +
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
                        "  |> range(start: -10s)\n" +
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

        String query = """
            from(bucket: "lecturas")
              |> range(start: -10s)
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
        }

        FluxRecord record = tables.get(0).getRecords().get(0);

        String ubicacionStr = (String) record.getValueByKey("ubicacion");

        return Long.valueOf(ubicacionStr);
    }

    public List<Long> recuperarIdDeUbicacionesRecuperadas(){

        String query = """
        bajos = from(bucket: "lecturas")
          |> range(start: -30m)
          |> filter(fn: (r) =>
            r._measurement == "temperaturas" and
            r._field == "temp" and
            r._value <= 5.0
          )
          |> map(fn: (r) => ({ r with ubicacion: string(v: r.ubicacion) }))
          |> keep(columns: ["ubicacion"])
          |> distinct(column: "ubicacion")
          |> group(columns: ["ubicacion"])

        ultimas = from(bucket: "lecturas")
          |> range(start: -30m)
          |> filter(fn: (r) =>
            r._measurement == "temperaturas" and
            r._field == "temp"
          )
          |> group(columns: ["ubicacion"])
          |> last()
          |> filter(fn: (r) => r._value >= 25.0)
          |> map(fn: (r) => ({ r with ubicacion: string(v: r.ubicacion) }))
          |> group(columns: ["ubicacion"])

        join(
          tables: {bajos: bajos, altas: ultimas},
          on: ["ubicacion"]
        )
        |> keep(columns: ["ubicacion"])
    """;

        List<FluxTable> tables = influxDBClient.getQueryApi().query(query);

        return tables.stream()
                .flatMap(table -> table.getRecords().stream())
                .map(record -> Long.valueOf(record.getValueByKey("ubicacion").toString()))
                .collect(Collectors.toList());
    }
}