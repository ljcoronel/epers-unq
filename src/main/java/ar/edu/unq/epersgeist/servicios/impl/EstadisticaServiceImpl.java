package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Reporte.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private final UbicacionRepository ubicacionRepository;
    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;

    public EstadisticaServiceImpl(UbicacionRepository ubicacionRepository, EspirituDAO espirituDAO, MediumDAO mediumDAO) {
        this.ubicacionRepository = ubicacionRepository;
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
    }

    @Override
    public ReporteSantuarioMasCorrupto santuarioCorrupto() {
        ReporteSantuarioMasCorrupto reporte = new ReporteSantuarioMasCorrupto();
        Long idSantuario = this.espirituDAO.santuarioCorrupto();
        if (idSantuario == null) return reporte;

        Ubicacion ubicacion = this.ubicacionRepository.recuperar(idSantuario).get();

        Long idMedium = this.mediumDAO.mediumConMasDemoniosEn(idSantuario);

        Medium medium = (idMedium != null) ? mediumDAO.findById(idMedium).orElse(null) : null;

        reporte.setNombreSantuario(ubicacion.getNombre());
        reporte.setMedium(medium);
        reporte.setCantidadDeDemonios(this.espirituDAO.cantidadTotalDeDemonios(idSantuario));
        reporte.setCantidadDeDemoniosLibres(this.espirituDAO.cantidadDeDemoniosLibresEn(idSantuario));
        return reporte;
    }

}
