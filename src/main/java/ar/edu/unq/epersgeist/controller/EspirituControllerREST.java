package ar.edu.unq.epersgeist.controller;


import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTORequest;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import  ar. edu. unq. epersgeist. modelo. espiritu. Espiritu;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/espiritu")
public final class EspirituControllerREST {
    private final EspirituService espirituService;
    private final UbicacionService ubicacionService;

    public EspirituControllerREST(EspirituService espirituService, UbicacionService ubicacionService ) {
        this.espirituService = espirituService;
        this.ubicacionService = ubicacionService;
    }

    @GetMapping
    public Set<EspirituDTOResponse> getAllEspiritus() {
        return espirituService.recuperarTodos().stream()
                .map(EspirituDTOResponse::desdeModelo)
                .collect(Collectors.toSet());
    }

    @GetMapping("/demonios")
    public Set<EspirituDTOResponse> getEspiritusDemoniacos(@RequestParam(defaultValue = "DESC") Sort.Direction direccion, @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "10") int cantidadPorPagina) {
        // TODO no puedo hacer chequeos al menos que pueda devolver ResponseEntity...

        return espirituService.espiritusDemoniacos(direccion, pagina, cantidadPorPagina).stream()
                .map(EspirituDTOResponse::desdeModelo) // TODO mantenemos algún tipo de default value o no?
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspirituDTOResponse> getEspirituById(@PathVariable Long id) {
        Optional<Espiritu> optionalEspiritu = espirituService.recuperar(id);

        if (optionalEspiritu.isEmpty()) return ResponseEntity.status(404).build(); // TODO no sé cómo hacer que se le pueda pasar un mensaje

        return ResponseEntity.ok(EspirituDTOResponse.desdeModelo(optionalEspiritu.get()));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createEspiritu(@RequestBody EspirituDTORequest espirituDTO) {

//        // TIPO
//        if (!TipoEspiritu.esTipoValido(espirituDTO.tipo().name())) {
//            return ResponseEntity.badRequest()
//                    .body(new ResponseMessage("Tipo erróneo: " + espirituDTO.tipo(), 400)); // TODO creo que no se puede hacer esto, porque directamente sale Bad Request por parte de Spring
//        }

        // NIVEL DE CONEXIÓN
        if (espirituDTO.nivelDeConexion() > 100 || espirituDTO.nivelDeConexion() < 0) { //TODO aclaramos cada caso?
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseMessage("Valor inválido de nivel de conexión: " + espirituDTO.nivelDeConexion(), 400));
        }

        // UBICACIÓN
        Optional<Ubicacion> ubicacionOptional = ubicacionService.recuperar(espirituDTO.ubicacionId());
        if (ubicacionOptional.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new ResponseMessage("Ubicación no encontrada: " + espirituDTO.ubicacionId(), 404));

        }

        // NOMBRE
        if (espirituService.existeConNombre(espirituDTO.nombre())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseMessage("Nombre duplicado: " + espirituDTO.nombre(), 400));
        }



        Espiritu espirituModelo = espirituDTO.aModelo(ubicacionOptional.get());
        espirituService.crear(espirituModelo);
        return ResponseEntity
                .status(201)
                .body( new ResponseMessage("Espiritu creado: " + espirituModelo.getNombre(), 201));
    }
}