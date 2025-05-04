package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;


public record EspirituDTOPutRequest(String nombre, int nivelDeConexion) {

    public Espiritu aModeloId(Long id) {

        Espiritu espiritu = new Angel(this.nivelDeConexion, this.nombre);
        // un placeholder, la subclase concreta es irrelevante porque no se puede modificar tipo
        espiritu.setId(id);

        return espiritu;
    }

}
