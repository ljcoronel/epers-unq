package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.UbicacionDTORequest;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTOResponse;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.dao.DataIntegrityViolationException;
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
    public ResponseEntity<UbicacionDTOResponse> createUbicacion(@RequestBody UbicacionDTORequest ubicacion) {
        try {
            Ubicacion ubicacionPersistida = this.ubicacionService.crear(ubicacion.aModelo());
            return ResponseEntity.ok(UbicacionDTOResponse.desdeModelo(ubicacionPersistida));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDTOResponse> getUbicacionById(@PathVariable Long id) {
        if (this.ubicacionService.recuperar(id).isPresent()) {
            return ResponseEntity.ok(UbicacionDTOResponse.desdeModelo(this.ubicacionService.recuperar(id).get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UbicacionDTOResponse>> getAllUbicaciones() {
        return ResponseEntity.ok(this.ubicacionService.recuperarTodos().stream()
                .map(UbicacionDTOResponse::desdeModelo)
                .toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UbicacionDTOResponse> updateUbicacion(@PathVariable Long id, @RequestBody UbicacionDTORequest ubicacion) {
        if (!this.ubicacionService.existe(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            Ubicacion ubicacionActual = ubicacion.aModelo(id);
            this.ubicacionService.actualizar(ubicacionActual);
            return ResponseEntity.ok(UbicacionDTOResponse.desdeModelo(ubicacionActual));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UbicacionDTOResponse> deleteUbicacion(@PathVariable Long id) {
        if (this.ubicacionService.existe(id)) {
            this.ubicacionService.eliminar(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
