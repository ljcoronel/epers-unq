package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;

import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

public record MediumDTO(Long id, String nombre, int manaMax, int mana, List<EspirituDTO> angeles, List<EspirituDTO> demonios, UbicacionDTO ubicacion) {

    public static MediumDTO desdeModelo(Medium medium) {
        return new MediumDTO(
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
                //UbicacionDTO.desdeModelo(medium.getUbicacion().getId(),medium.getUbicacion().getNombre())
                UbicacionDTO.desdeModelo(1L,"Bosque de los Lamentos")//temporal para pruebas,cambiar por lo de arriba
        );
    }

    public Medium aModelo() {//mediumDTORequest crear dto aparte
        Medium medium = new Medium(this.nombre, this.manaMax, this.mana,this.ubicacion.aModelo());
        medium.setId(this.id);

        medium.setAngeles(this.angeles != null ?
                this.angeles.stream()
                        .map(dto -> (Angel) dto.aModelo())//espirituDTO deberia de devolver una clase angel,reemplazar
                        .collect(Collectors.toList()) :
                new ArrayList<>()
        );
        medium.setDemonios(this.demonios != null ?
                this.demonios.stream()
                        .map(dto -> (Demonio) dto.aModelo())//espirituDTO deberia de devolver una clase angel,reemplazar
                        .collect(Collectors.toList()) :
                new ArrayList<>()
        );
        return medium;
    }
}
