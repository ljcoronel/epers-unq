package ar.edu.unq.epersgeist.controller.dto.ReporteDTO;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.MediumMONGODTO;

import java.util.List;

public record DraftMediumMONGODTO(String mediumId, List<Double> coordenada) implements SnapshotMongo {

    public static DraftMediumMONGODTO guardar(MediumMONGODTO medium) {
        return  new DraftMediumMONGODTO(
                medium.getId(),
                medium.getCoordenada().getCoordinates()
        );
    }
}
