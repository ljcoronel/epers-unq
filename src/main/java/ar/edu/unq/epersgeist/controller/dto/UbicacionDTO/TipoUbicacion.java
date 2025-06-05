package ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;

import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.CementerioNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.SantuarioNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.UbicacionNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.CementerioJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.SantuarioJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.UbicacionJPADTO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TipoUbicacion {

    SANTUARIO{
        @Override
        public Ubicacion getUbicacionInstance(String nombre, int energia, List<List<Double>> area) {
            List<Coordenada> areaGeo = area.stream()
                    .map(doubles -> new Coordenada(doubles.get(0), doubles.get(1)))
                    .toList();
            return new Santuario(nombre, energia, areaGeo);
        }

        @Override
        public UbicacionJPADTO getDto(Ubicacion ubicacion) {
            return new SantuarioJPADTO(ubicacion);

        }

        @Override
        public SantuarioNEODTO getDtoNEO(Ubicacion ubicacion) {
            return new SantuarioNEODTO(ubicacion);
        }

        @Override
        public UbicacionNEODTO getDtoNEO(Ubicacion ubicacion, UbicacionNEODTO dtoUbicacionYaCreada) {
            return new SantuarioNEODTO(ubicacion, dtoUbicacionYaCreada);
        }

    },CEMENTERIO{
        @Override
        public Ubicacion getUbicacionInstance(String nombre, int energia, List<List<Double>> area) {
            List<Coordenada> areaGeo = area.stream()
                        .map(doubles -> new Coordenada(doubles.get(0), doubles.get(1)))
                        .toList();
            return new Cementerio(nombre, energia, areaGeo);
        }

        @Override
        public UbicacionJPADTO getDto(Ubicacion ubicacion) {
            return new CementerioJPADTO(ubicacion);
        }

        @Override
        public CementerioNEODTO getDtoNEO(Ubicacion ubicacion) {
            return new CementerioNEODTO(ubicacion);
        }

        @Override
        public UbicacionNEODTO getDtoNEO(Ubicacion ubicacion, UbicacionNEODTO dtoUbicacionYaCreada) {
            return new CementerioNEODTO(ubicacion, dtoUbicacionYaCreada);
        }
    };

    public static String getNombreTipos() {
        return " | " + Arrays.stream(values()).map(tipo -> tipo.toString() + " | ").collect(Collectors.joining());
    }

    public abstract Ubicacion getUbicacionInstance(String nombre, int energia, List<List<Double>> area);

    public static TipoUbicacion obtenerTipoUbicacion(Ubicacion ubicacion) {
        return switch (ubicacion){
            case Santuario ignored -> SANTUARIO;
            case Cementerio ignored -> CEMENTERIO;
            default -> throw new IllegalStateException("Unexpected value: " + ubicacion.getClass().getSimpleName());
        };
    }

    public abstract UbicacionJPADTO getDto(Ubicacion ubicacion);
    public abstract UbicacionNEODTO getDtoNEO(Ubicacion ubicacion);
    public abstract UbicacionNEODTO getDtoNEO(Ubicacion ubicacion, UbicacionNEODTO dtoUbicacionYaCreada);
}
