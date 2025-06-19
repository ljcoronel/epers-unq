package ar.edu.unq.epersgeist.controller;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOPostRequest;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOPutRequest;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.PortalDTO.PortalDTORequest;
import ar.edu.unq.epersgeist.controller.dto.PortalDTO.PortalDTOResponse;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.PortalEspiritus;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import  ar. edu. unq. epersgeist. modelo. espiritu. Espiritu;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/espiritu")
public final class EspirituControllerREST {
    private final EspirituService espirituService;

    public EspirituControllerREST(EspirituService espirituService) {
        this.espirituService = espirituService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseEntity.BodyBuilder> actualizarEspiritu(@PathVariable Long id, @RequestBody EspirituDTOPutRequest espirituDTOPutRequest) {

        Espiritu espiritu = espirituDTOPutRequest.aModeloId(id);

        espirituService.actualizar(espiritu);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EspirituDTOResponse> eliminarEspiritu(@PathVariable Long id) {

        espirituService.eliminar(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public Set<EspirituDTOResponse> getAllEspiritus() {
        return espirituService.recuperarTodos().stream()
                .map(EspirituDTOResponse::desdeModelo)
                .collect(Collectors.toSet());
    }

    @GetMapping("/demonios")
    public Set<EspirituDTOResponse> getEspiritusDemoniacos(@RequestParam Sort.Direction direccion, @RequestParam int pagina, @RequestParam int cantidadPorPagina) {
        return espirituService.espiritusDemoniacos(direccion, pagina, cantidadPorPagina).stream()
                .map(EspirituDTOResponse::desdeModelo)
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspirituDTOResponse> getEspirituById(@PathVariable Long id) {
        Optional<Espiritu> espiritu = espirituService.recuperar(id);

        if (espiritu.isEmpty()) return ResponseEntity.status(404).body(null);

        return ResponseEntity.ok(EspirituDTOResponse.desdeModelo(espiritu.get()));
    }

    @PostMapping
    public ResponseEntity<EspirituDTOResponse> createEspiritu(@RequestBody EspirituDTOPostRequest espirituDTO) {

        Espiritu espirituModelo = espirituDTO.aModelo();
        Espiritu espirituCreado = espirituService.crear(espirituModelo);

        return ResponseEntity.status(201).body(EspirituDTOResponse.desdeModelo(espirituCreado));
    }

    @PostMapping("/crearPortal")
    public ResponseEntity<PortalDTOResponse> crearPortal(@RequestBody PortalDTORequest portalDTORequest) {

        PortalEspiritus portalCreado = espirituService.crearPortal(portalDTORequest.aModelo());

        return ResponseEntity.status(201).body(PortalDTOResponse.desdeModelo(portalCreado));
    }

    @PatchMapping("/{id}/conectar/{mediumId}")
    public ResponseEntity<MediumDTOResponse> conectar(@PathVariable Long id, @PathVariable Long mediumId) {
        Medium medium = espirituService.conectar(id, mediumId);

        return ResponseEntity.ok().body(MediumDTOResponse.desdeModelo(medium));
    }

    @PatchMapping("/{espirituDominanteId}/dominar/{espirituADominarId}")
    public ResponseEntity<Void> dominar(@PathVariable Long espirituDominanteId, @PathVariable Long espirituADominarId) {
        espirituService.dominar(espirituDominanteId, espirituADominarId);

        return ResponseEntity.ok().body(null);
    }
}