package ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

import java.util.List;

public record UbicacionDTOResponse(TipoUbicacion tipoUbicacion, Long id, String nombre, int energia, List<List<Double>> area, double temperatura) {


    public static UbicacionDTOResponse desdeModelo(Ubicacion ubicacion) {
        return new UbicacionDTOResponse(
                TipoUbicacion.obtenerTipoUbicacion(ubicacion),
                ubicacion.getId(),
                ubicacion.getNombre(),
                ubicacion.getEnergia(),
                ubicacion.getCoordenadas().stream().map(point -> List.of(point.getX(),point.getY())).toList(),
                ubicacion.getTemperatura()
        );
    }


}
