package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.modelo.EfectosDeUbicacion;
import ar.edu.unq.epersgeist.modelo.UbicacionesRecibidoras;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter @Setter @NoArgsConstructor @ToString

@Entity
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
    public void recibir(EfectosDeUbicacion espiritu) {
        espiritu.recibirEfectoDe(this);
    }

    private int getEnergiaParaRecuperarMedium() {
        return (int) (this.getEnergia()*0.5);
    public Cementerio(String nombre) {
        super(nombre);
    }
}
