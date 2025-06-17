package ar.edu.unq.epersgeist.controller.dto.PortalDTO;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOResponseAcotado;
import ar.edu.unq.epersgeist.modelo.PortalEspiritus;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import org.springframework.data.geo.Point;

import java.util.List;

public record PortalDTOResponse(Point coordenada, EspirituDTOResponseAcotado demonioHost) {

    public static PortalDTOResponse desdeModelo(PortalEspiritus portalCreado) {
        return new PortalDTOResponse(
                portalCreado.getCoordenada(),
                EspirituDTOResponseAcotado.desdeModelo(portalCreado.getDemonioHost())
        );

    }
}
