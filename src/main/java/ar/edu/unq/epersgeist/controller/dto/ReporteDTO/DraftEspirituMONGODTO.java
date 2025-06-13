package ar.edu.unq.epersgeist.controller.dto.ReporteDTO;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.EspirituMONGODTO;

import java.util.List;

public record DraftEspirituMONGODTO(Long espirituId, List<Double> coordenada) implements SnapshotMongo {

    public static DraftEspirituMONGODTO guardar(EspirituMONGODTO espiritu) {
        return new DraftEspirituMONGODTO(
                espiritu.getId(),
                espiritu.getCoordenada().getCoordinates()
        );
    }
}
