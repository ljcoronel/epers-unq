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
    public void guardarDatos(Point influxPoint) {
        influxDBClient.getWriteApiBlocking().writePoint(influxPoint);
    }

    @Override
    public Long recuperarIdDeUbicacionMasPeligrosa() {
        // PARA TESTEAR usar -7s como range ( |> range(start: -7s) ) justo abajo del from y arriba del filter
        String query =
                """
                        from(bucket: "lecturas")
                          |> range(start: -10s)
                          |> filter(fn: (r) => r._measurement == "temperaturas" and r._field == "temp")
                          |> group(columns: ["ubicacion"])
                          |> sort(columns: ["_time"], desc: true)
                          |> unique(column: "ubicacion")
                          |> group()
                          |> sort(columns: ["_value"])
                          |> limit(n: 1)""";

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
        // PARA TESTEAR usar -7s como range ( |> range(start: -7s) ) justo abajo del from y arriba del filter

        String query = """
    from(bucket: "lecturas")
      |> range(start: -7s)
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

    @Override
    public double getTemperatura(Long id) {
        String query = """
                        from(bucket: "lecturas")
                          |> range(start: -10s)
                          |> filter(fn: (r) => r._measurement == "temperaturas" and r._field == "temp" and r.ubicacion == "%s")
                          |> keep(columns: ["_value"])
                        """.formatted(id);

        List<FluxTable> tables = influxDBClient.getQueryApi().query(query);

        double temperatura = (double) tables.getFirst()
                .getRecords()
                .getFirst()
                .getRow()
                .getLast();

        return temperatura;
    }

    @Override
    public boolean tieneDeltaDeTemperaturaMenorOIgual(Long id, int deltaTemp) {

//        String query = """
//                        from(bucket: "lecturas")
//                        |> range(start: -10s)
//                        |> filter(fn: (r) => r._measurement == "temperaturas" and r._field == "temp" and r.ubicacion == "%s")
//                        |> keep(columns: ["_value"])
//                        |> map(fn: (r) => ({ _value: r._value, esDelTempMenorOIgual: if r._value - %d <= %d then true else false }))
//                        |> keep(columns: ["esDelTempMenorOIgual"])
//                """.formatted(id, tempAmbiente, deltaTemp);

        String query = """
                        from(bucket: "lecturas")
                        |> range(start: -10s)
                        |> filter(fn: (r) => r._measurement == "temperaturas" and (r._field == "temp" or r._field == "tempAmbiente") and r.ubicacion == "%s")
                        |> pivot(rowKey:["_time"], columnKey: ["_field"], valueColumn: "_value")
                        |> map(fn: (r) => ({temp: r.temp, esDelTempMenorOIgual: if r.temp - r.tempAmbiente <= %d then true else false }))
                """.formatted(id, deltaTemp); // TODO no puedo sacar ese temp del map o revienta

        List<FluxTable> tables = influxDBClient.getQueryApi().query(query);

        return (boolean) tables.getFirst()
                .getRecords()
                .getFirst()
                .getValueByKey("esDelTempMenorOIgual");
    }


}