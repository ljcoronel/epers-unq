package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;


public record EspirituDTORequest(TipoEspiritu tipo, String nombre, int nivelDeConexion, Long ubicacionId) {

    public static EspirituDTORequest desdeModelo(Espiritu espiritu) {
        return new EspirituDTORequest(
                TipoEspiritu.getValueOfEspiritu(espiritu),
                espiritu.getNombre(),
                espiritu.getNivelDeConexion(),
                espiritu.getUbicacion().getId()
        );
    }

    public Espiritu aModelo(Long id, Ubicacion ubicacion) {
        Espiritu espiritu = this.tipo.getEspirituInstance(this.nivelDeConexion, this.nombre, ubicacion);
        espiritu.setId(id);
        return espiritu;
    }

    public Espiritu AActualizado(Ubicacion ubicacion, Espiritu espiritu) {
        espiritu.setNombre(this.nombre);
        espiritu.setNivelDeConexion(this.nivelDeConexion);
        espiritu.setUbicacion(ubicacion);
        return espiritu;
    }

}
