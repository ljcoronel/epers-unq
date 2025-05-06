package ar.edu.unq.epersgeist.controller;


import ar.edu.unq.epersgeist.controller.dto.DTOResponseError;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.ReporteDTOResponse;
import ar.edu.unq.epersgeist.modelo.Reporte.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reporte")
public class ReporteControllerREST {
    private final EstadisticaService estadisticaService;

    public ReporteControllerREST(EstadisticaService estadisticaService){

        this.estadisticaService = estadisticaService;

    }

    @GetMapping("/santuarioCorrupto")
    public ResponseEntity<ReporteDTOResponse> santuarioCorrupto(){

        ReporteSantuarioMasCorrupto reporte = estadisticaService.santuarioCorrupto();

        return ResponseEntity.ok(ReporteDTOResponse.desdeModelo(reporte));
    }

}