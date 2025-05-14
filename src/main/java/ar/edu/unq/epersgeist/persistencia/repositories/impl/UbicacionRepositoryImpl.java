package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.UbicacionDAOJPA;
import ar.edu.unq.epersgeist.persistencia.dao.neo4j.UbicacionDAONEO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.UbicacionNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.UbicacionJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    }


    @Override
    public Ubicacion guardar(Ubicacion ubicacion) {
        UbicacionNEODTO ubicacionNeo = UbicacionNEODTO.desdeModelo(ubicacion);
        UbicacionJPADTO ubicacionJpa = UbicacionJPADTO.desdeModelo(ubicacion);
        //this.ubicacionDaoNeo.save(ubicacionNeo);
        return this.ubicacionDaoJpa.save(ubicacionJpa).aModelo(ubicacion);
    }

    @Override
    public Optional<Ubicacion> recuperar(Long id) {
        Optional<UbicacionJPADTO> optionalUbicacionJpa = this.ubicacionDaoJpa.findById(id);

        return optionalUbicacionJpa.map(UbicacionJPADTO::aModelo);
    }

    @Override
    public void eliminar(Long id) {
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
