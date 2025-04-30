package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.MediumDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumRequestDTO;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.RecursoNoEncontradoException;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        Medium medium =  mediumService.recuperar(id)
                .orElseThrow(()-> new RecursoNoEncontradoException("Medium", id));
        //en el service me lanzo una excepcion si el medium esta vacio y la capturo en el controlerAdvice
        //deberia lanzar el error desde el controller? o esta bien en el service?

        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediumDTO> updateMedium(@PathVariable Long id, @RequestBody MediumRequestDTO mediumRequestDTO){
        Medium updateMedium = mediumService.recuperar(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Medium", id));

        Medium mediumReq = mediumRequestDTO.aModelo();

        updateMedium.setNombre(mediumReq.getNombre());
        updateMedium.setManaMax(mediumReq.getManaMax());
        updateMedium.setMana(mediumReq.getMana());
        if(mediumReq.getUbicacion() != null) updateMedium.setUbicacion(mediumReq.getUbicacion());

        mediumService.actualizar(updateMedium);

        return ResponseEntity.ok(MediumDTO.desdeModelo(updateMedium));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MediumDTO> deleteMedium(@PathVariable Long id) {
        mediumService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/descansar/{id}")
    public ResponseEntity<MediumDTO> descansarMedium(@PathVariable Long id){

        mediumService.descansar(id);

        Medium mediumDescansado = mediumService.recuperar(id)
                .orElseThrow(()-> new RecursoNoEncontradoException("Medium", id));

        return ResponseEntity.ok(MediumDTO.desdeModelo(mediumDescansado));
    }

//    void exorcisar(Long idMediumExorcista, Long idMediumAExorcisar);
//    List<Espiritu> espiritus(Long mediumId);
//    Espiritu invocar(Long mediumId, Long espirituId);
}
