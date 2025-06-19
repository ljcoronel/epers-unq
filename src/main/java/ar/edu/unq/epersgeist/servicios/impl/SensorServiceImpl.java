package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Lectura.Lectura;
import ar.edu.unq.epersgeist.modelo.Lectura.LecturaDeTemperatura;
import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.DAO.influx.LecturaDAOInflux;
import ar.edu.unq.epersgeist.servicios.interfaces.SensorService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class SensorServiceImpl implements SensorService {

    private LecturaDAOInflux lecturaDAOInflux;
    private UbicacionService ubicacionService;

    public SensorServiceImpl(LecturaDAOInflux lecturaDAOInflux, UbicacionService ubicacionService) {
        this.lecturaDAOInflux = lecturaDAOInflux;
        this.ubicacionService = ubicacionService;
    }

    @Override
    public void guardarLecturaDeTemperatura(LecturaDeTemperatura lectura) {
        Long id = Long.parseLong(lectura.getUbicacionId());

        Optional<Ubicacion> ubicacionLeida =  ubicacionService.recuperar(id);

        if (ubicacionLeida.isEmpty())
            throw new EntidadInexistenteException("El id ingresado no corresponde a una ubicacion");

        lecturaDAOInflux.guardarLectura(lectura);
    }

    @Override
    public List<LecturaDeTemperatura> recuperarLecturas() {
        return lecturaDAOInflux.recuperarLecturas();
    }
}
