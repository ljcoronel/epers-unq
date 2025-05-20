package ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public record UbicacionDTOResponseAcotado(TipoUbicacion tipoUbicacion, Long id, String nombre) {
    public static UbicacionDTOResponseAcotado desdeModelo(Ubicacion ubicacion) {
        return new UbicacionDTOResponseAcotado(
                TipoUbicacion.obtenerTipoUbicacion(ubicacion),
                ubicacion.getId(),
                ubicacion.getNombre()
        );
    }
}
