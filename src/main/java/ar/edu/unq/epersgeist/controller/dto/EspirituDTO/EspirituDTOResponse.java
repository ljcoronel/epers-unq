package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;




public record EspirituDTOResponse(Long id, TipoEspiritu tipo, String nombre, int nivelDeConexion, boolean estaLibre, Long mediumId, UbicacionDTO ubicacionDTO) {
    public static EspirituDTOResponse desdeModelo(Espiritu espiritu) {
        return new EspirituDTOResponse(
                espiritu.getId(),
                TipoEspiritu.getValueOfEspiritu(espiritu),
                espiritu.getNombre(),
                espiritu.getNivelDeConexion(),
                espiritu.estaLibre(),
                espiritu.getMedium() != null ? espiritu.getMedium().getId() : null,
                UbicacionDTO.desdeModelo(espiritu.getUbicacion())
        );
    }

    public Espiritu aModelo() {

        return this.tipo.getEspirituInstance(this.nivelDeConexion, this.nombre, this.ubicacionDTO.aModelo());

    }
}


