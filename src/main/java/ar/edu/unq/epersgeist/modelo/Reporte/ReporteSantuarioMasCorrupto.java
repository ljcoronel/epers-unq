package ar.edu.unq.epersgeist.modelo.Reporte;

import ar.edu.unq.epersgeist.modelo.Medium;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC) @ToString
public class ReporteSantuarioMasCorrupto implements Reporte {

    private String nombreSantuario;
    private Medium medium;
    private int cantidadDeDemonios;
    private int cantidadDeDemoniosLibres;
}
