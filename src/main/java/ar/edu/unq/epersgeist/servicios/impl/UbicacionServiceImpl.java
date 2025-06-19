package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.EntidadTodaviaReferenciadaException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.NombreRepetidoException;
import ar.edu.unq.epersgeist.persistencia.DAO.jpa.EspirituDAOJPA;
import ar.edu.unq.epersgeist.persistencia.DAO.jpa.MediumDAOJPA;
import ar.edu.unq.epersgeist.persistencia.DAO.jpa.UbicacionDAOJPA;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionRepository ubicacionRepository;
    private final EspirituDAOJPA espirituDAOJPA;
    private final MediumDAOJPA mediumDAOJPA;

    public UbicacionServiceImpl(UbicacionRepository ubicacionRepository, EspirituDAOJPA espirituDAOJPA, MediumDAOJPA mediumDAOJPA) {
        this.ubicacionRepository = ubicacionRepository;
        this.espirituDAOJPA = espirituDAOJPA;
        this.mediumDAOJPA = mediumDAOJPA;
    }

    @Override
    public Ubicacion crear(Ubicacion ubicacion) {
        if (this.ubicacionRepository.existeNombre(ubicacion.getNombre(), ubicacion.getId())) throw new NombreRepetidoException("No se puede crear una ubicación con nombre repetido.");
        return this.ubicacionRepository.save(ubicacion);
    }

    @Override
    public Optional<Ubicacion> recuperar(Long id) {
        return this.ubicacionRepository.findById(id);
    }

    @Override
    public void actualizar(Ubicacion ubicacion) {
        if (this.ubicacionRepository.existeNombre(ubicacion.getNombre(), ubicacion.getId())) throw new NombreRepetidoException("No se puede crear una ubicación con nombre repetido.");
        this.ubicacionRepository.actualizar(
                ubicacion.getId(),
                ubicacion.getNombre(),
                ubicacion.getEnergia());
    }

    @Override
    public void eliminar(Long id) {
        if (this.ubicacionRepository.esReferenciado(id)) throw new EntidadTodaviaReferenciadaException("La ubicación de id: " + id + " todavía es referenciada.");
        this.ubicacionRepository.deleteById(id);
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return List.copyOf(this.ubicacionRepository.findAll());
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return this.espirituDAOJPA.espiritusEn(ubicacionId);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return this.mediumDAOJPA.mediumsSinEspiritusEn(ubicacionId);
    }

    @Override
    public void guardarLecturaDeTemperatura(String ubicacionId, double temperatura, String timeStamp) {
        ubicacionRepository.guardarLecturaDeTemperatura(ubicacionId, temperatura, timeStamp);
    }

    @Override
    public List<FluxRecord> recuperarLecturas() {
        return ubicacionRepository.recuperarLecturas();
    }

}
