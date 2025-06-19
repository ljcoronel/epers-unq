package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Lectura.LecturaDeTemperatura;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor @Getter @Setter
public class LecturaDeSensorDTO {

    private String ubicacionId;
    private String timeStamp;
    private double temperatura;

    public LecturaDeSensorDTO(String ubicacionId, double temp, String timeStamp){
        this.ubicacionId = ubicacionId;
        this.timeStamp = timeStamp;
        this.temperatura = temp;
    }

    public static LecturaDeSensorDTO desdeModelo(LecturaDeTemperatura lectura) {
        Instant time = Instant.parse(lectura.getTimeStamp());
        LocalDateTime timeDto =  LocalDateTime.ofInstant(time, ZoneId.systemDefault());

        LecturaDeSensorDTO dto = new LecturaDeSensorDTO();
        dto.ubicacionId = lectura.getUbicacionId();
        dto.timeStamp = timeDto.toString();
        dto.temperatura = lectura.getTemperatura();

        return dto;
    }

    public LecturaDeTemperatura aModelo() {
        LecturaDeTemperatura lectura = new LecturaDeTemperatura();

        lectura.setUbicacionId(this.ubicacionId);
        lectura.setTimeStamp(this.timeStamp);
        lectura.setTemperatura(this.temperatura);

        return lectura;
    }

}
