package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.exception.EntidadTodaviaReferenciadaException;
import ar.edu.unq.epersgeist.modelo.exception.NombreRepetidoException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.UbicacionDAOJpa;
import ar.edu.unq.epersgeist.persistencia.dao.neo4j.UbicacionDAONeo4j;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.UbicacionNeo4jDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.UbicacionSQLDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// TODO va transactional?
public class UbicacionRepositoryImpl implements UbicacionRepository {

    private final UbicacionDAOJpa ubicacionDAOJpa;
    private final UbicacionDAONeo4j ubicacionDAONeo4j;

    public UbicacionRepositoryImpl(UbicacionDAOJpa ubicacionDAOJpa, UbicacionDAONeo4j ubicacionDAONeo4j) {
        this.ubicacionDAOJpa = ubicacionDAOJpa;
        this.ubicacionDAONeo4j = ubicacionDAONeo4j;
    }

    @Override
    public boolean existeNombre(String nombre, Long unId) {
        return this.ubicacionDAOJpa.existeNombre(nombre, unId);
    }


    @Override
    public void actualizar(Ubicacion ubicacion) {
        if (this.ubicacionDAOJpa.existeNombre(ubicacion.getNombre(), ubicacion.getId())) throw new NombreRepetidoException("No se puede crear una ubicación con nombre repetido.");
        this.ubicacionDAOJpa.actualizar(
                ubicacion.getId(),
                ubicacion.getNombre(),
                ubicacion.getEnergia());
    }


    @Override
    public Ubicacion guardar(Ubicacion ubicacion) {
        UbicacionNeo4jDTO neo4jDTO = UbicacionNeo4jDTO.desdeModelo(ubicacion);
        UbicacionSQLDTO sqldto = UbicacionSQLDTO.desdeModelo(ubicacion);
        if (this.ubicacionDAOJpa.existeNombre(ubicacion.getNombre(), ubicacion.getId())) throw new NombreRepetidoException("No se puede crear una ubicación con nombre repetido.");

        ubicacionDAONeo4j.save(neo4jDTO); // neo4j
        return this.ubicacionDAOJpa.save(sqldto).aModelo(ubicacion); // sql
    }

    @Override
    public Optional<Ubicacion> recuperar(Long id) {
        Optional<UbicacionSQLDTO> optionalUbicacionSQLDTO = this.ubicacionDAOJpa.findById(id);

        if (optionalUbicacionSQLDTO.isEmpty()) return Optional.empty();
        return Optional.ofNullable(optionalUbicacionSQLDTO.get().aModelo());
    }

    @Override
    public void eliminar(Long id) {
        if (this.ubicacionDAOJpa.esReferenciado(id)) throw new EntidadTodaviaReferenciadaException("La ubicación de id: " + id + " todavía es referenciada.");
        // TODO los throws van en repository o en service?
        this.ubicacionDAOJpa.deleteById(id);
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return List.copyOf(this.ubicacionDAOJpa.findAll().stream().map(UbicacionSQLDTO::aModelo).toList());
    }
}
