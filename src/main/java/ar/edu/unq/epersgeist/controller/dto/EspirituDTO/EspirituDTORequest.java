package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


public record EspirituDTORequest(Long id, Class<? extends Espiritu> tipo, String nombre, int nivelDeConexion, boolean estaLibre, Long mediumId, UbicacionDTO ubicacionDTO) {
    public static EspirituDTORequest desdeModelo(Espiritu espiritu) {
        return new EspirituDTORequest(
                espiritu.getId(),
                espiritu.getClass(),
                espiritu.getNombre(),
                espiritu.getNivelDeConexion(),
                espiritu.estaLibre(), // no sé si ponerlo o no.
                UbicacionDTO.desdeModelo(espiritu.getUbicacion().getId(), espiritu.getUbicacion().getNombre()) // usaría el nombre para mayor descripción, pero pasa que sino es imposible obtener una ubicación con sólo esa info, haciendo a este dato menos útil
                // tal vez se pueda hacer un objeto más chico con menos info de ubicación...? (nombre e id)
        );
    }

    public Espiritu aModelo() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        return this.tipo.getConstructor(Integer.class, String.class, Ubicacion.class)
                .newInstance(this.nivelDeConexion, this.nombre, this.ubicacionDTO.aModelo());

        // con esto luego se hace un MediumService.recuperar(espiritu.getMediumId()) y se le agrega con el método de abajo
    }

//    public Espiritu aModelo(Medium medium) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
//        Espiritu espiritu = this.aModelo();
//        espiritu.setMedium(medium);
//
//        return espiritu;
//    }
}
