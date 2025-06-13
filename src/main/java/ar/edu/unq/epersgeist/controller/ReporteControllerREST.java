package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.ReporteResponseDTO;
import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.SnapshotResponseDTO;
import ar.edu.unq.epersgeist.modelo.Reporte.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @PostMapping("/snapshot")
    public ResponseEntity<SnapshotResponseDTO> crearSnapshot() {
        this.estadisticaService.crearSnapshot();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/snapshot")
    public ResponseEntity<SnapshotResponseDTO> obtenerSnapshot(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        if (this.estadisticaService.obtenerSnapshot(fecha) == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(SnapshotResponseDTO.desdeModelo(this.estadisticaService.obtenerSnapshot(fecha)));
    }
}