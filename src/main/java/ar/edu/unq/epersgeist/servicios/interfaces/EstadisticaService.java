package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.Reporte.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;

import java.time.LocalDate;

public interface EstadisticaService {
    ReporteSantuarioMasCorrupto santuarioCorrupto();
    void crearSnapshot();
    Snapshot obtenerSnapshot(LocalDate fecha);
}
