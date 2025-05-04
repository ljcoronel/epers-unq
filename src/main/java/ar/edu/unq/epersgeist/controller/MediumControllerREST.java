package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTOResponse;
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
    public Set<MediumDTOResponse>getAllMediums(){
        return mediumService.recuperarTodos().stream()
                .map(MediumDTOResponse::desdeModelo)
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediumDTOResponse> getMediumById(@PathVariable Long id) {
        Optional<Medium> optionalMedium = mediumService.recuperar(id);

        if (optionalMedium.isEmpty()) return ResponseEntity.ok(null);

        return ResponseEntity.ok(MediumDTOResponse.desdeModelo(optionalMedium.get()));
    }

    @PostMapping
    public void createMedium(@RequestBody MediumDTOResponse medium) {

        mediumService.crear(medium.aModelo());
    }
}
