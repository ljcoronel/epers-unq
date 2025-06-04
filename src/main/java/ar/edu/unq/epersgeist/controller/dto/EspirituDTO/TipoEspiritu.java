package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.espiritu.AngelMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.espiritu.EspirituMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.AngelJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.DemonioJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.EspirituJPADTO;
import org.springframework.data.geo.Point;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum TipoEspiritu {
    ANGEL {
        @Override
        public Espiritu getEspirituInstance(int nivelDeConexion, String nombre, Point coordenada){
            return new Angel(nivelDeConexion, nombre, coordenada);
        }

        @Override
        public EspirituJPADTO getDto(Espiritu espiritu) {
            return new AngelJPADTO(espiritu);
        }

        @Override
        public AngelMongoDTO getMongo(Espiritu espiritu) {return new AngelMongoDTO(espiritu);
        }
    },
    DEMONIO {
        @Override
        public Espiritu getEspirituInstance(int nivelDeConexion, String nombre, Point coordenada){
            return new Demonio(nivelDeConexion, nombre, coordenada);
        }

        @Override
        public EspirituJPADTO getDto(Espiritu espiritu) {
            return new DemonioJPADTO(espiritu);
        }

        @Override
        public AngelMongoDTO getMongo(Espiritu espiritu) {return new AngelMongoDTO(espiritu);}
    };

    public static String getNombreTipos() {
        return " | " + Arrays.stream(values()).map(tipo -> tipo.toString() + " | ").collect(Collectors.joining());
    }

    public abstract Espiritu getEspirituInstance(int nivelDeConexion, String nombre, Point coordenada);
    public abstract EspirituJPADTO getDto(Espiritu espiritu);

    public static TipoEspiritu getValueOfEspiritu(Espiritu espiritu) {
        return switch(espiritu) {
            case Angel ignored -> ANGEL;
            case Demonio ignored -> DEMONIO;
            default -> throw new IllegalStateException("Unexpected value: " + espiritu.getClass().getSimpleName());
        };
    }

    public abstract EspirituMongoDTO getMongo(Espiritu espiritu);

}

