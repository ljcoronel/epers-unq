package ar.edu.unq.epersgeist.controller.dto.MediumDTO;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

public record MediumDTORequestPost(String nombre, int manaMax, int mana, Long ubicacionID) {

    public Medium aModelo() {
        Ubicacion ubicacionPlaceholder = new Ubicacion("Placeholder");
        ubicacionPlaceholder.setId(this.ubicacionID);
        return new Medium(this.nombre, this.manaMax, this.mana, ubicacionPlaceholder);
    }
}
