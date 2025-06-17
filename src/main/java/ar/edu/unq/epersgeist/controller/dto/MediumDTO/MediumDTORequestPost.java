package ar.edu.unq.epersgeist.controller.dto.MediumDTO;

import ar.edu.unq.epersgeist.modelo.geo.Coordenada;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

import java.util.List;

public record MediumDTORequestPost(String nombre, int manaMax, int mana, List<Double> coordenada) {

    public Medium aModelo() {
        validarLosCampos();
        Coordenada coordenadaActual = new Coordenada(coordenada.getFirst(),coordenada.getLast());
        return new Medium(this.nombre, this.manaMax, this.mana, coordenadaActual);
    }

    private void validarLosCampos() {
        if (nombre == null) throw new ParametroIncorrectoException("El medium debe tener nombre.");
        if (manaMax <= 0) throw new ParametroIncorrectoException("El maná máximo debe ser positivo.");
        if (mana > manaMax) throw new ParametroIncorrectoException("El maná no puede superar al maná máximo.");
        if (mana < 0) throw new ParametroIncorrectoException("El maná no puede ser negativo.");
        if (coordenada.size()!=2) throw new ParametroIncorrectoException("La coordenada debe estar compuesta por 2 valores numericos, \"X\" e \"Y\".");
    }
}
