package ar.edu.unq.epersgeist.controller.dto.ReporteDTO.Drafts;

import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.Snapshot.SnapshotSql;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.AngelJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.DemonioJPADTO;

import java.util.List;
import java.util.stream.Collectors;

public record DraftMediumJPADTO(Long mediumId, String nombre, Integer manaMax, Integer mana, Long ubicacionId, List<Long> espiritusId) implements SnapshotSql {

    public static DraftMediumJPADTO guardar(MediumJPADTO medium) {
        return new DraftMediumJPADTO(
                medium.getId(),
                medium.getNombre(),
                medium.getManaMax(),
                medium.getMana(),
                medium.getUbicacion().getId(),
                espiritus(medium.getAngeles(), medium.getDemonios())
        );
    }

    private static List<Long> espiritus(List<AngelJPADTO> angeles, List<DemonioJPADTO> demonios) {
        List<Long> angelesIds = angeles.stream().map(AngelJPADTO::getId).collect(Collectors.toList());
        List<Long> demoniosIds = demonios.stream().map(DemonioJPADTO::getId).collect(Collectors.toList());
        angelesIds.addAll(demoniosIds);
        return angelesIds;
    }
}
