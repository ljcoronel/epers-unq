package ar.edu.unq.epersgeist.modelo.Lectura;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor @Getter
@Setter
public class LecturaDeTemperatura implements Lectura {
    private String ubicacionId;
    private Double temperatura;
    private String timeStamp;

    public LecturaDeTemperatura(String ubicacionId, Double temperatura, String timeStamp) {
        this.temperatura = temperatura;
        this.timeStamp = timeStamp;
        this.ubicacionId = ubicacionId;
    }

    public static LecturaDeTemperatura getLectura(FluxRecord record) {
        LecturaDeTemperatura lectura = new LecturaDeTemperatura();
        lectura.setUbicacionId((String) record.getValueByKey("ubicacion"));
        lectura.setTemperatura((Double)record.getValueByKey("_value"));
        lectura.setTimeStamp(record.getTime().toString());

        return lectura;
    }

    public static Point getPoint(LecturaDeTemperatura lectura) {
        return Point.measurement("temperaturas")
                .addTag("ubicacion", lectura.getUbicacionId())
                .addField("temp", lectura.getTemperatura())
                .time(Instant.parse(lectura.getTimeStamp()), WritePrecision.MS);
    }
}
