package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
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

    @GetMapping
    public Set<EspirituDTO> getAllEspiritus() {
        return espirituService.recuperarTodos().stream()
                .map(EspirituDTO::desdeModelo)
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspirituDTO> getEspirituById(@PathVariable Long id) { //TODO no sé si sacar el ResponseEntity para poder devolver nada... o algo así.
        Optional<Espiritu> optionalEspiritu = espirituService.recuperar(id);

        if (optionalEspiritu.isEmpty()) return ResponseEntity.ok(null);

        return ResponseEntity.ok(EspirituDTO.desdeModelo(optionalEspiritu.get()));
    }

    @PostMapping
    public void createEspiritu(@RequestBody EspirituDTO espiritu) {
        espirituService.crear(espiritu.aModelo());
    }
}