package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.UbicacionDTOResponse;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

public record EspirituDTOResponseAcotado(Long id, String nombre) {
    public static EspirituDTOResponseAcotado desdeModelo(Espiritu espiritu) {
        if (espiritu == null) return null;
        return new EspirituDTOResponseAcotado(
                espiritu.getId(),
                espiritu.getNombre()
        );
    }
}
