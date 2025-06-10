package ar.edu.unq.epersgeist.controller.dto.ReporteDTO;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.EspirituJPADTO;

public record DraftEspirituJPADTO(Long espirituId, Integer nivelDeConexion, String nombre, Long mediumId, Long idUbicacion, Long espirituDominanteId) implements SnapshotSql {

    public static DraftEspirituJPADTO guardar(EspirituJPADTO espiritu) {
        return new DraftEspirituJPADTO(
                espiritu.getId(),
                espiritu.getNivelDeConexion(),
                espiritu.getNombre(),
                espiritu.getMedium().getId(),
                espiritu.getUbicacion().getId(),
                espiritu.getEspirituDominante().getId()
        );
    }
}
