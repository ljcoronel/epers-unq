package ar.edu.unq.epersgeist.modelo.espiritu;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString

@Entity
public class Angel extends Espiritu {
    public Angel(String nombre, Ubicacion ubicacion) {
        super(nombre, ubicacion);
    }

    public Angel(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {
        super(nivelDeConexion,nombre, ubicacion);
    }

//    public Angel( Integer nivelDeConexion, String nombre) {
//        super(nivelDeConexion, nombre);
//    }

//    public void atacar(Demonio demonio) {
//        //hacer algo
//        demonio.defender(this);
//    }
}
