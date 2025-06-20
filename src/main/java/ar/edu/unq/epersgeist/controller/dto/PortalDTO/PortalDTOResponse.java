package ar.edu.unq.epersgeist.controller.dto.PortalDTO;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOResponseAcotado;
import ar.edu.unq.epersgeist.modelo.PortalDemonios;
import org.springframework.data.geo.Point;

public record PortalDTOResponse(Point coordenada, EspirituDTOResponseAcotado demonioHost, int energiaTotal) {

    public static PortalDTOResponse desdeModelo(PortalDemonios portalCreado) {
        return new PortalDTOResponse(
                portalCreado.getCoordenada(),
                EspirituDTOResponseAcotado.desdeModelo(portalCreado.getDemonioHost()),
                portalCreado.getNivelDeConexionAbsorbido()
        );

    }
}
