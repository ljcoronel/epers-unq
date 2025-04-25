package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import  ar. edu. unq. epersgeist. modelo. espiritu. Espiritu;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/espiritu")
public final class EspirituControllerREST {
    private final EspirituService espirituService;
    private final MediumService mediumService;

    public EspirituControllerREST(EspirituService espirituService, MediumService mediumService) {
        this.espirituService = espirituService;
        this.mediumService = mediumService;
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
    public void createEspiritu(@RequestBody EspirituDTO espiritu) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Espiritu espirituModelo = espiritu.aModelo();
        if (!espirituModelo.estaLibre() && espirituModelo.getMedium() != null ) { //  esto es para evitar la recursividad en los DTOs, de forma que corta en Espíritu.
            // básicamente le pido el medium id al DTO, lo hago un Medium a través del service y luego lo meto forzosamente al DTO para crearlo.
            Medium medium = mediumService.recuperar(espiritu.mediumId()).get(); //TODO chequear optional
            espirituModelo.setMedium(medium);
        }
        espirituService.crear(espirituModelo);
    }
}