package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;

import java.util.List;

public interface LecturaService {
    void recibirLecturaDeTemperatura(Point lectura); //timestamp en iso 8601
    List<FluxRecord> recuperarLecturas();
    Ubicacion ubicacionMasPeligrosa();
}