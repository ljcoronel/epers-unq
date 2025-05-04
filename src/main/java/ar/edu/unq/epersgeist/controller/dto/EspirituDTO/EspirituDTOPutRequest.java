package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;


public record EspirituDTOPutRequest(String nombre, int nivelDeConexion) {

    public Espiritu aModeloId(Long id) {

//        Espiritu espiritu = this.tipo.getEspirituInstance(this.nivelDeConexion, this.nombre, ubicacionPlaceholder);
        Espiritu espiritu = new Angel(this.nivelDeConexion, this.nombre);
        // un placeholder, el tipo es irrelevante porque no se puede modificar

        espiritu.setId(id);
        return espiritu;
    }

}
