package ar.edu.unq.epersgeist.modelo.espiritu;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor
@ToString

@Entity
public class Demonio extends Espiritu {
    public Demonio(String nombre, Ubicacion ubicacion) {
        super(nombre, ubicacion);
    }
    public Demonio(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {
        super(nivelDeConexion, nombre, ubicacion);
    }

//    public void defender(Angel angel) {
//        //hacer algo
//    }
}
