package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.MediumDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumRequestDTO;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.RecursoNoEncontradoException;
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

    @PostMapping
    public ResponseEntity<MediumDTO> createMedium(@RequestBody MediumRequestDTO mediumDTO) {
        Medium medium = mediumService.crear(mediumDTO.aModelo());
        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }

    @GetMapping
    public ResponseEntity<List<MediumDTO>> getAllMediums(){
        return ResponseEntity.ok(
                mediumService.recuperarTodos().stream()
                        .map(MediumDTO::desdeModelo)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediumDTO> getMediumById(@PathVariable Long id) {
        Optional<Medium> optionalMedium = mediumService.recuperar(id);
        //en el service me lanzo una excepcion si el medium esta vacio y la capturo en el controlerAdvice
        //deberia lanzar el error desde el controller? o esta bien en el service?
        return ResponseEntity.ok(MediumDTO.desdeModelo(optionalMedium.get()));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<MediumDTO> updateMedium(@PathVariable Long id, @RequestBody MediumRequestDTO mediumDTO) {
//        Op
//    }

//    void actualizar(Medium medium);
//    void eliminar(Long idDelMedium);
//    void descansar(Long mediumId);
//    void exorcisar(Long idMediumExorcista, Long idMediumAExorcisar);
//    List<Espiritu> espiritus(Long mediumId);
//    Espiritu invocar(Long mediumId, Long espirituId);
}
