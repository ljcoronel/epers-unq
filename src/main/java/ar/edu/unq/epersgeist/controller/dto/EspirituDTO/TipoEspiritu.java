package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum TipoEspiritu {
    ANGEL {
        @Override
        public Espiritu getEspirituInstance(int nivelDeConexion, String nombre, Ubicacion ubicacion){
            return new Angel(nivelDeConexion, nombre, ubicacion);
        }

    },
    DEMONIO {
        @Override
        public Espiritu getEspirituInstance(int nivelDeConexion, String nombre, Ubicacion ubicacion){
            return new Demonio(nivelDeConexion, nombre, ubicacion);
        }


    };

    public static String getNombreTipos() {
        return " | " + Arrays.stream(values()).map(tipo -> tipo.toString() + " | ").collect(Collectors.joining());
    }

    public abstract Espiritu getEspirituInstance(int nivelDeConexion, String nombre, Ubicacion ubicacion);

    public static TipoEspiritu getValueOfEspiritu(Espiritu espiritu) {
        return switch(espiritu.getClass().getSimpleName()) {
            case "Angel" -> ANGEL;
            case "Demonio" -> DEMONIO;
            default -> throw new IllegalStateException("Unexpected value: " + espiritu.getClass().getSimpleName());
        };
    }



}

