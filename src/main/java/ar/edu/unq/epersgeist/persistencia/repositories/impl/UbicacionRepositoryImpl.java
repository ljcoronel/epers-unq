package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.ClosenessResult;
import ar.edu.unq.epersgeist.persistencia.DegreeResult;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.UbicacionDAOJPA;
import ar.edu.unq.epersgeist.persistencia.dao.mongo.UbicacionDAOMongo;
import ar.edu.unq.epersgeist.persistencia.dao.neo4j.UbicacionDAONEO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongo.UbicacionMONGODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.UbicacionNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.UbicacionJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UbicacionRepositoryImpl implements UbicacionRepository {

    private final UbicacionDAOJPA ubicacionDaoJpa;
    private final UbicacionDAONEO ubicacionDaoNeo;
    private final UbicacionDAOMongo ubicacionDaoMongo;
    private final UbicacionDAOMongo ubicacionDAOMongo;

    public UbicacionRepositoryImpl(UbicacionDAOJPA ubicacionDaoJpa, UbicacionDAONEO ubicacionDaoNeo, UbicacionDAOMongo ubicacionDaoMongo, UbicacionDAOMongo ubicacionDAOMongo) {
        this.ubicacionDaoJpa = ubicacionDaoJpa;
        this.ubicacionDaoNeo = ubicacionDaoNeo;
        this.ubicacionDaoMongo = ubicacionDaoMongo;
        this.ubicacionDAOMongo = ubicacionDAOMongo;
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

        this.ubicacionDaoNeo.actualizar(
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

        UbicacionMONGODTO ubicacionMongo = UbicacionMONGODTO.desdeModelo(ubicacion);
        this.ubicacionDaoMongo.save(ubicacionMongo);

        return ubicacionConId;
    }

    @Override
    public Optional<Ubicacion> recuperar(Long id) {

        return ubicacionDaoJpa.findById(id).map(ubicacionJPADTO -> {
                UbicacionMONGODTO ubicacionMongo = ubicacionDaoMongo.findById(id).get();
                return ubicacionMongo.aModelo((ubicacionJPADTO.aModelo()));
        });
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
    public boolean estanConectadas(Long idOrigen, Long idDestino) {
        return ubicacionDaoNeo.estanConectadas(idOrigen, idDestino);
    }

    @Override
    public void conectar(Long idOrigen, Long idDestino) {
        ubicacionDaoNeo.conectar(idOrigen, idDestino);
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return List.copyOf(this.ubicacionDaoJpa.findAll().stream()
                .map(ubicacionJPA -> {
                    UbicacionMONGODTO ubicacionMongo = ubicacionDaoMongo.findById(ubicacionJPA.getId()).get();
                    return ubicacionMongo.aModelo(ubicacionJPA.aModelo());
                }).toList());
    }

    public List<Ubicacion> caminoMasCorto(Long idOrigen, Long idDestino){
        List<UbicacionNEODTO> caminoDTO = ubicacionDaoNeo.caminoMasCorto(idOrigen, idDestino);

        return caminoDTO.stream()
                .map(UbicacionNEODTO::aModelo)
                .toList();

    }

    public boolean estanConectadasDeAlgunModo(Long idOrigen, Long idDestino) {
        return ubicacionDaoNeo.estanConectadasDeAlgunModo(idOrigen, idDestino);
    }

    @Override
    public List<Ubicacion> ubicacionesSobrecargadas(int umbralDeEnergia) {
        return ubicacionDaoNeo.ubicacionesConEnergiaSueriorAUmbral(umbralDeEnergia)
                .stream()
                .map(UbicacionNEODTO::aModelo).collect(Collectors.toList());
    }

    @Override
    public List<ClosenessResult> closenessOf(List<Long> ids) {
        return this.ubicacionDaoNeo.closenessOf(ids).stream()
                .map(ClosenessResult::registrar)
                .collect(Collectors.toList());
    }

    @Override
    public List<DegreeResult> outgoingDegreeOf(List<Long> ids) {
        return this.ubicacionDaoNeo.outgoingDegreeOf(ids).stream()
                .map(DegreeResult::registrar)
                .collect(Collectors.toList());
    }

    @Override
    public List<DegreeResult> incomingDegreeOf(List<Long> ids) {
        return this.ubicacionDaoNeo.incomingDegreeOf(ids).stream()
                .map(DegreeResult::registrar)
                .collect(Collectors.toList());
    }

}
