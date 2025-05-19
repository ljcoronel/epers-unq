package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.CementerioNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.UbicacionNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu.AngelNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu.DemonioNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.espiritu.EspirituNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.AngelJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.DemonioJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.EspirituJPADTO;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum TipoEspiritu {
    ANGEL {
        @Override
        public Espiritu getEspirituInstance(int nivelDeConexion, String nombre, Ubicacion ubicacion){
            return new Angel(nivelDeConexion, nombre, ubicacion);
        }

        @Override
        public EspirituJPADTO getDto(Espiritu espiritu) {
            return new AngelJPADTO(espiritu);
        }

        @Override
        public EspirituNEODTO getDtoNEO(Espiritu espiritu) {
            return new AngelNEODTO(espiritu);
        }

    },
    DEMONIO {
        @Override
        public Espiritu getEspirituInstance(int nivelDeConexion, String nombre, Ubicacion ubicacion){
            return new Demonio(nivelDeConexion, nombre, ubicacion);
        }

        @Override
        public EspirituJPADTO getDto(Espiritu espiritu) {
            return new DemonioJPADTO(espiritu);
        }

        @Override
        public EspirituNEODTO getDtoNEO(Espiritu espiritu) {
            return new DemonioNEODTO(espiritu);
        }
    };

    public static String getNombreTipos() {
        return " | " + Arrays.stream(values()).map(tipo -> tipo.toString() + " | ").collect(Collectors.joining());
    }

    public abstract Espiritu getEspirituInstance(int nivelDeConexion, String nombre, Ubicacion ubicacion);
    public abstract EspirituJPADTO getDto(Espiritu espiritu);
    public abstract EspirituNEODTO getDtoNEO(Espiritu espiritu);

    public static TipoEspiritu getValueOfEspiritu(Espiritu espiritu) {
        return switch(espiritu) {
            case Angel ignored -> ANGEL;
            case Demonio ignored -> DEMONIO;
            default -> throw new IllegalStateException("Unexpected value: " + espiritu.getClass().getSimpleName());
        };
    }



}

