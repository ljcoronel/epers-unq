package ar.edu.unq.epersgeist.controller.dto.MediumDTO;


import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOResponse;

import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.UbicacionDTOResponse;
import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.List;
import java.util.stream.Collectors;

public record MediumDTOResponse(Long id, String nombre, int manaMax, int mana, List<EspirituDTOResponse> angeles, List<EspirituDTOResponse> demonios, UbicacionDTOResponse ubicacion) {

    public static MediumDTOResponse desdeModelo(Medium medium) {
        if(medium == null) return null;

        return new MediumDTOResponse(
                medium.getId(),
                medium.getNombre(),
                medium.getManaMax(),
                medium.getMana(),
                medium.getAngeles().stream()
                        .map(EspirituDTOResponse::desdeModelo)
                        .collect(Collectors.toList()),
                medium.getDemonios().stream()
                        .map(EspirituDTOResponse::desdeModelo)
                        .collect(Collectors.toList()),
                UbicacionDTOResponse.desdeModelo(medium.getUbicacion()) //TODO sacar el DTO
        );
    }

}
