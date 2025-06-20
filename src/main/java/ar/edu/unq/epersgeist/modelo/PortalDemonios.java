package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.geo.Point;

@Getter @Setter
public class PortalDemonios {
    public static final int DURACION = 10000;
    public static final int NIVEL_DE_CONEX_A_TOMAR = 10;

    private Demonio demonioHost;
    private Point coordenada;

    private int cantMinionsInvocados = 1;
    private int nivelDeConexionAbsorbido;

    public PortalDemonios(Point coordenada) {
        this.coordenada = coordenada;
    }

    public synchronized void abrirPortal() {
        nivelDeConexionAbsorbido = demonioHost.getNivelDeConexion();
        demonioHost.setNivelDeConexion(0);
    }

    public synchronized Demonio obtenerSiguienteMinion() throws InterruptedException {
        Demonio minion = summonearMinion();
        cantMinionsInvocados++;

        return minion;
    }

    private synchronized Demonio summonearMinion() throws InterruptedException {
        int nivelDeConexTomado = NIVEL_DE_CONEX_A_TOMAR; // default es 10

        if (nivelDeConexionAbsorbido < NIVEL_DE_CONEX_A_TOMAR) nivelDeConexTomado = nivelDeConexionAbsorbido; // TODO testear

        Demonio minion = new Demonio(nivelDeConexTomado, "minion-de-" + demonioHost.getNombre() + "-" + cantMinionsInvocados, coordenada);
        minion.setEspirituDominante(demonioHost);

        nivelDeConexionAbsorbido -= nivelDeConexTomado;
        Thread.sleep(DURACION);
        return minion;
    }

    public synchronized boolean sigueAbierto() {
        return nivelDeConexionAbsorbido > 0;
    }


    public void nombrarMinion(Demonio minion) {
        minion.setNombre(minion.getNombre() + "-" + minion.getId());
    }
}
