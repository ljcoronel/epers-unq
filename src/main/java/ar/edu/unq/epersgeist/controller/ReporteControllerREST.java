package ar.edu.unq.epersgeist.controller;


import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.ReporteResponseDTO;
import ar.edu.unq.epersgeist.modelo.Reporte.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reporte")
public class ReporteControllerREST {
    private final EstadisticaService estadisticaService;

    public ReporteControllerREST(EstadisticaService estadisticaService){

        this.estadisticaService = estadisticaService;

    }

    @GetMapping("/santuarioCorrupto")
    public ResponseEntity<ReporteResponseDTO> santuarioCorrupto(){

        ReporteSantuarioMasCorrupto reporte = estadisticaService.santuarioCorrupto();

        return ResponseEntity.ok(ReporteResponseDTO.desdeModelo(reporte));
    }

}