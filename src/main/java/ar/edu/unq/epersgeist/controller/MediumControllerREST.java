package ar.edu.unq.epersgeist.controller;


import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTORequestPost;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTORequestPut;
import ar.edu.unq.epersgeist.controller.dto.DTOResponseError;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<MediumDTOResponse> crearMedium(@RequestBody MediumDTORequestPost mediumDTO) {

        Medium mediumModelo = mediumDTO.aModelo();

        Medium medium = mediumService.crear(mediumModelo);

        return ResponseEntity.status(201).body(MediumDTOResponse.desdeModelo(medium));
    }

    @GetMapping
    public ResponseEntity<List<MediumDTOResponse>> getAllMediums(){
        return ResponseEntity.ok(
                mediumService.recuperarTodos().stream()
                        .map(MediumDTOResponse::desdeModelo)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediumDTOResponse> getMediumById(@PathVariable Long id) {
        Optional <Medium> medium = mediumService.recuperar(id);

        if(medium.isEmpty()) return ResponseEntity.status(404).body(null);

        return ResponseEntity.ok().body(MediumDTOResponse.desdeModelo(medium.get()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DTOResponseError> actualizarMedium(@PathVariable Long id, @RequestBody MediumDTORequestPut mediumDTORequest){

        Medium mediumModelo = mediumDTORequest.aModelo(id);

        mediumService.actualizar(mediumModelo);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DTOResponseError> eliminarMedium(@PathVariable Long id){

        mediumService.eliminar(id);

        return ResponseEntity.accepted().build();
    }


    @PatchMapping("/descansar/{id}")
    public ResponseEntity<DTOResponseError> descansarMedium(@PathVariable Long id){

        mediumService.descansar(id);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{mediumExorcistaId}/exorcisar/{mediumPoseidoId}")
    public ResponseEntity<DTOResponseError> exorcisar(@PathVariable Long mediumExorcistaId, @PathVariable Long mediumPoseidoId){

        mediumService.exorcisar(mediumExorcistaId,mediumPoseidoId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{mediumId}/espiritus")
    public ResponseEntity<Set<EspirituDTOResponse>> espiritus(@PathVariable Long mediumId){

        List<Espiritu> espiritus = mediumService.espiritus(mediumId);

        if (espiritus == null) return ResponseEntity.status(404).body(null);

        return ResponseEntity.ok().body(espiritus.stream().map(EspirituDTOResponse::desdeModelo).collect(Collectors.toSet()));
    }

    @PatchMapping("/{mediumId}/invocar/{espirituId}")
    public ResponseEntity<EspirituDTOResponse> invocarEspiritu(@PathVariable Long mediumId,@PathVariable Long espirituId){

        Espiritu espiritu = mediumService.invocar(mediumId, espirituId);

        return ResponseEntity.ok().body(EspirituDTOResponse.desdeModelo(espiritu));
    }
    @PatchMapping("/{mediumId}/mover-a/{ubicacionId}")
    public ResponseEntity<DTOResponseError> moverMediumConEspiritu(@PathVariable Long mediumId,@PathVariable Long ubicacionId){

        mediumService.mover(mediumId, ubicacionId);

        return ResponseEntity.ok().build();
    }
}
