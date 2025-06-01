package ar.edu.unq.epersgeist.persistencia.repositories.dto.mongo;


import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.mapping.Document;

import java.awt.*;
import java.util.List;

@Getter @Setter

@Document("Ubicacion")
public class UbicacionMONGODTO {

    @Id
    private String id;
    private String name;
    private GeoJsonPolygon coordenadas;

    public UbicacionMONGODTO(String name, GeoJsonPolygon coordenadas) {
        this.name = name;
        this.coordenadas = coordenadas;
    }

}

