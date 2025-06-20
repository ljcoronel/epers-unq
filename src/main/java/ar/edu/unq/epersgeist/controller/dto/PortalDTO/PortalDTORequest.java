package ar.edu.unq.epersgeist.controller.dto.PortalDTO;

import ar.edu.unq.epersgeist.modelo.PortalDemonios;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;
import ar.edu.unq.epersgeist.modelo.geo.Coordenada;

import java.util.List;

public record PortalDTORequest(List<Double> coordenada) {

    public PortalDemonios aModelo() {
        if (coordenada.size() != 2)
            throw new ParametroIncorrectoException("La coordenada debe estar compuesta por 2 valores numericos, \"X\" e \"Y\".");

        Coordenada coord = new Coordenada(coordenada.get(0), coordenada.get(1));

        return new PortalDemonios(coord);
    }
}
