package ar.edu.unq.epersgeist.persistencia.dao.influxdb;

import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UbicacionDAOINFLUXImpl implements UbicacionDAOINFLUX {

    private final InfluxDBClient influxDBClient;

    public UbicacionDAOINFLUXImpl(InfluxDBClient influxDBClient) {
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
    public Optional<Long> recuperarIdDeUbicacionMasConMasTransitoDeEspiritus(){

        String query = """
    from(bucket: "lecturas")
      |> range(start: -30d)
      |> filter(fn: (r) => r._measurement == "temperaturas" and r._field == "temp")
      |> group(columns: ["ubicacion"])
      |> sort(columns: ["_time"])
      |> difference()
      |> filter(fn: (r) => r._value != 0.0)
      |> count()
      |> group()
      |> sort(columns: ["_value"], desc: true)
      |> limit(n:1)
""";

        List<FluxTable> tables = influxDBClient.getQueryApi().query(query);

        if (tables.isEmpty() || tables.get(0).getRecords().isEmpty()) {
            return Optional.empty();
        }

        FluxRecord record = tables.get(0).getRecords().get(0);

        String ubicacionStr = (String) record.getValueByKey("ubicacion");

        return Optional.of(Long.valueOf(ubicacionStr));
    }

    public List<Long>   recuperarIdDeUbicacionesRecuperadas(){

        String query = """
join(
  tables: {
    frio: from(bucket: "lecturas")
      |> range(start: -10m)
      |> filter(fn: (r) =>
          r._measurement == "temperaturas" and r._field == "temp" and r._value <= 5.0)
      |> map(fn: (r) => ({ r with ubicacion: string(v: r.ubicacion) }))
      |> keep(columns: ["ubicacion"])
      |> distinct(column: "ubicacion"),

    calor: from(bucket: "lecturas")
      |> range(start: -10m)
      |> filter(fn: (r) =>
          r._measurement == "temperaturas" and r._field == "temp")
      |> map(fn: (r) => ({ r with ubicacion: string(v: r.ubicacion) }))
      |> group()
      |> last()
      |> filter(fn: (r) => r._value >= 25.0)
      |> keep(columns: ["ubicacion"])
  },
  on: ["ubicacion"]
)
|> keep(columns: ["ubicacion"])
|> yield(name: "resultado")
"""; // TODO en vez de 25.0 iría la temp ambiente de la ubicación específica

        List<FluxTable> tables = influxDBClient.getQueryApi().query(query);

        return tables.stream()
                .flatMap(table -> table.getRecords().stream())
                .map(record -> Long.valueOf(record.getValueByKey("ubicacion").toString()))
                .collect(Collectors.toList());
    }

}