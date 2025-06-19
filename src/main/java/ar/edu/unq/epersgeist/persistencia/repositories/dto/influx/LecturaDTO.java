package ar.edu.unq.epersgeist.persistencia.repositories.dto.influx;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor @Getter @Setter
public class LecturaDTO {

    private Long ubicacionId;
    private String timeStamp;
    private double temperatura;

    public LecturaDTO(Long ubicacionId, double temp, String timeStamp){
        this.ubicacionId = ubicacionId;
        this.timeStamp = timeStamp;
        this.temperatura = temp;
    }

    public static List<LecturaDTO> getLecturaDTO(List<FluxRecord> records) {

        List<LecturaDTO> lecturas = new ArrayList<>();

        records.forEach(record -> {
            Long ubicacionId = (Long) record.getValueByKey("ubicacion");
            Double temp = (Double) record.getValueByKey("temp");
            String timeStamp = (String) record.getValueByKey("timeStamp");

            LecturaDTO lectura = new LecturaDTO(ubicacionId,temp,timeStamp);
            lecturas.add(lectura);
        });

        return lecturas;
    }

    public Point getInfluxPoint(){
        return Point.measurement("temperaturas")
                .addTag("ubicacion", this.ubicacionId.toString())
                .addField("temp", this.temperatura)
                .time(Instant.parse(this.timeStamp), WritePrecision.MS);
    }
}
