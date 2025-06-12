package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.DraftMediumJPADTO;
import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.DraftMediumMONGODTO;
import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.SnapshotMongo;
import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.SnapshotSql;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.MediumDAOJPA;
import ar.edu.unq.epersgeist.persistencia.dao.mongodb.MediumDAOMONGO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.MediumMONGODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MediumRepositoryImpl implements MediumRepository {

    private final MediumDAOJPA mediumDAOJPA;
    private final MediumDAOMONGO mediumDAOMONGO;

    public MediumRepositoryImpl(MediumDAOJPA mediumDAOJPA, MediumDAOMONGO mediumDAOMONGO) {
        this.mediumDAOJPA = mediumDAOJPA;
        this.mediumDAOMONGO = mediumDAOMONGO;
    }

    @Override
    public Medium guardar(Medium medium) {
        MediumJPADTO mediumJPA = MediumJPADTO.desdeModelo(medium);
        Medium mediumConId = this.mediumDAOJPA.save(mediumJPA).aModelo(medium);
        this.mediumDAOMONGO.save(MediumMONGODTO.desdeModelo(mediumConId));
        return mediumConId;
    }

    @Override
    public Optional<Medium> recuperar(Long id) {
        Optional<MediumJPADTO> optionalMediumJPADTO = this.mediumDAOJPA.findById(id);
        Optional<MediumMONGODTO> mediumMONGODTO = this.mediumDAOMONGO.findById(id.toString());
        if (mediumMONGODTO.isEmpty()) return Optional.empty();
        Point coordenada = mediumMONGODTO.get().getCoordenada();
        return optionalMediumJPADTO.map(mediumdto -> mediumdto.aModelo(coordenada));
    }

    @Override
    public List<Medium> recuperarTodos() {
        return this.mediumDAOJPA.findAll().stream()
                .map(mediumdto -> {
                    Point coordenada = this.mediumDAOMONGO.findById(mediumdto.getId().toString()).get().getCoordenada();
                    return mediumdto.aModelo(coordenada);
                })
                .toList();
    }

    @Override
    public void actualizar(Medium medium) {
        this.mediumDAOJPA.actualizar(medium.getNombre(), medium.getManaMax(),medium.getMana(), medium.getId());
    }

    @Override
    public void eliminar(Long id) {
        this.mediumDAOJPA.deleteById(id);
        this.mediumDAOMONGO.deleteById(id.toString());
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return this.mediumDAOJPA.mediumsSinEspiritusEn(ubicacionId).stream()
                .map(mediumdto -> {
                    Point coordenada = this.mediumDAOMONGO.findById(mediumdto.getId().toString()).get().getCoordenada();
                    return mediumdto.aModelo(coordenada);
                })
                .toList();
    }

    @Override
    public Long mediumConMasDemonios(Long idSantuario) {
        return this.mediumDAOJPA.mediumConMasDemonios(idSantuario);
    }

    @Override
    public void crearSnapshot(Snapshot snapshot) {
        snapshot.guardarEstadoSql(this.mediumDAOJPA.findAll().stream()
                .map(dto -> (SnapshotSql) DraftMediumJPADTO.guardar(dto))
                .toList());
        snapshot.guardarEstadoMongo(this.mediumDAOMONGO.findAll().stream()
                .map(dto -> (SnapshotMongo) DraftMediumMONGODTO.guardar(dto))
                .toList());
    }
}
