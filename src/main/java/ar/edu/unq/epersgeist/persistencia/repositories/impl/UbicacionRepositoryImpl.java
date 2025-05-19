package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.UbicacionDAOJPA;
import ar.edu.unq.epersgeist.persistencia.dao.neo4j.UbicacionDAONEO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.UbicacionNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.UbicacionJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class UbicacionRepositoryImpl implements UbicacionRepository {

    private final UbicacionDAOJPA ubicacionDaoJpa;
    private final UbicacionDAONEO ubicacionDaoNeo;

    public UbicacionRepositoryImpl(UbicacionDAOJPA ubicacionDaoJpa, UbicacionDAONEO ubicacionDaoNeo) {
        this.ubicacionDaoJpa = ubicacionDaoJpa;
        this.ubicacionDaoNeo = ubicacionDaoNeo;
    }

    @Override
    public boolean existeNombre(String nombre, Long unId) {
        return this.ubicacionDaoJpa.existeNombre(nombre, unId);
    }


    @Override
    public void actualizar(Ubicacion ubicacion) {

        this.ubicacionDaoJpa.actualizar(
                ubicacion.getId(),
                ubicacion.getNombre(),
                ubicacion.getEnergia());

        ubicacionDaoNeo.actualizar(
                ubicacion.getId(),
                ubicacion.getNombre(),
                ubicacion.getEnergia());
    }


    @Override
    public Ubicacion guardar(Ubicacion ubicacion) {
        UbicacionJPADTO ubicacionJpa = UbicacionJPADTO.desdeModelo(ubicacion);
        Ubicacion ubicacionConId = this.ubicacionDaoJpa.save(ubicacionJpa).aModelo(ubicacion);

        UbicacionNEODTO ubicacionNeo = UbicacionNEODTO.desdeModelo(ubicacionConId);
        this.ubicacionDaoNeo.save(ubicacionNeo);

        return ubicacionConId;
    }

    @Override
    public Optional<Ubicacion> recuperar(Long id) {
        Optional<UbicacionJPADTO> optionalUbicacionJpa = this.ubicacionDaoJpa.findById(id);
        UbicacionNEODTO ubicacionNEODTO = this.ubicacionDaoNeo.findById(id).get();

        return optionalUbicacionJpa.map(jpaDto -> jpaDto.aModelo(ubicacionNEODTO));
    }

    @Override
    public void eliminar(Long id) {
        ubicacionDaoNeo.deleteById(id);
        this.ubicacionDaoJpa.deleteById(id);
    }

    public boolean esReferenciado(Long id) {
        return this.ubicacionDaoJpa.esReferenciado(id);
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return List.copyOf(this.ubicacionDaoJpa.findAll().stream().map(UbicacionJPADTO::aModelo).toList());
    }
}

