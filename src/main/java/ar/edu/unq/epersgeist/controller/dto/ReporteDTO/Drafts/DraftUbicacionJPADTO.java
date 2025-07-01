package ar.edu.unq.epersgeist.controller.dto.ReporteDTO.Drafts;

import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.Snapshot.SnapshotSql;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.UbicacionJPADTO;

public record DraftUbicacionJPADTO(Long ubicacionId, String nombre, int energia) implements SnapshotSql {

    public static DraftUbicacionJPADTO guardar(UbicacionJPADTO ubicacion) {
        return new DraftUbicacionJPADTO(
                ubicacion.getId(),
                ubicacion.getNombre(),
                ubicacion.getEnergia()
        );
    }
}
