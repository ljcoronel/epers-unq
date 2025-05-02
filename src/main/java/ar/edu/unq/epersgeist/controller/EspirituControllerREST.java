package ar.edu.unq.epersgeist.controller;


import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOPostRequest;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOResponse;
import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import  ar. edu. unq. epersgeist. modelo. espiritu. Espiritu;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/espiritu")
public final class EspirituControllerREST {
    private final EspirituService espirituService;
    private final UbicacionService ubicacionService;

    public EspirituControllerREST(EspirituService espirituService, UbicacionService ubicacionService ) {
        this.espirituService = espirituService;
        this.ubicacionService = ubicacionService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspirituDTOResponse> actualizarEspiritu(@PathVariable Long id, @RequestBody EspirituDTOPostRequest espirituDTORequest) {
        //TODO dejamos DTOs que no usan ciertos parámetros, o hacemos otro DTO?

        Espiritu espiritu = espirituDTORequest.aModeloId(id);

        espirituService.actualizar(espiritu);
        return ResponseEntity.ok(EspirituDTOResponse.desdeModelo(espiritu));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EspirituDTOResponse> eliminarEspiritu(@PathVariable Long id) {
        Espiritu espiritu = espirituService.recuperar(id)
                .orElseThrow(()->new EntidadInexistenteException("No se encontro el id de la espiritu"));
        espirituService.eliminar(id);
        return ResponseEntity.accepted().build();
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
                .map(EspirituDTOResponse::desdeModelo) // TODO mantenemos algún tipo de default value o no?
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspirituDTOResponse> getEspirituById(@PathVariable Long id) {
        Espiritu espiritu = espirituService.recuperar(id).orElseThrow(() -> new EntidadInexistenteException
                ("Espíritu de id " + id + " no encontrado"));

        return ResponseEntity.ok(EspirituDTOResponse.desdeModelo(espiritu));
    }

    @PostMapping
    public ResponseEntity<EspirituDTOResponse> createEspiritu(@RequestBody EspirituDTOPostRequest espirituDTO) {

        // TODO ojo con NivelDeConexión porque si se pasa para arriba o para abajo del valor válido, lo defaultea en el máximo o mínimo respectivamente
        // preguntar al profe si está bien esto, o si tiramos excepción. Lo mismo puede ir para otros errores.

        Espiritu espirituModelo = espirituDTO.aModelo();
        Espiritu espirituCreado = espirituService.crear(espirituModelo);

        return ResponseEntity
                .status(201)
                .body(EspirituDTOResponse.desdeModelo(espirituCreado));
    }

    @PatchMapping("/{id}/conectar/{mediumId}")
    public ResponseEntity<EspirituDTOResponse> conectar(@PathVariable Long id, @PathVariable Long mediumId) {
        Espiritu espiritu = espirituService.recuperar(id).orElseThrow(() -> new EntidadInexistenteException
                ("Espíritu de id " + id + " no encontrado"));

        espirituService.conectar(id, mediumId);
        Espiritu espirituConectado = espirituService.recuperar(id).get();
        return ResponseEntity.ok(EspirituDTOResponse.desdeModelo(espirituConectado));
    }
}