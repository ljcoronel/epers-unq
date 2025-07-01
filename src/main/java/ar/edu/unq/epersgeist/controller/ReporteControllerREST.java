package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.EventoPortalDTO.EventoPortalDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.ReporteResponseDTO;
import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.Snapshot.SnapshotResponseDTO;
import ar.edu.unq.epersgeist.modelo.Reporte.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.influx.EventoPortalDTO;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/actividadDePortales")
    public ResponseEntity<List<EventoPortalDTOResponse>> actividadDePortales(){
        List<EventoPortalDTO> eventosPortales = estadisticaService.eventosDePortalesOcurridos();

        return ResponseEntity.ok(eventosPortales.stream()
                .map(EventoPortalDTOResponse::desdeModelo)
                .collect(Collectors.toList()));
    }
}