package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.geo.Point;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PortalEspiritus {
    private Demonio demonioHost;
    private int cantMinionsInvocados = 1;
    private Point coordenada;
    private List<Demonio> minionsInvocados = new ArrayList<>();

    public PortalEspiritus(Point coordenada) {
        this.coordenada = coordenada;
    }

    public Demonio abrirPortal() {
        Demonio minion = summonearMinion();
        cantMinionsInvocados++;
        demonioHost.reducirNivelDeConexionEn(10);
        return minion;
    }

    private Demonio summonearMinion() {
        int nivelDeConexTomado = 10;

        if (demonioHost.getNivelDeConexion() < 10) nivelDeConexTomado = demonioHost.getNivelDeConexion(); // TODO testear

        Demonio minion = new Demonio(nivelDeConexTomado, "minion-de-" + demonioHost.getNombre() + "-" + cantMinionsInvocados, coordenada);
        minionsInvocados.add(minion);
        return minion;
    }

    public boolean sigueAbierto() {
        return demonioHost.getNivelDeConexion() > 0;
    }
}
