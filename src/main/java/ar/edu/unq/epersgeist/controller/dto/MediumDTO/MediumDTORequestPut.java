package ar.edu.unq.epersgeist.controller.dto.MediumDTO;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public record MediumDTORequestPut(String nombre, int manaMax, int mana) {

    public Medium aModelo(Long id) {
        Ubicacion ubicacionPlaceholder = new Ubicacion("Placeholder");

        Medium medium = new Medium(this.nombre, this.manaMax, this.mana, ubicacionPlaceholder);
        medium.setId(id);
        return medium;
    }
}

