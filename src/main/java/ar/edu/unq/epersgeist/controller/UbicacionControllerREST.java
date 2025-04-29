package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.UbicacionDTORequest;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO.UbicacionDTOResponse;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
            if (ubicacion.nombre() == null) throw new IllegalArgumentException();
            Ubicacion ubicacionPersistida = this.ubicacionService.crear(ubicacion.aModelo());
            return ResponseEntity.ok(UbicacionDTOResponse.desdeModelo(ubicacionPersistida));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
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
        try {
            this.ubicacionService.recuperar(id).orElseThrow();
            if (ubicacion.nombre() == null) throw new IllegalArgumentException();
            Ubicacion ubicacionActual = ubicacion.aModelo(id);
            this.ubicacionService.actualizar(ubicacionActual);
            return ResponseEntity.ok(UbicacionDTOResponse.desdeModelo(ubicacionActual));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UbicacionDTOResponse> deleteUbicacion(@PathVariable Long id) {
        if (this.ubicacionService.recuperar(id).isEmpty()) return ResponseEntity.notFound().build();
        this.ubicacionService.eliminar(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/espiritus")
    public ResponseEntity<List<EspirituDTOResponse>> espiritusEnUbicacion(@PathVariable Long id) {
        if (this.ubicacionService.recuperar(id).isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(this.ubicacionService.espiritusEn(id).stream()
                .map(EspirituDTOResponse::desdeModelo)
                .toList());
    }

    @GetMapping("/{id}/mediumsSinEspiritus")
    public ResponseEntity<List<MediumDTOResponse>> mediumsSinEspiritusEnUbicacion(@PathVariable Long id) {
        if (this.ubicacionService.recuperar(id).isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(this.ubicacionService.mediumsSinEspiritusEn(id).stream()
                .map(MediumDTOResponse::desdeModelo)
                .toList());
    }
}
