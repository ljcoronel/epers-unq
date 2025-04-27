package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

public class EspirituDTO {
    public static EspirituDTO desdeModelo(String nombre,int nivelDeConexion,Long idMedium,UbicacionDTO ubicacionDTO) {
        return null;
    }

    public static EspirituDTO desdeModelo(Espiritu espiritu) {return null;}

    public Angel aModeloAngel() {
        return null;
    }
//cambiar metodos aModelo
    public Demonio aModeloDemonio() {
        return null;
    }

}