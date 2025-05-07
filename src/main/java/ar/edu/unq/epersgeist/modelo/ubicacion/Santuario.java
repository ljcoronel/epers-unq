package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.modelo.EfectosDeUbicacion;
import ar.edu.unq.epersgeist.modelo.UbicacionesRecibidoras;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
public class Santuario extends Ubicacion  {

    public Santuario(String nombre) {
        super(nombre);
    }

    @Override
    public void recibir (EfectosDeUbicacion espiritu) {
        espiritu.recibirEfectoDe(this);
    }
}
