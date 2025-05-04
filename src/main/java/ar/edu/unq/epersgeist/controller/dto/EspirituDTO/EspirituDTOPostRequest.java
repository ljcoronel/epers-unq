package ar.edu.unq.epersgeist.controller.dto.EspirituDTO;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;


public record EspirituDTOPostRequest(TipoEspiritu tipo, String nombre, int nivelDeConexion, Long ubicacionId) {

    public Espiritu aModelo() {
        Ubicacion ubicacionPlaceholder = new Ubicacion("Placeholder");
        ubicacionPlaceholder.setId(this.ubicacionId);

        Espiritu espiritu = this.tipo.getEspirituInstance(this.nivelDeConexion, this.nombre, ubicacionPlaceholder);
        return espiritu;
    }

}
