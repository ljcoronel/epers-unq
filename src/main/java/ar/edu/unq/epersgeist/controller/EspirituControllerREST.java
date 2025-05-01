package ar.edu.unq.epersgeist.controller;


import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTORequest;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.EspirituDTOResponse;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import  ar. edu. unq. epersgeist. modelo. espiritu. Espiritu;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/espiritus")
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
    public Set<EspirituDTOResponse> getEspiritusDemoniacos(@RequestParam Sort.Direction direccion, @RequestParam int pagina, @RequestParam int cantidadPorPagina) {
        if (pagina < 0) throw new ResponseStatusException
                    (HttpStatus.BAD_REQUEST, "El número de página debe ser 0 o mayor.");

        if (cantidadPorPagina <= 0) throw new ResponseStatusException
                (HttpStatus.BAD_REQUEST, "La cantidad por página debe ser positiva y mayor a 0.");

        return espirituService.espiritusDemoniacos(direccion, pagina, cantidadPorPagina).stream()
                .map(EspirituDTOResponse::desdeModelo) // TODO mantenemos algún tipo de default value o no?
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspirituDTOResponse> getEspirituById(@PathVariable Long id) {
        Optional<Espiritu> optionalEspiritu = espirituService.recuperar(id);

        if (optionalEspiritu.isEmpty())
            throw new ResponseStatusException
                    (HttpStatus.NOT_FOUND, "Espíritu de id " + id + " no encontrado");

        return ResponseEntity.ok(EspirituDTOResponse.desdeModelo(optionalEspiritu.get()));
    }

    @PostMapping
    public ResponseEntity<EspirituDTOResponse> createEspiritu(@RequestBody EspirituDTORequest espirituDTO) {

        // TIPO
        if (!TipoEspiritu.esTipoValido(espirituDTO.tipo().name())) {
            throw new ResponseStatusException (HttpStatus.BAD_REQUEST, "Tipo erróneo: " + espirituDTO.tipo()); // TODO creo que no se puede hacer esto, porque directamente sale Bad Request por parte de Spring
        }

        // NIVEL DE CONEXIÓN

        if (espirituDTO.nivelDeConexion() > 100 || espirituDTO.nivelDeConexion() < 0) throw new ResponseStatusException
                (HttpStatus.BAD_REQUEST, "Valor inválido de nivel de conexión (debe estar entre 0 y 100 inclusive): " + espirituDTO.nivelDeConexion());

        // UBICACIÓN
        Optional<Ubicacion> ubicacionOptional = ubicacionService.recuperar(espirituDTO.ubicacionId());
        if (ubicacionOptional.isEmpty()) throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Ubicación no encontrada: " + espirituDTO.ubicacionId());

        // NOMBRE
        if (espirituService.existeConNombre(espirituDTO.nombre())) throw new ResponseStatusException
                (HttpStatus.CONFLICT, "Nombre ya existente: " + espirituDTO.nombre());

        Espiritu espirituModelo = espirituDTO.aModelo(ubicacionOptional.get());
        Espiritu espirituCreado = espirituService.crear(espirituModelo);
        return ResponseEntity
                .status(201)
                .body(EspirituDTOResponse.desdeModelo(espirituCreado));
    }
}