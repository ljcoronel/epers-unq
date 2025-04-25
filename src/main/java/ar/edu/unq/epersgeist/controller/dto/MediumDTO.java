package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Medium;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record MediumDTO(Long id, String nombre, Integer manaMax, Integer mana,
                        List<AngelDTO> angeles, List<DemonioDTO> demonios, UbicacionDTO ubicacion) {

    public static MediumDTO desdeModelo(Medium medium) {

        return new MediumDTO(
                medium.getId(),
                medium.getNombre(),
                medium.getManaMax(),
                medium.getMana(),
                medium.getAngeles().stream().map(AngelDTO::desdeModelo).collect(Collectors.toList()),
                medium.getDemonios().stream().map(DemonioDTO::desdeModelo).collect(Collectors.toList()),
                medium.getUbicacion() != null? UbicacionDTO.desdeModelo(medium.getUbicacion()) : null
        );
    }


    public Medium aModelo() {

        Medium medium = new Medium();

        medium.setId(this.id);
        medium.setNombre(this.nombre);
        medium.setManaMax(this.manaMax);
        medium.setMana(this.mana);
        medium.setUbicacion(this.ubicacion != null ? this.ubicacion.aModelo() : null );
        medium.setAngeles(this.angeles != null ?
                this.angeles.stream().map(AngelDTO::aModelo).collect(Collectors.toList()) : new ArrayList<>());
        medium.setDemonios(this.demonios != null ?
                this.demonios.stream().map(DemonioDTO::aModelo).collect(Collectors.toList()) : new ArrayList<>());

        return medium;
    }
}
