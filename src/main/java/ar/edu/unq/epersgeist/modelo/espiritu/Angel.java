package ar.edu.unq.epersgeist.modelo.espiritu;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString

@Entity
public class Angel extends Espiritu {
    public Angel(String nombre) {
        super(nombre);
    }

    public Angel(Integer nivelDeConexion, String nombre) {
        super(nivelDeConexion,nombre);
    }

//    public Angel( Integer nivelDeConexion, String nombre) {
//        super(nivelDeConexion, nombre);
//    }

//    public void atacar(Demonio demonio) {
//        //hacer algo
//        demonio.defender(this);
//    }
}
