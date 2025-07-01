package ar.edu.unq.epersgeist.controller.dto.ReporteDTO.Drafts;

import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.Snapshot.SnapshotMongo;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.UbicacionMONGODTO;

import java.util.List;

public record DraftUbicacionMONGODTO(Long ubicacionId, List<List<Double>> area) implements SnapshotMongo {

    public static DraftUbicacionMONGODTO guardar(UbicacionMONGODTO ubicacion) {
        return new DraftUbicacionMONGODTO(
                ubicacion.getId(),
                ubicacion.getPoints().stream()
                        .map(punto -> List.of(punto.getX(), punto.getY()))
                        .toList()
        );
    }
}
