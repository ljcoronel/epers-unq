package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.geo.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PortalDemonios {
    public static final int DURACION = 10000;
    public static final int NIVEL_DE_CONEX_A_TOMAR = 10;
    public static final double GRADOS = 5d;

    private Demonio demonioHost;
    private Coordenada coordenada;

    private int cantMinionsInvocados = 1;
    private int nivelDeConexionAbsorbido;

    public PortalDemonios(Coordenada coordenada) {
        this.coordenada = coordenada;
    }

    public synchronized void abrir() {
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

        if (nivelDeConexionAbsorbido < NIVEL_DE_CONEX_A_TOMAR) nivelDeConexTomado = nivelDeConexionAbsorbido;

        Demonio minion = new Demonio(nivelDeConexTomado, "minion-de-" + demonioHost.getNombre() + "-" + cantMinionsInvocados, coordenada);
        minion.setEspirituDominante(demonioHost);
        minion.setUbicacion(demonioHost.getUbicacion());

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
