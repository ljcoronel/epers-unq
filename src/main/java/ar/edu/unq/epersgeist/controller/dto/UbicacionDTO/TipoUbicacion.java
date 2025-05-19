package ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;

import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.CementerioJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.SantuarioJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.UbicacionJPADTO;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum TipoUbicacion {

    SANTUARIO{
        @Override
        public Ubicacion getUbicacionInstance(String nombre, int energia) {
            return new Santuario(nombre, energia);
        }

        @Override
        public UbicacionJPADTO getDto(Ubicacion ubicacion) {
            return new SantuarioJPADTO(ubicacion);
        }

    },CEMENTERIO{
        @Override
        public Ubicacion getUbicacionInstance(String nombre, int energia) {
            return new Cementerio(nombre, energia);
        }

        @Override
        public UbicacionJPADTO getDto(Ubicacion ubicacion) {
            return new CementerioJPADTO(ubicacion);
        }
    };

    public static String getNombreTipos() {
        return " | " + Arrays.stream(values()).map(tipo -> tipo.toString() + " | ").collect(Collectors.joining());
    }

    public abstract Ubicacion getUbicacionInstance(String nombre, int energia);

    public static TipoUbicacion obtenerTipoUbicacion(Ubicacion ubicacion) {
        return switch (ubicacion){
            case Santuario ignored -> SANTUARIO;
            case Cementerio ignored -> CEMENTERIO;
            default -> throw new IllegalStateException("Unexpected value: " + ubicacion.getClass().getSimpleName());
        };
    }

    public abstract UbicacionJPADTO getDto(Ubicacion ubicacion);
}
