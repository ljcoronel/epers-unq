package ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;

import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public enum TipoUbicacion {

    SANTUARIO{
        @Override
        public Ubicacion getUbicacionInstance(String nombre, int energia) {
            return new Santuario(nombre, energia);
        }
    },CEMENTERIO{
        @Override
        public Ubicacion getUbicacionInstance(String nombre, int energia) {
            return new Cementerio(nombre, energia);
        }
    };

    public abstract Ubicacion getUbicacionInstance(String nombre, int energia);

    public static TipoUbicacion obtenerTipoUbicacion(Ubicacion ubicacion) {
        return switch (ubicacion){
            case Santuario ignored -> SANTUARIO;
            case Cementerio ignored -> CEMENTERIO;
            default -> throw new IllegalStateException("Unexpected value: " + ubicacion.getClass().getSimpleName());
        };
    }
}
