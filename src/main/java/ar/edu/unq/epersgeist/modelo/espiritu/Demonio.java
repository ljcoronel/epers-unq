package ar.edu.unq.epersgeist.modelo.espiritu;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor
@ToString

@Entity
public class Demonio extends Espiritu {
    public Demonio(String nombre) {
        super(nombre);
    }
    public Demonio(Integer nivelDeConexion, String nombre ) {
        super(nivelDeConexion, nombre);
    }

//    public void defender(Angel angel) {
//        //hacer algo
//    }
}
