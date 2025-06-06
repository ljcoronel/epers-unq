package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.geo.Point;

import java.util.*;

@Setter @Getter @NoArgsConstructor
public abstract class Ubicacion implements UbicacionesRecibidoras {

    private Long id;
    private String nombre;
    private int energia;
    private List<Point> territorio;

    public Ubicacion(String nombre, int energia, List<Coordenada> territorio) {
        this.nombre = nombre;
        this.setEnergia(energia);
        this.territorio = new ArrayList<>(territorio);
    }

    public Ubicacion(String nombre, int energia) {//se quitaria?
        this.nombre = nombre;
        this.setEnergia(energia);
    }

    public void setTerritorio(List<Coordenada> territorio) {
        this.territorio = new ArrayList<>(territorio);
    }

    public void setEnergia(int i) {
        if (i < 0) {
            this.energia = 0;
        } else if (i > 100) {
            this.energia = 100;
        } else {
            this.energia = i;
        }
    }

    @Override
    public void recibir(EfectosDeUbicacion espiritu) {
        throw new NotImplementedException("recibir no implementado");
    }

    public abstract void invocarAEspirituParaMedium(Espiritu espiritu, Medium medium);

    public abstract void recuperarConexionParaAngeles(List<Angel> angeles);

    public abstract void recuperarConexionParaDemonios(List<Demonio> demonios);

    public abstract void recuperarManaParaMedium(Medium medium);

}
