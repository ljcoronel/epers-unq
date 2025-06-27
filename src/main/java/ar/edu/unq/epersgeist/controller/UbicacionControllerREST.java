package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.EventoPortalDTO.EventoPortalDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.CentralityResponseDTO;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.UbicacionDTOPostRequest;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.UbicacionDTOPutRequest;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.UbicacionDTOResponse;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.influx.EventoPortalDTO;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ubicacion")
public final class UbicacionControllerREST {
    private final UbicacionService ubicacionService;

    public UbicacionControllerREST(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @PostMapping
    public ResponseEntity<UbicacionDTOResponse> createUbicacion(@RequestBody UbicacionDTOPostRequest ubicacionRequest) {
        Ubicacion ubicacion = this.ubicacionService.crear(ubicacionRequest.aModelo());
        return ResponseEntity.status(HttpStatus.CREATED).body(UbicacionDTOResponse.desdeModelo(ubicacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDTOResponse> getUbicacionById(@PathVariable Long id) {
        if (this.ubicacionService.recuperar(id).isEmpty()) return ResponseEntity.status(404).body(null);
        Ubicacion ubicacion = this.ubicacionService.recuperar(id).get();
        return ResponseEntity.ok(UbicacionDTOResponse.desdeModelo(ubicacion));
    }

    @GetMapping
    public ResponseEntity<Set<UbicacionDTOResponse>> getAllUbicaciones() {
        return ResponseEntity.ok(this.ubicacionService.recuperarTodos().stream()
                .map(UbicacionDTOResponse::desdeModelo)
                .collect(Collectors.toSet()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UbicacionDTOResponse> updateUbicacion(@PathVariable Long id, @RequestBody UbicacionDTOPutRequest ubicacionRequest) {
        Ubicacion ubicacion = ubicacionRequest.aModelo(id);

        this.ubicacionService.actualizar(ubicacion);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UbicacionDTOResponse> deleteUbicacion(@PathVariable Long id) {
        this.ubicacionService.eliminar(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/espiritus")
    public ResponseEntity<Set<EspirituDTOResponse>> espiritusEnUbicacion(@PathVariable Long id) {
        return ResponseEntity.ok(this.ubicacionService.espiritusEn(id).stream()
                .map(EspirituDTOResponse::desdeModelo)
                .collect(Collectors.toSet()));
    }

    @GetMapping("/{id}/mediumsSinEspiritus")
    public ResponseEntity<Set<MediumDTOResponse>> mediumsSinEspiritusEnUbicacion(@PathVariable Long id) {
        return ResponseEntity.ok(this.ubicacionService.mediumsSinEspiritusEn(id).stream()
                .map(MediumDTOResponse::desdeModelo)
                .collect(Collectors.toSet()));
    }

    @PatchMapping("/{idOrigen}/conectar/{idDestino}")
    public ResponseEntity<ResponseEntity.BodyBuilder> conectar(@PathVariable Long idOrigen, @PathVariable Long idDestino) {
        this.ubicacionService.conectar(idOrigen, idDestino);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{idOrigen}/estanConectadas/{idDestino}")
    public ResponseEntity<Boolean> estanConectadas(@PathVariable Long idOrigen, @PathVariable Long idDestino) {
        Boolean estanConectadas = this.ubicacionService.estanConectadas(idOrigen, idDestino);
        return ResponseEntity.ok(estanConectadas);
    }

    @GetMapping("/{idOrigen}/CaminoMasCorto/{idDestino}")
    public ResponseEntity<Set<UbicacionDTOResponse>> CaminoMasCorto(@PathVariable Long idOrigen, @PathVariable Long idDestino){

        List<Ubicacion> camino = ubicacionService.caminoMasCorto(idOrigen,idDestino);

        if (camino == null) return ResponseEntity.status(404).body(null);

        return ResponseEntity.ok(camino.stream()
                .map(UbicacionDTOResponse::desdeModelo)
                .collect(Collectors.toSet()));
    }

    @GetMapping("/centrality/ClosenessOf")
    public ResponseEntity<List<CentralityResponseDTO>> closenessOf(@RequestParam(required = false) List<Long> ids) {
        return ResponseEntity.ok(this.ubicacionService.closenessOf(ids).stream()
                .map(CentralityResponseDTO::resultadoCloseness)
                .collect(Collectors.toList()));
    }

    @GetMapping("/centrality/DegreeOf/incoming")
    public ResponseEntity<List<CentralityResponseDTO>> incomingDegreeOf(@RequestParam(required = false) List<Long> ids) {
        return ResponseEntity.ok(this.ubicacionService.incomingDegreeOf(ids).stream()
                .map(CentralityResponseDTO::resultadoDegree)
                .collect(Collectors.toList()));
    }

    @GetMapping("/centrality/DegreeOf/outgoing")
    public ResponseEntity<List<CentralityResponseDTO>> outcomingDegreeOf(@RequestParam(required = false) List<Long> ids) {
        return ResponseEntity.ok(this.ubicacionService.outgoingDegreeOf(ids).stream()
                .map(CentralityResponseDTO::resultadoDegree)
                .collect(Collectors.toList()));
    }

    @GetMapping("sobrecargadas/{umbralDeEnergia}")
    public ResponseEntity<List<UbicacionDTOResponse>> ubicacionesSobrecargadas(@PathVariable int umbralDeEnergia) {

        List<Ubicacion> ubicacionesSobrecargadas = ubicacionService.ubicacionesSobrecargadas(umbralDeEnergia);

        return ResponseEntity.ok(ubicacionesSobrecargadas.stream()
                .map(UbicacionDTOResponse::desdeModelo)
                .collect(Collectors.toList()));
    }

    @GetMapping("/ubicacionMasPeligrosa")
    public ResponseEntity<UbicacionDTOResponse> ubicacionMasPeligrosa(){
        Optional<Ubicacion> optionalUbicacion = ubicacionService.ubicacionMasPeligrosa();

        if (optionalUbicacion.isEmpty()) return ResponseEntity.status(404).body(null);
        Ubicacion ubicacion = optionalUbicacion.get();
        return ResponseEntity.ok(UbicacionDTOResponse.desdeModelo(ubicacion));
    }

    @GetMapping("/ubicacionMasTransitada")
    public ResponseEntity<UbicacionDTOResponse> ubicacionMasTransitada(){
        Optional<Ubicacion> optionalUbicacion = ubicacionService.ubicacionConMasTransitoDeEspiritus();

        if (optionalUbicacion.isEmpty()) return ResponseEntity.status(404).body(null);
        Ubicacion ubicacion = optionalUbicacion.get();
        return ResponseEntity.ok(UbicacionDTOResponse.desdeModelo(ubicacion));
    }

    @GetMapping("/actividadDePortales")
    public ResponseEntity<List<EventoPortalDTOResponse>> actividadDePortales(){
        List<EventoPortalDTO> eventosPortales = ubicacionService.eventosDePortalesOcurridos();

        return ResponseEntity.ok(eventosPortales.stream()
                .map(EventoPortalDTOResponse::desdeModelo)
                .collect(Collectors.toList()));
    }
}
