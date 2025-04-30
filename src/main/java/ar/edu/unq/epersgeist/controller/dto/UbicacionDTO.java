package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTORequest;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

public record UbicacionDTO(Long id,String nombre) {
    public static UbicacionDTO desdeModelo(Ubicacion ubicacion) {
        return new UbicacionDTO(
                ubicacion.getId(),
                ubicacion.getNombre()
        );
    }

    public Ubicacion aModelo() {
        return null;
    }
}