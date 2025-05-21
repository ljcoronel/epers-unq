package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.UbicacionDTOPostRequest;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.UbicacionDTOPutRequest;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.UbicacionDTOResponse;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("sobrecargadas/{umbralDeEnergia}")
    public ResponseEntity<List<UbicacionDTOResponse>> ubicacionesSobrecargadas(@PathVariable int umbralDeEnergia) {

        List<Ubicacion> ubicacionesSobrecargadas = ubicacionService.ubicacionesSobrecargadas(umbralDeEnergia);

        return ResponseEntity.ok(ubicacionesSobrecargadas.stream()
                .map(UbicacionDTOResponse::desdeModelo)
                .collect(Collectors.toList()));
    }
}
