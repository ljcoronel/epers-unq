package ar.edu.unq.epersgeist.modelo.Reporte;

import ar.edu.unq.epersgeist.modelo.Medium;
import lombok.*;

@Getter @Setter @ToString
public class ReporteSantuarioMasCorrupto implements Reporte {

    private String nombreSantuario;
    private Medium medium;
    private int cantidadDeDemonios;
    private int cantidadDeDemoniosLibres;

    public ReporteSantuarioMasCorrupto() {
        this.nombreSantuario = "";
        this.medium = null;
        this.cantidadDeDemonios = 0;
        this.cantidadDeDemoniosLibres = 0;
    }
}
