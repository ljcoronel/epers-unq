package ar.edu.unq.epersgeist.controller.dto.ReporteDTO;

import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTOResponse;
import ar.edu.unq.epersgeist.modelo.Reporte.ReporteSantuarioMasCorrupto;

public record ReporteResponseDTO(String nombreSantuario, MediumDTOResponse medium, int cantidadDeDemonios, int cantidadDeDemoniosLibres) {

    public static ReporteResponseDTO desdeModelo(ReporteSantuarioMasCorrupto reporte) {
        return new ReporteResponseDTO(
                reporte.getNombreSantuario(),
                MediumDTOResponse.desdeModelo(reporte.getMedium()),
                reporte.getCantidadDeDemonios(),
                reporte.getCantidadDeDemoniosLibres()
        );
    }
}
