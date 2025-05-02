package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.UbicacionDTORequest;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.UbicacionDTOResponse;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ubicacion")
public final class UbicacionControllerREST {
    private final UbicacionService ubicacionService;

    public UbicacionControllerREST(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @PostMapping
    public ResponseEntity<UbicacionDTOResponse> createUbicacion(@RequestBody UbicacionDTORequest ubicacionRequest) {
        Ubicacion ubicacion = this.ubicacionService.crear(ubicacionRequest.aModelo());
        return ResponseEntity.status(HttpStatus.CREATED).body(UbicacionDTOResponse.desdeModelo(ubicacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDTOResponse> getUbicacionById(@PathVariable Long id) {
        Ubicacion ubicacion = this.ubicacionService.recuperar(id).get();
        return ResponseEntity.ok(UbicacionDTOResponse.desdeModelo(ubicacion));
    }

    @GetMapping
    public ResponseEntity<List<UbicacionDTOResponse>> getAllUbicaciones() {
        return ResponseEntity.ok(this.ubicacionService.recuperarTodos().stream()
                .map(UbicacionDTOResponse::desdeModelo)
                .toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UbicacionDTOResponse> updateUbicacion(@PathVariable Long id, @RequestBody UbicacionDTORequest ubicacionRequest) {
        Ubicacion ubicacion = ubicacionRequest.aModelo(id);
        this.ubicacionService.actualizar(ubicacion);
        return ResponseEntity.ok(UbicacionDTOResponse.desdeModelo(ubicacion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UbicacionDTOResponse> deleteUbicacion(@PathVariable Long id) {
        this.ubicacionService.eliminar(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/espiritus")
    public ResponseEntity<List<EspirituDTOResponse>> espiritusEnUbicacion(@PathVariable Long id) {
        return ResponseEntity.ok(this.ubicacionService.espiritusEn(id).stream()
                .map(EspirituDTOResponse::desdeModelo)
                .toList());
    }

    @GetMapping("/{id}/mediumsSinEspiritus")
    public ResponseEntity<List<MediumDTOResponse>> mediumsSinEspiritusEnUbicacion(@PathVariable Long id) {
        return ResponseEntity.ok(this.ubicacionService.mediumsSinEspiritusEn(id).stream()
                .map(MediumDTOResponse::desdeModelo)
                .toList());
    }
}
