package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Reporte.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.DAO.jpa.EspirituDAOJPA;
import ar.edu.unq.epersgeist.persistencia.DAO.jpa.MediumDAOJPA;
import ar.edu.unq.epersgeist.persistencia.DAO.jpa.UbicacionDAOJPA;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private final UbicacionDAOJPA ubicacionDAOJPA;
    private final EspirituDAOJPA espirituDAOJPA;
    private final MediumDAOJPA mediumDAOJPA;

    public EstadisticaServiceImpl(UbicacionDAOJPA ubicacionDAOJPA, EspirituDAOJPA espirituDAOJPA, MediumDAOJPA mediumDAOJPA) {
        this.ubicacionDAOJPA = ubicacionDAOJPA;
        this.espirituDAOJPA = espirituDAOJPA;
        this.mediumDAOJPA = mediumDAOJPA;
    }

    @Override
    public ReporteSantuarioMasCorrupto santuarioCorrupto() {
        ReporteSantuarioMasCorrupto reporte = new ReporteSantuarioMasCorrupto();
        Long idSantuario = this.espirituDAOJPA.santuarioCorrupto();
        if (idSantuario == null) return reporte;

        Ubicacion ubicacion = this.ubicacionDAOJPA.findById(idSantuario).get();

        Long idMedium = this.mediumDAOJPA.mediumConMasDemoniosEn(idSantuario);

        Medium medium = (idMedium != null) ? mediumDAOJPA.findById(idMedium).orElse(null) : null;

        reporte.setNombreSantuario(ubicacion.getNombre());
        reporte.setMedium(medium);
        reporte.setCantidadDeDemonios(this.espirituDAOJPA.cantidadTotalDeDemonios(idSantuario));
        reporte.setCantidadDeDemoniosLibres(this.espirituDAOJPA.cantidadDeDemoniosLibresEn(idSantuario));
        return reporte;
    }

}
