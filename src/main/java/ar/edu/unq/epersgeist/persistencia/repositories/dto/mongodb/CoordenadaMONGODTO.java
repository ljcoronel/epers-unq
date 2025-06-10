package ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class CoordenadaMONGODTO {
    @Id
    private String id;
    private Double longitud;
    private Double latitud;
    private Medium medium;
    private List<Espiritu> espiritus;
}
