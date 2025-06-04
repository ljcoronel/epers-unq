package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.AngelJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.DemonioJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.EspirituJPADTO;
import org.springframework.data.geo.Point;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum TipoEspiritu {
    ANGEL {
        @Override
        public Espiritu getEspirituInstance(int nivelDeConexion, String nombre, Point coordenada, Ubicacion ubicacion){
            return new Angel(nivelDeConexion, nombre, coordenada,ubicacion);
        }

        @Override
        public EspirituJPADTO getDto(Espiritu espiritu) {
            return new AngelJPADTO(espiritu);
        }

    },
    DEMONIO {
        @Override
        public Espiritu getEspirituInstance(int nivelDeConexion, String nombre, Point coordenada,Ubicacion ubicacion){
            return new Demonio(nivelDeConexion, nombre, coordenada,ubicacion);
        }

        @Override
        public EspirituJPADTO getDto(Espiritu espiritu) {
            return new DemonioJPADTO(espiritu);
        }

    };

    public static String getNombreTipos() {
        return " | " + Arrays.stream(values()).map(tipo -> tipo.toString() + " | ").collect(Collectors.joining());
    }

    public abstract Espiritu getEspirituInstance(int nivelDeConexion, String nombre, Point coordenada,Ubicacion ubicacion);
    public abstract EspirituJPADTO getDto(Espiritu espiritu);

    public static TipoEspiritu getValueOfEspiritu(Espiritu espiritu) {
        return switch(espiritu) {
            case Angel ignored -> ANGEL;
            case Demonio ignored -> DEMONIO;
            default -> throw new IllegalStateException("Unexpected value: " + espiritu.getClass().getSimpleName());
        };
    }

}

