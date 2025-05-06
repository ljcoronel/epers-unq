package ar.edu.unq.epersgeist.modelo.espiritu;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString

@Entity
public class Angel extends Espiritu {
    public Angel(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {
        super(nivelDeConexion,nombre, ubicacion);
    }

    public Angel(int nivelDeConexion, String nombre) {
        super(nivelDeConexion, nombre);
    }

    public void atacar(Demonio demonio) {

        int porcentajeDeAtaque = this.porcentajeAtaqueRandom();

        demonio.defender(this,porcentajeDeAtaque);
    }

    @Override
    public void agregarAEspiritusDeMedium(Medium medium) {
        medium.agregarAAngeles(this);
    }

    @Override
    public void desconectarDeMedium(Medium medium) {
        medium.quitarDeAngeles(this);
    }

}
