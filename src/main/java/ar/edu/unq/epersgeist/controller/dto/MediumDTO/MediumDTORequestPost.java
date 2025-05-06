package ar.edu.unq.epersgeist.controller.dto.MediumDTO;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public record MediumDTORequestPost(String nombre, int manaMax, int mana, Long ubicacionID) {

    public Medium aModelo() {
        Ubicacion ubicacionPlaceholder = new Santuario("Placeholder");
        ubicacionPlaceholder.setId(this.ubicacionID);
        return new Medium(this.nombre, this.manaMax, this.mana, ubicacionPlaceholder);
    }
}
