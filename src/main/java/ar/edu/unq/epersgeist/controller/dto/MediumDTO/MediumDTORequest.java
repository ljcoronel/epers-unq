package ar.edu.unq.epersgeist.controller.dto.MediumDTO;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;

import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

public record MediumDTORequest(String nombre, int manaMax, int mana, Long ubicacionID) {

    public static MediumDTORequest desdeModelo(Medium medium) {
        return new MediumDTORequest(
                medium.getNombre(),
                medium.getManaMax(),
                medium.getMana(),
                medium.getUbicacion().getId()
        );
    }

    public Medium aModelo(Ubicacion ubicacion) {
        return new Medium(this.nombre, this.manaMax, this.mana, ubicacion);
    }
}
