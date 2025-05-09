package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@Getter @Setter @NoArgsConstructor @ToString

@Entity
@SQLDelete(sql = "update ubicacion set deleted = current_timestamp where id =?")
@Where(clause = "deleted = null")
public class Cementerio extends Ubicacion {


    public Cementerio(String nombre, int energia) { super(nombre, energia); }


    @Override
    public void invocarAEspirituParaMedium(Espiritu espiritu, Medium medium) {
        espiritu.invocadoACementerioPorMedium(this, medium);
    };

    @Override
    public void recuperarConexionParaAngeles(List<Angel> angeles) {}

    @Override
    public void recuperarConexionParaDemonios(List<Demonio> demonios) {
        demonios.forEach(demonio -> demonio.aumentarNivelDeConexionEn(this.getEnergia()));
    }

    @Override
    public void recuperarManaParaMedium(Medium medium) {

        medium.aumentarMana(this.getEnergiaParaRecuperarMedium());
    }

    private int getEnergiaParaRecuperarMedium() {
        return (int) (this.getEnergia()*0.5);
    }
}
