package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.geo.Point;

@Getter @Setter
public class PortalEspiritus {
    private final Demonio demonioHost;
    private int cantMinionsInvocados;
    private Point coordenada;

    public PortalEspiritus(Demonio espirituHost, Point coordenada) {
        this.demonioHost = espirituHost;
        this.coordenada = coordenada;
    }

    public Demonio abrirPortal() {
        demonioHost.reducirNivelDeConexionEn(10);
        cantMinionsInvocados++;
        return summonearMinion();
    }

    private Demonio summonearMinion() {
        Demonio minion = new Demonio(10, "minion-de-" + demonioHost.getNombre() + "-" + cantMinionsInvocados);
        minion.setCoordenada(coordenada);
        return minion;
    }
}
