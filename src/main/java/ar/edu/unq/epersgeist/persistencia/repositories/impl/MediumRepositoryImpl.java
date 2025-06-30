package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.DraftMediumJPADTO;
import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.DraftMediumMONGODTO;
import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.SnapshotMongo;
import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.SnapshotSql;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.influxdb.UbicacionDAOINFLUX;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.MediumDAOJPA;
import ar.edu.unq.epersgeist.persistencia.dao.mongodb.MediumDAOMONGO;
import ar.edu.unq.epersgeist.persistencia.dao.mongodb.UbicacionDAOMONGO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.EspirituMONGODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.MediumMONGODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.UbicacionMONGODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MediumRepositoryImpl implements MediumRepository {

    private final MediumDAOJPA mediumDAOJPA;
    private final MediumDAOMONGO mediumDAOMONGO;
    private final UbicacionDAOMONGO ubicacionDAOMONGO;
    private final UbicacionDAOINFLUX ubicacionDAOINFLUX;

    public MediumRepositoryImpl(MediumDAOJPA mediumDAOJPA, MediumDAOMONGO mediumDAOMONGO, UbicacionDAOMONGO ubicacionDAOMONGO, UbicacionDAOINFLUX ubicacionDAOINFLUX) {
        this.mediumDAOJPA = mediumDAOJPA;
        this.mediumDAOMONGO = mediumDAOMONGO;
        this.ubicacionDAOMONGO = ubicacionDAOMONGO;
        this.ubicacionDAOINFLUX = ubicacionDAOINFLUX;
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

        return optionalMediumJPADTO.map(this::mappearMedium);
    }

    private Medium mappearMedium(MediumJPADTO jpaDTO){

        Medium medium = jpaDTO.aModelo();
        MediumMONGODTO mediumMONGODTO = this.mediumDAOMONGO.findById(String.valueOf(medium.getId())).get();

        UbicacionMONGODTO ubicacionMONGODTO = ubicacionDAOMONGO.findById(medium.getUbicacion().getId()).get();

        mappearConInflux(medium);

        return mediumMONGODTO.aModelo(medium, ubicacionMONGODTO);
    }

    private void mappearConInflux(Medium medium) {
        Ubicacion ubMedium = medium.getUbicacion();
        double temperatura = ubicacionDAOINFLUX.getTemperatura(ubMedium.getId());

        ubMedium.establecerTemperatura(temperatura); // añado temp de influx
        medium.getDemonios().forEach(dem -> dem.getUbicacion().establecerTemperatura(temperatura));
        medium.getAngeles().forEach(ang -> ang.getUbicacion().establecerTemperatura(temperatura));
    }

    @Override
    public List<Medium> recuperarTodos() {
        return this.mediumDAOJPA.findAll().stream()
                .map(this::mappearMedium)
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
                .map(this::mappearMedium)
                .toList();
    }

    @Override
    public Long mediumConMasDemonios(Long idSantuario) {
        return this.mediumDAOJPA.mediumConMasDemonios(idSantuario);
    }

    @Override
    public boolean estaEntreDistancia(Long id, Point coordenadaReferencia, int minKms, int maxKms) {
        return mediumDAOMONGO.estanEntreDistancia(id, new GeoJsonPoint(coordenadaReferencia), minKms*1000, maxKms*1000).isPresent();
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
