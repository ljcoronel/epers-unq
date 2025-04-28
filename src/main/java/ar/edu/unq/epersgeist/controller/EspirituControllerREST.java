package ar.edu.unq.epersgeist.controller;


import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTORequest;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOResponse;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
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
    private final UbicacionService ubicacionService;

    public EspirituControllerREST(EspirituService espirituService, UbicacionService ubicacionService ) {
        this.espirituService = espirituService;
        this.ubicacionService = ubicacionService;
    }

    @GetMapping
    public Set<EspirituDTOResponse> getAllEspiritus() {
        return espirituService.recuperarTodos().stream()
                .map(EspirituDTOResponse::desdeModelo)
                .collect(Collectors.toSet());
    }

    @GetMapping("/demonios")
    public Set<EspirituDTOResponse> getEspiritusDemoniacos(@RequestParam(defaultValue = "DESC") Sort.Direction direccion, @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "10") int cantidadPorPagina) {
        return espirituService.espiritusDemoniacos(direccion, pagina, cantidadPorPagina).stream()
                .map(EspirituDTOResponse::desdeModelo)
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspirituDTOResponse> getEspirituById(@PathVariable Long id) { //TODO no sé si sacar el ResponseEntity para poder devolver nada... o algo así.
        Optional<Espiritu> optionalEspiritu = espirituService.recuperar(id);

        if (optionalEspiritu.isEmpty()) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(EspirituDTOResponse.desdeModelo(optionalEspiritu.get()));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createEspiritu(@RequestBody EspirituDTORequest espirituDTO) {

        Optional<Ubicacion> ubicacionOptional = ubicacionService.recuperar(espirituDTO.ubicacionId());
        if (ubicacionOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Ubicación inexistente: " + espirituDTO.ubicacionId()));

        }
        // FALTA COMPROBAR NOMBRE DUPLICADO

        Espiritu espirituModelo = espirituDTO.aModelo(ubicacionOptional.get());

        espirituService.crear(espirituModelo);
        return ResponseEntity.ok( new ResponseMessage("Espiritu creado: " + espirituModelo.getNombre()));
    }
}