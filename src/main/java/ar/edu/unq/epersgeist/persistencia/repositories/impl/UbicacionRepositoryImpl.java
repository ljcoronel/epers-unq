package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.DAO.influx.LecturaDAOInflux;
import ar.edu.unq.epersgeist.persistencia.DAO.jpa.UbicacionDAOJPA;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class UbicacionRepositoryImpl implements UbicacionRepository {

    private UbicacionDAOJPA ubicacionDaoJpa;
    private LecturaDAOInflux lecturaDaoInflux;

    public UbicacionRepositoryImpl(UbicacionDAOJPA ubicacionDAOJPA, LecturaDAOInflux lecturaDAOInflux) {
        this.ubicacionDaoJpa = ubicacionDAOJPA;
        this.lecturaDaoInflux = lecturaDAOInflux;
    }

    @Override
    public boolean existeNombre(String nombre, Long id) {
        return ubicacionDaoJpa.existeNombre(nombre, id);
    }

    @Override
    public Ubicacion save(Ubicacion ubicacion) {
        return ubicacionDaoJpa.save(ubicacion);
    }

    @Override
    public Optional<Ubicacion> findById(Long id) {
        return ubicacionDaoJpa.findById(id);
    }

    @Override
    public void actualizar(Long id, String nombre, int energia) {
        ubicacionDaoJpa.actualizar(id, nombre, energia);
    }

    @Override
    public boolean esReferenciado(Long id) {
        return ubicacionDaoJpa.esReferenciado(id);
    }

    @Override
    public void deleteById(Long id) {
        ubicacionDaoJpa.deleteById(id);
    }

    @Override
    public List<Ubicacion> findAll() {
        return ubicacionDaoJpa.findAll();
    }

    @Override
    public void guardarLecturaDeTemperatura(String ubicacionId, double temperatura, String timeStamp) {
        lecturaDaoInflux.guardarLectura(ubicacionId,temperatura,timeStamp);
    }

    @Override
    public List<FluxRecord> recuperarLecturas() {
        return lecturaDaoInflux.recuperarLecturas();
    }

}
