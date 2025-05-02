package ar.edu.unq.epersgeist.controller.dto.MediumDTO;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

public record MediumDTORequestPut(String nombre, int manaMax, int mana) {

    public static MediumDTORequestPut desdeModelo(Medium medium) {
        return new MediumDTORequestPut(
                medium.getNombre(),
                medium.getManaMax(),
                medium.getMana()
        );
    }

    public Medium aModelo(Long id) {
        Ubicacion ubicacionPlaceholder = new Ubicacion("Placeholder");

        Medium medium = new Medium(this.nombre, this.manaMax, this.mana, ubicacionPlaceholder);
        medium.setId(id);
        return medium;
    }
}

