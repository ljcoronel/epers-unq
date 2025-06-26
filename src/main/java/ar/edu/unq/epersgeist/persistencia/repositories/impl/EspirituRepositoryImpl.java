package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.DraftEspirituJPADTO;
import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.DraftEspirituMONGODTO;
import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.SnapshotMongo;
import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.SnapshotSql;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.influxdb.UbicacionDAOINFLUX;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.EspirituDAOJPA;
import ar.edu.unq.epersgeist.persistencia.dao.mongodb.EspirituDAOMONGO;
import ar.edu.unq.epersgeist.persistencia.dao.mongodb.UbicacionDAOMONGO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.EspirituMONGODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.UbicacionMONGODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.espiritu.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class EspirituRepositoryImpl implements EspirituRepository {

    private final EspirituDAOJPA espirituDAOJPA;
    private final EspirituDAOMONGO espirituDAOMongo;
    private final UbicacionDAOMONGO ubicacionDAOMONGO;
    private final UbicacionDAOINFLUX ubicacionDAOINFLUX;

    public EspirituRepositoryImpl(EspirituDAOJPA espirituDAOJPA, EspirituDAOMONGO espirituDAOMongo, UbicacionDAOMONGO ubicacionDAOMONGO, UbicacionDAOINFLUX ubicacionDAOINFLUX) {
        this.espirituDAOMongo = espirituDAOMongo;
        this.espirituDAOJPA = espirituDAOJPA;
        this.ubicacionDAOMONGO = ubicacionDAOMONGO;
        this.ubicacionDAOINFLUX = ubicacionDAOINFLUX;
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        this.espirituDAOJPA.actualizar(espiritu.getNombre(), espiritu.getNivelDeConexion(), espiritu.getId());
    }

    @Override
    public Espiritu guardar(Espiritu espiritu) {
        EspirituJPADTO jpaDTO = EspirituJPADTO.desdeModelo(espiritu);
        Espiritu espirituConId = this.espirituDAOJPA.save(jpaDTO).aModelo(espiritu);

        mappearEspirituDominante(espiritu.getEspirituDominante());

        EspirituMONGODTO espirituMongo = EspirituMONGODTO.desdeModelo(espirituConId);
        this.espirituDAOMongo.save(espirituMongo);

        return espirituConId;
    }

    @Override
    public Optional<Espiritu> recuperar(Long id) {
        Optional<EspirituJPADTO> optionalEspirituJPADTO = this.espirituDAOJPA.findById(id);

        return espirituDAOJPA.findById(id).map(this::mappearEspiritu);
    }

    @Override
    public void eliminar(Long id) {
        espirituDAOJPA.deleteById(id);
        this.espirituDAOMongo.deleteById(id);
        // TODO devolver temperatura a la normalidad
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return this.espirituDAOJPA.findAll().stream().map(this::mappearEspiritu).toList();
    }

    @Override
    public boolean espirituConNombre(String nombre, Long id) {
        return this.espirituDAOJPA.espirituConNombre(nombre, id);
    }

    @Override
    public List<Demonio> espiritusDemoniacos(Sort.Direction direccion, int pagina, int cantidadPorPagina) {
       return this.espirituDAOJPA.findAll
                       (Example.of
                               (EspirituJPADTO.desdeModelo(new Demonio())),
                               PageRequest.of(pagina,cantidadPorPagina, direccion, "nivelDeConexion"))
                                .map(mappearDemonioAModelo()).toList();
    }

    private Function<EspirituJPADTO, Demonio> mappearDemonioAModelo() {
        return jpaDTO -> {
            Demonio espiritu = (Demonio) jpaDTO.aModelo();
            EspirituMONGODTO espirituMongo = this.espirituDAOMongo.findById(espiritu.getId()).get();

            mappearEspirituDominante(espiritu.getEspirituDominante());
            UbicacionMONGODTO ubicacionMONGODTO = ubicacionDAOMONGO.findById(espiritu.getUbicacion().getId()).get();

            mappearConInflux(espiritu);

            return (Demonio) espirituMongo.aModelo(espiritu, ubicacionMONGODTO);
        };
    }

    @Override
    public List<Espiritu> todosLosEspiritusDeMedium(Long id) {
        return this.espirituDAOJPA.todosLosEspiritusDeMedium(id).stream().map(this::mappearEspiritu).toList();
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return this.espirituDAOJPA.espiritusEn(ubicacionId).stream().map(this::mappearEspiritu).toList();
    }

    @Override
    public Long santuarioCorrupto() {
        return this.espirituDAOJPA.santuarioCorrupto();
    }

    @Override
    public int cantidadTotalDeDemonios(Long idSantuario) {
        return this.espirituDAOJPA.cantidadTotalDeDemonios(idSantuario);
    }

    @Override
    public int cantidadDeDemoniosLibresEn(Long idSantuario) {
        return this.espirituDAOJPA.cantidadDeDemoniosLibresEn(idSantuario);
    }

    @Override
    public void desvincular(Long id) {
        this.espirituDAOJPA.desvincular(id);
    }

    @Override
    public boolean referencia(Long id) {
        return this.espirituDAOJPA.referencia(id);
    }

    @Override
    public boolean estaEntreDistancia(Long espirituId, Point coordenadaReferencia, int minKms, int maxKms) {
        return espirituDAOMongo.estanEntreDistancia(espirituId, new GeoJsonPoint(coordenadaReferencia), minKms*1000, maxKms*1000).isPresent();
    }

    private Espiritu mappearEspiritu(EspirituJPADTO jpaDTO){

        Espiritu espiritu = jpaDTO.aModelo();
        EspirituMONGODTO espirituMongo = this.espirituDAOMongo.findById(espiritu.getId()).get();

        mappearEspirituDominante(espiritu.getEspirituDominante());
        UbicacionMONGODTO ubicacionMONGODTO = ubicacionDAOMONGO.findById(espiritu.getUbicacion().getId()).get();

        mappearConInflux(espiritu);

        return espirituMongo.aModelo(espiritu, ubicacionMONGODTO);
    }

    private void mappearConInflux(Espiritu espiritu) {
        // TODO debería hacer esto con DTOs?
        Ubicacion ubEspiritu = espiritu.getUbicacion();
        ubEspiritu.establecerTemperatura(ubicacionDAOINFLUX.getTemperatura(ubEspiritu.getId()));// añado temp de influx
        if (espiritu.getEspirituDominante() == null) return;

        Ubicacion ubEspirituDom = espiritu.getEspirituDominante().getUbicacion();
        ubEspirituDom.establecerTemperatura(ubicacionDAOINFLUX.getTemperatura(ubEspirituDom.getId()));// añado temp de influx
    }

    private void mappearEspirituDominante(Espiritu espirituDominante){
        if (espirituDominante == null) return;
        EspirituMONGODTO espirituMongo = this.espirituDAOMongo.findById(espirituDominante.getId()).get();

        mappearEspirituDominante(espirituDominante.getEspirituDominante()); // recursivo, dá coords a todos los espíritus de la cadena de dominancia
        UbicacionMONGODTO ubicacionMONGODTO = ubicacionDAOMONGO.findById(espirituDominante.getUbicacion().getId()).get();
        espirituMongo.aModelo(espirituDominante, ubicacionMONGODTO);
    }
    
    @Override
    public void crearSnapshot(Snapshot snapshot) {
        snapshot.guardarEstadoSql(this.espirituDAOJPA.findAll().stream()
                .map(dto -> (SnapshotSql) DraftEspirituJPADTO.guardar(dto))
                .toList());
        snapshot.guardarEstadoMongo(this.espirituDAOMongo.findAll().stream()
                .map(dto -> (SnapshotMongo) DraftEspirituMONGODTO.guardar(dto))
                .toList());
    }
}
