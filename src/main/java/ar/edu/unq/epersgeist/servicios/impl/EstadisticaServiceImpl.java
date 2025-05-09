package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Reporte.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private final UbicacionDAO ubicacionDAO;
    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;

    public EstadisticaServiceImpl(UbicacionDAO ubicacionDAO, EspirituDAO espirituDAO, MediumDAO mediumDAO) {
        this.ubicacionDAO = ubicacionDAO;
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
    }

    @Override
    public ReporteSantuarioMasCorrupto santuarioCorrupto() {
        ReporteSantuarioMasCorrupto reporte = new ReporteSantuarioMasCorrupto();
        Long idSantuario = this.espirituDAO.santuarioCorrupto();
        if (idSantuario == null) return reporte;

        Ubicacion ubicacion = this.ubicacionDAO.findById(idSantuario).get();

        Long idMedium = this.mediumDAO.mediumConMasDemoniosEn(idSantuario);

        Medium medium = (idMedium != null) ? mediumDAO.findById(idMedium).orElse(null) : null;

        reporte.setNombreSantuario(ubicacion.getNombre());
        reporte.setMedium(medium);
        reporte.setCantidadDeDemonios(this.espirituDAO.cantidadTotalDeDemonios(idSantuario));
        reporte.setCantidadDeDemoniosLibres(this.espirituDAO.cantidadDeDemoniosLibresEn(idSantuario));
        return reporte;
    }

}
