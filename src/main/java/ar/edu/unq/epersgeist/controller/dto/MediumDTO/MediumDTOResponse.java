package ar.edu.unq.epersgeist.controller.dto.MediumDTO;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;

import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

public record MediumDTOResponse(Long id, String nombre, int manaMax, int mana, List<EspirituDTO> angeles, List<EspirituDTO> demonios, UbicacionDTO ubicacion) {

    public static MediumDTOResponse desdeModelo(Medium medium) {
        return new MediumDTOResponse(
                medium.getId(),
                medium.getNombre(),
                medium.getManaMax(),
                medium.getMana(),
                medium.getAngeles().stream()
                        .map(EspirituDTO::desdeModelo)
                        .collect(Collectors.toList()),
                medium.getDemonios().stream()
                        .map(EspirituDTO::desdeModelo)
                        .collect(Collectors.toList()),
                UbicacionDTO.desdeModelo(medium.getUbicacion())
        );
    }

}
