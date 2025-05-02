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
        public <T extends Espiritu> T getEspirituInstance(int nivelDeConexion, String nombre, Ubicacion ubicacion){
            return (T) new Angel(nivelDeConexion, nombre, ubicacion); //TODO check
        }

    },
    DEMONIO {
        @Override
        public <T extends Espiritu> T getEspirituInstance(int nivelDeConexion, String nombre, Ubicacion ubicacion){
            return (T) new Demonio(nivelDeConexion, nombre, ubicacion); //TODO check
        }


    };

    public static boolean esTipoValido(String tipo) {
        return Arrays.stream(TipoEspiritu.values()).anyMatch(t -> t.name().equals(tipo));
    }

    public static String getNombreTipos() {
        return " | " + Arrays.stream(values()).map(tipo -> tipo.toString() + " | ").collect(Collectors.joining());
    }

    public abstract <T extends Espiritu> T getEspirituInstance(int nivelDeConexion, String nombre, Ubicacion ubicacion);

    public static TipoEspiritu getValueOfEspiritu(Espiritu espiritu) {
        return switch(espiritu.getClass().getSimpleName()) {
            case "Angel" -> ANGEL;
            case "Demonio" -> DEMONIO;
            default -> throw new IllegalStateException("Unexpected value: " + espiritu.getClass().getSimpleName());
        };
    }



}

