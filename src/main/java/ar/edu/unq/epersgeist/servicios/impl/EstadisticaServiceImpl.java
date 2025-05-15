package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Reporte.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private final UbicacionRepository ubicacionRepository;
    private final EspirituRepository espirituRepository;
    private final MediumRepository mediumRepository;

    public EstadisticaServiceImpl(UbicacionRepository ubicacionRepository, EspirituRepository espirituRepository, MediumRepository mediumRepository) {
        this.ubicacionRepository = ubicacionRepository;
        this.espirituRepository = espirituRepository;
        this.mediumRepository = mediumRepository;
    }

    @Override
    public ReporteSantuarioMasCorrupto santuarioCorrupto() {
        ReporteSantuarioMasCorrupto reporte = new ReporteSantuarioMasCorrupto();
        Long idSantuario = this.espirituRepository.santuarioCorrupto();
        if (idSantuario == null) return reporte;

        Ubicacion ubicacion = this.ubicacionRepository.recuperar(idSantuario).get();

        Long idMedium = this.mediumRepository.mediumConMasDemoniosEn(idSantuario);

        Medium medium = (idMedium != null) ? mediumRepository.recuperar(idMedium).orElse(null) : null;

        reporte.setNombreSantuario(ubicacion.getNombre());
        reporte.setMedium(medium);
        reporte.setCantidadDeDemonios(this.espirituRepository.cantidadTotalDeDemonios(idSantuario));
        reporte.setCantidadDeDemoniosLibres(this.espirituRepository.cantidadDeDemoniosLibresEn(idSantuario));
        return reporte;
    }

}
