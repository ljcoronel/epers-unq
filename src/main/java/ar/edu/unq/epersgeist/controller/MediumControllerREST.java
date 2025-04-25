package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.MediumDTO;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
    public ResponseEntity<?> getMediumById(@PathVariable Long id) {
        Optional<Medium> optionalMedium = mediumService.recuperar(id);

        if (optionalMedium.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new HashMap<>().put("error", "Medium no encontrado"));

        return ResponseEntity.ok(MediumDTO.desdeModelo(optionalMedium.get()));
    }

    @PostMapping
    public void createMedium(@RequestBody MediumDTO medium) {

        mediumService.crear(medium.aModelo());
    }

//    Medium crear(Medium medium);
//    void actualizar(Medium medium);
//    void eliminar(Long idDelMedium);
//    void descansar(Long mediumId);
//    void exorcisar(Long idMediumExorcista, Long idMediumAExorcisar);
//    List<Espiritu> espiritus(Long mediumId);
//    Espiritu invocar(Long mediumId, Long espirituId);
}
