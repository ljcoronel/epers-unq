package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;


public record EspirituDTOPostRequest(TipoEspiritu tipo, String nombre, int nivelDeConexion, Long ubicacionID) {

    public Espiritu aModelo() {
        Integer nivelDeConexionInteger = nivelDeConexion;
        if (this.nombre == null) throw new ParametroIncorrectoException("El nombre no debe ser nulo.");
        if (this.tipo == null) throw new ParametroIncorrectoException("El tipo del espíritu no debe ser nulo.");

        Ubicacion ubicacionPlaceholder = new Santuario("Placeholder", 0);
        ubicacionPlaceholder.setId(this.ubicacionID);

        return this.tipo.getEspirituInstance(this.nivelDeConexion, this.nombre, ubicacionPlaceholder);
    }

}
