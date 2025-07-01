package ar.edu.unq.epersgeist.controller.dto.ReporteDTO.Drafts;

import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.Snapshot.SnapshotMongo;
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
