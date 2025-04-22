package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.MediumDTO;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medium")
public class MediumControllerREST {
    private final MediumService mediumService;

    public MediumControllerREST(MediumService mediumService){

        this.mediumService = mediumService;
    }

    @GetMapping
    public Set<MediumDTO>getAllMediums(){
        return mediumService.recuperarTodos().stream()
                .map(MediumDTO::desdeModelo)
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediumDTO> getMediumById(@PathVariable Long id) {
        Optional<Medium> optionalMedium = mediumService.recuperar(id);

        if (optionalMedium.isEmpty()) return ResponseEntity.ok(null);

        return ResponseEntity.ok(MediumDTO.desdeModelo(optionalMedium.get()));
    }

    @PostMapping
    public void createMedium(@RequestBody MediumDTO medium) {

        mediumService.crear(medium.aModelo());
    }
}
