package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.UbicacionDTOResponse;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.springframework.data.geo.Point;


public record EspirituDTOResponse(Long id, TipoEspiritu tipo, String nombre, int nivelDeConexion
        , boolean estaLibre, Long mediumId, Point coordenada, UbicacionDTOResponse ubicacion, EspirituDTOResponseAcotado dominadoPor) {
    public static EspirituDTOResponse desdeModelo(Espiritu espiritu) {
        if (espiritu == null) return null;

        return new EspirituDTOResponse(
                espiritu.getId(),
                TipoEspiritu.getValueOfEspiritu(espiritu),
                espiritu.getNombre(),
                espiritu.getNivelDeConexion(),
                espiritu.estaLibre(),
                espiritu.getMedium() != null ? espiritu.getMedium().getId() : null,
                espiritu.getCoordenada(),
                UbicacionDTOResponse.desdeModelo(espiritu.getUbicacion()),
                EspirituDTOResponseAcotado.desdeModelo(espiritu.getEspirituDominante())
        );
    }

}


