package ar.edu.unq.epersgeist.controller.dto.ReporteDTO;

import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.EspirituJPADTO;

public record DraftEspirituJPADTO(Long espirituId, Integer nivelDeConexion, String nombre, Long mediumId, Long idUbicacion, Long espirituDominanteId) implements SnapshotSql {

    public static DraftEspirituJPADTO guardar(EspirituJPADTO espiritu) {
        return new DraftEspirituJPADTO(
                espiritu.getId(),
                espiritu.getNivelDeConexion(),
                espiritu.getNombre(),
                verificarMedium(espiritu.getMedium()),
                espiritu.getUbicacion().getId(),
                verificarEspiritu(espiritu.getEspirituDominante())
        );
    }

    public static Long verificarMedium(MediumJPADTO medium) {
        if (medium != null) return medium.getId();
        return null;
    }

    public static Long verificarEspiritu(EspirituJPADTO espiritu) {
        if (espiritu != null) return espiritu.getId();
        return null;
    }
}
