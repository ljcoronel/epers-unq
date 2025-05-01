package ar.edu.unq.epersgeist.controller;


import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTORequest;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.RecursoNoEncontradoException;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.modelo.exception.RecursoNoEncontradoException;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.http.HttpStatus;
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
    private final UbicacionService ubicacionService;
    private final EspirituService espirituService;

    public MediumControllerREST(MediumService mediumService, UbicacionService ubicacionService,EspirituService espirituService){

        this.mediumService = mediumService;
        this.ubicacionService = ubicacionService;
        this.espirituService = espirituService;

    }

    @PostMapping
    public ResponseEntity<ResponseMessage> crearMedium(@RequestBody MediumDTORequest mediumDTO) {
        Ubicacion ubicacion =  ubicacionService.recuperar(mediumDTO.ubicacionID())
                .orElseThrow(()-> new RecursoNoEncontradoException("Ubicacion", mediumDTO.ubicacionID()));
        Medium mediumModelo = mediumDTO.aModelo(ubicacion);

        mediumService.crear(mediumModelo);

        return ResponseEntity.status(201).body( new ResponseMessage("Medium creado: " + mediumModelo.getNombre(), 201));
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
        Medium Medium = mediumService.recuperar(id).orElseThrow(()-> new RecursoNoEncontradoException("Medium", id));

        return ResponseEntity.ok(MediumDTO.desdeModelo(Medium));
    }

    @PutMapping("/{id}")//REVISAR METODO
    public ResponseEntity<ResponseMessage> actualizarMedium(@PathVariable Long id, @RequestBody MediumDTORequest mediumDTORequest){

        Medium mediumOriginal = mediumService.recuperar(id).orElseThrow(()-> new RecursoNoEncontradoException("Medium", id));

        Ubicacion ubicacion = ubicacionService.recuperar(mediumDTORequest.ubicacionID()).orElseThrow(()-> new RecursoNoEncontradoException("Ubicacion", mediumDTORequest.ubicacionID()));

        Medium mediumModelo = mediumDTORequest.aModelo(ubicacion);

        //buscar optimizar
        mediumModelo.setId(mediumOriginal.getId());
        mediumModelo.setAngeles(mediumOriginal.getAngeles());
        mediumModelo.setDemonios(mediumOriginal.getDemonios());

        mediumService.actualizar(mediumModelo);

        return ResponseEntity.ok( new ResponseMessage("Medium actualizado ", 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> eliminarMedium(@PathVariable Long id){
        Optional<Medium> optionalMedium = mediumService.recuperar(id);

        if (optionalMedium.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        mediumService.eliminar(id);

        return ResponseEntity.ok( new ResponseMessage("Medium eliminado ", 200));
    }


    @PatchMapping("/descansar/{id}")
    public ResponseEntity<ResponseMessage> descansarMedium(@PathVariable Long id){
        Medium medium = mediumService.recuperar(id).orElseThrow(()-> new RecursoNoEncontradoException("Medium", id));
        mediumService.descansar(id);

        return ResponseEntity.ok( new ResponseMessage("descanso realizado ", 200));
    }

    @PatchMapping("/{mediumExorcistaId}/exorcisar/{mediumPoseidoId}")
    public ResponseEntity<ResponseMessage> exorcisar(@PathVariable Long mediumExorcistaId,@PathVariable Long mediumPoseidoId){
        Medium mediumExorcista = mediumService.recuperar(mediumExorcistaId).orElseThrow(()-> new RecursoNoEncontradoException("Medium", mediumExorcistaId));

        Medium mediumPoseido = mediumService.recuperar(mediumPoseidoId).orElseThrow(()-> new RecursoNoEncontradoException("Medium", mediumPoseidoId));

        //verificar si exorcista tiene angeles

        mediumService.exorcisar(mediumExorcistaId,mediumPoseidoId);

        return ResponseEntity.ok( new ResponseMessage("exorcismo realizado ", 200));
    }

    @PatchMapping("/{mediumId}/espiritus")
    public ResponseEntity<Set<EspirituDTO>> espiritus(@PathVariable Long mediumId){

        Medium medium = mediumService.recuperar(mediumId).orElseThrow(()-> new RecursoNoEncontradoException("Medium", mediumId));

        List<Espiritu> espiritus = mediumService.espiritus(mediumId);

        return ResponseEntity.ok(espiritus.stream()
                .map(EspirituDTO::desdeModelo)
                .collect(Collectors.toSet()));
    }

    @PatchMapping("/{mediumId}/invocar/{espirituId}")
    public ResponseEntity<EspirituDTO> descansarMedium(@PathVariable Long mediumId,@PathVariable Long espirituId){
        Medium medium = mediumService.recuperar(mediumId).orElseThrow(() -> new RecursoNoEncontradoException("Medium", mediumId));
        Espiritu espiritu = espirituService.recuperar(espirituId).orElseThrow(() -> new RecursoNoEncontradoException("Espiritu", espirituId));

        mediumService.invocar(mediumId, espirituId);

        return ResponseEntity.ok(EspirituDTO.desdeModelo(espiritu));
    }
}
