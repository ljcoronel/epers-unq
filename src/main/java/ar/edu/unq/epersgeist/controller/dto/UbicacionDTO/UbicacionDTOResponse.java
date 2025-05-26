package ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public record UbicacionDTOResponse(TipoUbicacion tipoUbicacion, Long id, String nombre, int energia) {


    public static UbicacionDTOResponse desdeModelo(Ubicacion ubicacion) {
        return new UbicacionDTOResponse(
                TipoUbicacion.obtenerTipoUbicacion(ubicacion),
                ubicacion.getId(),
                ubicacion.getNombre(),
                ubicacion.getEnergia()
        );
    }


}
