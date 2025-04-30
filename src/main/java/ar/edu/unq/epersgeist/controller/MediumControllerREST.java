package ar.edu.unq.epersgeist.controller;


import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO.MediumDTORequest;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
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

    @GetMapping
    public Set<MediumDTO>getAllMediums(){
        return mediumService.recuperarTodos().stream()
                .map(MediumDTO::desdeModelo)
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediumDTO> getMediumById(@PathVariable Long id) {
        Optional<Medium> optionalMedium = mediumService.recuperar(id);

        if (optionalMedium.isEmpty()) return ResponseEntity.status(404).build();

        return ResponseEntity.ok(MediumDTO.desdeModelo(optionalMedium.get()));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> crearMedium(@RequestBody MediumDTORequest mediumDTO) {//no verifico nombre repetido porque medium no tiene unique
        Optional<Ubicacion> ubicacionOptional = ubicacionService.recuperar(mediumDTO.ubicacionID());
        Medium mediumModelo = mediumDTO.aModelo(ubicacionOptional.get());

        mediumService.crear(mediumModelo);

        return ResponseEntity.status(201).body( new ResponseMessage("Medium creado: " + mediumModelo.getNombre(), 201));
    }

    @PutMapping("/{id}")//REVISAR METODO
    public ResponseEntity<ResponseMessage> actualizarMedium(@PathVariable Long id, @RequestBody MediumDTORequest mediumDTO){
        Optional<Medium> optionalMedium = mediumService.recuperar(id);
        if (optionalMedium.isEmpty()) return ResponseEntity.status(404).build();

        Optional<Ubicacion> ubicacionOptional = ubicacionService.recuperar(mediumDTO.ubicacionID());//chekear que existe ubicacion
        Medium mediumModelo = mediumDTO.aModelo(ubicacionOptional.get());

        //buscar optimizar
        mediumModelo.setNombre(mediumDTO.nombre());
        mediumModelo.setManaMax(mediumDTO.manaMax());
        mediumModelo.setMana(mediumDTO.mana());
        mediumModelo.setUbicacion(ubicacionOptional.get());

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
        Optional<Medium> optionalMedium = mediumService.recuperar(id);
        if (optionalMedium.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        mediumService.descansar(id);

        return ResponseEntity.ok( new ResponseMessage("descanso realizado ", 200));
    }

    @PatchMapping("/{mediumExorcistaId}/exorcisar/{mediumPoseidoId}")
    public ResponseEntity<ResponseMessage> exorcisar(@PathVariable Long mediumExorcistaId,@PathVariable Long mediumPoseidoId){
        Optional<Medium> optionalExorcista = mediumService.recuperar(mediumExorcistaId);
        Optional<Medium> optionalPoseido = mediumService.recuperar(mediumPoseidoId);
        if (optionalExorcista.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        if (optionalPoseido.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        //verificar si exorcista tiene angeles

        mediumService.exorcisar(mediumExorcistaId,mediumPoseidoId);

        return ResponseEntity.ok( new ResponseMessage("exorcismo realizado ", 200));
    }

    @PatchMapping("/{mediumId}/espiritus")
    public ResponseEntity<Set<EspirituDTO>> espiritus(@PathVariable Long mediumId){
        Optional<Medium> optionalMedium = mediumService.recuperar(mediumId);
        if (optionalMedium.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        List<Espiritu> espiritus = mediumService.espiritus(mediumId);

        return ResponseEntity.ok(espiritus.stream()
                .map(EspirituDTO::desdeModelo)
                .collect(Collectors.toSet()));
    }

    @PatchMapping("/{mediumId}/invocar/{espirituId}")
    public ResponseEntity<EspirituDTO> descansarMedium(@PathVariable Long mediumId,@PathVariable Long espirituId){
        Optional<Medium> optionalMedium = mediumService.recuperar(mediumId);
        Optional<Espiritu> optionalEspiritu = espirituService.recuperar(espirituId);
        if (optionalMedium.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        if (optionalEspiritu.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        mediumService.invocar(mediumId,espirituId);

        return ResponseEntity.ok(EspirituDTO.desdeModelo(optionalEspiritu.get()));
    }
}
