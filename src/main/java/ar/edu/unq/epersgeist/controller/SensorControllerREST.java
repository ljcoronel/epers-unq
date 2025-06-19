package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.modelo.Lectura.LecturaDeTemperatura;
import ar.edu.unq.epersgeist.controller.dto.LecturaDeSensorDTO;
import ar.edu.unq.epersgeist.servicios.interfaces.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensor")
public class SensorControllerREST {

    private final SensorService sensorService;

    public SensorControllerREST(SensorService sensorService){
        this.sensorService = sensorService;
    }

    @PostMapping
    public ResponseEntity<String> guardarLectura(@RequestBody LecturaDeSensorDTO sensor){

        LecturaDeTemperatura lectura =  sensor.aModelo();

        sensorService.guardarLecturaDeTemperatura(lectura);

        return ResponseEntity.ok("Lectura guardada con exito");
    }

    @GetMapping
    public ResponseEntity<List<LecturaDeSensorDTO>> listarLecturas(){
        return ResponseEntity.ok(
                sensorService.recuperarLecturas()
                        .stream()
                        .map(LecturaDeSensorDTO::desdeModelo).toList()
        );
    }
}
