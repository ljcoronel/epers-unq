package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.EntidadTodaviaReferenciadaException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.NombreRepetidoException;
import ar.edu.unq.epersgeist.persistencia.DAO.Jpa.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.DAO.Jpa.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.DAO.Jpa.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.DAO.Neo4j.UbicacionDAONeo4j;
import ar.edu.unq.epersgeist.persistencia.repositories.DTO.UbicacionRepositoryDTO;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionDAO ubicacionDAO;
    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;
    private final UbicacionDAONeo4j ubicacionDAONeo4J;

    public UbicacionServiceImpl(UbicacionDAO ubicacionDAO, EspirituDAO espirituDAO, MediumDAO mediumDAO, UbicacionDAONeo4j ubicacionDAONeo4J) {
        this.ubicacionDAO = ubicacionDAO;
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
        this.ubicacionDAONeo4J = ubicacionDAONeo4J;
    }

    @Override
    public Ubicacion crear(Ubicacion ubicacion) {
        UbicacionRepositoryDTO dto = UbicacionRepositoryDTO.desdeModelo(ubicacion);
        if (this.ubicacionDAO.existeNombre(ubicacion.getNombre(), ubicacion.getId())) throw new NombreRepetidoException("No se puede crear una ubicación con nombre repetido.");
        this.ubicacionDAONeo4J.save(dto); //Neo 4j, idealmente iría en repository
        return this.ubicacionDAO.save(ubicacion); // sql
    }

    @Override
    public Optional<Ubicacion> recuperar(Long id) {
        return this.ubicacionDAO.findById(id);
    }

    @Override
    public void actualizar(Ubicacion ubicacion) {
        if (this.ubicacionDAO.existeNombre(ubicacion.getNombre(), ubicacion.getId())) throw new NombreRepetidoException("No se puede crear una ubicación con nombre repetido.");
        this.ubicacionDAO.actualizar(
                ubicacion.getId(),
                ubicacion.getNombre(),
                ubicacion.getEnergia());
    }

    @Override
    public void eliminar(Long id) {
        if (this.ubicacionDAO.esReferenciado(id)) throw new EntidadTodaviaReferenciadaException("La ubicación de id: " + id + " todavía es referenciada.");
        this.ubicacionDAO.deleteById(id);
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return List.copyOf(this.ubicacionDAO.findAll());
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
