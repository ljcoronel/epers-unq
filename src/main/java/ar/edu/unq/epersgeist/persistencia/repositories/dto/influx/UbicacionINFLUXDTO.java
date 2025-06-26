package ar.edu.unq.epersgeist.persistencia.repositories.dto.influx;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UbicacionINFLUXDTO { // TODO implementar en vez de setear forzosamente

    private Long id;
    private double temperatura;

    public UbicacionINFLUXDTO(Long id, double temperatura) {
        this.id = id;
        this.temperatura = temperatura;
    }

   public Ubicacion aModelo(Ubicacion ubicacion) {
        ubicacion.setTemperatura(temperatura);
        return ubicacion;
   }

}