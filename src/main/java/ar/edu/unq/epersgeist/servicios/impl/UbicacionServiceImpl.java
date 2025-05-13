package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.EntidadTodaviaReferenciadaException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.NombreRepetidoException;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.neo4j.UbicacionDAONeo4j;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.UbicacionNeo4jDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionRepository ubicacionRepository;
    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;

    public UbicacionServiceImpl(UbicacionRepository ubicacionRepository, EspirituDAO espirituDAO, MediumDAO mediumDAO) {
        this.ubicacionRepository = ubicacionRepository;
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
    }

    @Override
    public Ubicacion crear(Ubicacion ubicacion) {
        if (this.ubicacionRepository.existeNombre(ubicacion.getNombre(), ubicacion.getId())) throw new NombreRepetidoException("No se puede crear una ubicación con nombre repetido.");
        // TODO los throws van en repository o en service? sino haríamos "crear" en repository con la función de arriba
        return this.ubicacionRepository.guardar(ubicacion);
    }

    @Override
    public Optional<Ubicacion> recuperar(Long id) {
        return this.ubicacionRepository.recuperar(id);
    }

    @Override
    public void actualizar(Ubicacion ubicacion) {
        this.ubicacionRepository.actualizar(ubicacion);
    }

    @Override
    public void eliminar(Long id) {

        this.ubicacionRepository.eliminar(id);
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return this.ubicacionRepository.recuperarTodos();
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return this.espirituDAO.espiritusEn(ubicacionId);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return this.mediumDAO.mediumsSinEspiritusEn(ubicacionId);
    }
}
