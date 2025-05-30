package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;


public record EspirituDTOPutRequest(String nombre, int nivelDeConexion) {

    public Espiritu aModeloId(Long id) {
        Integer nivelDeConexionInteger = nivelDeConexion;
        if (this.nombre == null) throw new ParametroIncorrectoException("El nombre no debe ser nulo.");

        Espiritu espiritu = new Angel(this.nivelDeConexion, this.nombre);
        // un placeholder, la subclase concreta es irrelevante porque no se puede modificar tipo
        espiritu.setId(id);

        return espiritu;
    }

}
