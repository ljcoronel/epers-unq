package ar.edu.unq.epersgeist.servicios.impl;

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

    private UbicacionDAO ubicacionDAO;
    private EspirituDAO espirituDAO;
    private MediumDAO mediumDAO;

    public EstadisticaServiceImpl(UbicacionDAO ubicacionDAO, EspirituDAO espirituDAO, MediumDAO mediumDAO) {
        this.ubicacionDAO = ubicacionDAO;
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
    }

    @Override
    public ReporteSantuarioMasCorrupto santuarioCorrupto() {
        ReporteSantuarioMasCorrupto reporte = new ReporteSantuarioMasCorrupto();
        Long id = this.espirituDAO.santuarioCorrupto();
        if (id == null || this.ubicacionDAO.findById(id).isEmpty()) return reporte;
        Ubicacion ubicacion = this.ubicacionDAO.findById(id).get();
        reporte.setNombreSantuario(ubicacion.getNombre());
        //reporte.setMedium(this.mediumDAO.mediumConMasDemonios(id));
        reporte.setCantidadDeDemonios(this.espirituDAO.cantidadTotalDeDemonios(id));
        //reporte.setCantidadDeDemoniosLibres(this.espirituDAO.cantidadDeDemoniosLibresEn(id));
        return reporte;
    }
}
