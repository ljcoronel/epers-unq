package ar.edu.unq.epersgeist.controller.dto.MediumDTO;

import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public record MediumDTORequestPost(String nombre, int manaMax, int mana, Long ubicacionID) {

    public Medium aModelo() {
        validarLosCampos();
        Ubicacion ubicacionPlaceholder = new Santuario("Placeholder", 0);
        ubicacionPlaceholder.setId(this.ubicacionID);
        return new Medium(this.nombre, this.manaMax, this.mana, ubicacionPlaceholder, new Coordenada(1,1));
    }

    private void validarLosCampos() {
        if (nombre == null) throw new ParametroIncorrectoException("El medium debe tener nombre.");
        if (manaMax <= 0) throw new ParametroIncorrectoException("El maná máximo debe ser positivo.");
        if (mana > manaMax) throw new ParametroIncorrectoException("El maná no puede superar al maná máximo.");
        if (mana < 0) throw new ParametroIncorrectoException("El maná no puede ser negativo.");
    }
}
