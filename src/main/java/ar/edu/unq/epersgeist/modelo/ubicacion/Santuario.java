package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.modelo.geo.Coordenada;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @NoArgsConstructor @ToString
public class Santuario extends Ubicacion {

    public Santuario(String nombre, int energia,List<Coordenada> territorio) {
        super(nombre, energia,territorio);
    }

    public Santuario(String nombre, int energia) {//se quitaria?
        super(nombre, energia);
    }

    @Override
    public void invocarAEspirituParaMedium(Espiritu espiritu, Medium medium) {
        espiritu.invocarASantuarioAMedium(this, medium);
    }

    @Override
    public void recuperarConexionParaAngeles(List<Angel> angeles) {
        for(Angel angel : angeles){
            angel.aumentarNivelDeConexionEn(this.getEnergia());
        }

    }

    @Override
    public void recibir (EfectosDeUbicacion espiritu) {
        espiritu.recibirEfectoDe(this);
    }

    @Override
    public void recuperarConexionParaDemonios(List<Demonio> demonios) {}

    @Override
    public void recuperarManaParaMedium(Medium medium) {
        medium.aumentarMana((int) (this.getEnergia() * getEnergiaParaRecuperarMedium()));
    }

    private double getEnergiaParaRecuperarMedium() {
        return 1.5;
    }

}
