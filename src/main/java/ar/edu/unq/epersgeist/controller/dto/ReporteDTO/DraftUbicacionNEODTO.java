package ar.edu.unq.epersgeist.controller.dto.ReporteDTO;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.UbicacionNEODTO;

import java.util.List;

public record DraftUbicacionNEODTO(Long ubicacionId, int energia, List<Long> ubicacionesId) implements SnapshotNeo {

    public static DraftUbicacionNEODTO guardar(UbicacionNEODTO ubicacion, List<Long> ubicacionesId){
        return new DraftUbicacionNEODTO(
                ubicacion.getId(),
                ubicacion.getEnergia(),
                ubicacionesId
        );
    }
}
