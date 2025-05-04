package ar.edu.unq.epersgeist.controller;


import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTORequestPost;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTORequestPut;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.RecursoNoEncontradoException;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medium")
public class MediumControllerREST {
    private final MediumService mediumService;
    private final UbicacionService ubicacionService;
    private final EspirituService espirituService;

    public MediumControllerREST(MediumService mediumService, UbicacionService ubicacionService,EspirituService espirituService){

        this.mediumService = mediumService;
        this.ubicacionService = ubicacionService;
        this.espirituService = espirituService;

    }

    @PostMapping
    public ResponseEntity<ResponseMessage> crearMedium(@RequestBody MediumDTORequestPost mediumDTO) {

        Medium mediumModelo = mediumDTO.aModelo();

        mediumService.crear(mediumModelo);

        return ResponseEntity.status(201).body( new ResponseMessage("Medium creado: " + mediumModelo.getNombre(), 201));
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
        Medium Medium = mediumService.recuperar(id).orElseThrow(()-> new RecursoNoEncontradoException("Medium", id));

        return ResponseEntity.ok().body(MediumDTOResponse.desdeModelo(Medium));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage> actualizarMedium(@PathVariable Long id, @RequestBody MediumDTORequestPut mediumDTORequest){

        Medium mediumModelo = mediumDTORequest.aModelo(id);

        mediumService.actualizar(mediumModelo);

        return ResponseEntity.ok().body(new ResponseMessage("Medium actualizado ", 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> eliminarMedium(@PathVariable Long id){

        Medium mediumOriginal = mediumService.recuperar(id).orElseThrow(()-> new RecursoNoEncontradoException("Medium", id));

        mediumService.eliminar(id);

        return ResponseEntity.ok().body(new ResponseMessage("Medium eliminado ", 200));
    }


    @PatchMapping("/descansar/{id}")
    public ResponseEntity<ResponseMessage> descansarMedium(@PathVariable Long id){

        mediumService.descansar(id);

        return ResponseEntity.ok().body(new ResponseMessage("descanso realizado ", 200));
    }

    @PatchMapping("/{mediumExorcistaId}/exorcisar/{mediumPoseidoId}")
    public ResponseEntity<ResponseMessage> exorcisar(@PathVariable Long mediumExorcistaId,@PathVariable Long mediumPoseidoId){

        mediumService.exorcisar(mediumExorcistaId,mediumPoseidoId);

        return ResponseEntity.ok().body( new ResponseMessage("exorcismo realizado ", 200));
    }

    @PatchMapping("/{mediumId}/espiritus")
    public ResponseEntity<Set<EspirituDTO>> espiritus(@PathVariable Long mediumId){

        List<Espiritu> espiritus = mediumService.espiritus(mediumId);

        return ResponseEntity.ok().body(espiritus.stream().map(EspirituDTO::desdeModelo).collect(Collectors.toSet()));
    }

    @PatchMapping("/{mediumId}/invocar/{espirituId}")
    public ResponseEntity<EspirituDTO> descansarMedium(@PathVariable Long mediumId,@PathVariable Long espirituId){


        Espiritu espiritu = mediumService.invocar(mediumId, espirituId);

        return ResponseEntity.ok().body(EspirituDTO.desdeModelo(espiritu));
    }
}
