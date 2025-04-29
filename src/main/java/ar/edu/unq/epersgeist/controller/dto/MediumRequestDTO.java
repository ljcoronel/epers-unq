package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import jakarta.validation.constraints.NotNull;

public record MediumRequestDTO(String nombre, Integer manaMax, Integer mana, UbicacionDTO ubicacion) {

    public Medium aModelo(){
        Medium medium =  new Medium(
                this.nombre,
                this.manaMax,
                this.mana,
                ubicacion != null ? ubicacion.aModelo() : null
        );

        return medium;
    }
}
