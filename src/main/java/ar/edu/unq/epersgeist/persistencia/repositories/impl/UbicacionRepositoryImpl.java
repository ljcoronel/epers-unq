package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.controller.dto.ReporteDTO.*;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.ClosenessResult;
import ar.edu.unq.epersgeist.persistencia.DegreeResult;
import ar.edu.unq.epersgeist.persistencia.dao.influxdb.UbicacionDAOINFLUX;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.UbicacionDAOJPA;
import ar.edu.unq.epersgeist.persistencia.dao.mongodb.UbicacionDAOMONGO;
import ar.edu.unq.epersgeist.persistencia.dao.neo4j.UbicacionDAONEO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.influx.EventoPortalDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.Snapshot;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.mongodb.UbicacionMONGODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.neo4j.UbicacionNEODTO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.ubicacion.UbicacionJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import com.influxdb.client.domain.WritePrecision;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UbicacionRepositoryImpl implements UbicacionRepository {

    private final UbicacionDAOJPA ubicacionDaoJpa;
    private final UbicacionDAONEO ubicacionDaoNeo;
    private final UbicacionDAOMONGO ubicacionDaoMongo;
    private final UbicacionDAOINFLUX ubicacionDAOInflux;

    public UbicacionRepositoryImpl(UbicacionDAOJPA ubicacionDaoJpa, UbicacionDAONEO ubicacionDaoNeo, UbicacionDAOMONGO ubicacionDaoMongo, UbicacionDAOINFLUX ubicacionDAOINFLUX) {
        this.ubicacionDaoJpa = ubicacionDaoJpa;
        this.ubicacionDaoNeo = ubicacionDaoNeo;
        this.ubicacionDaoMongo = ubicacionDaoMongo;
        this.ubicacionDAOInflux = ubicacionDAOINFLUX;
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

        return mappearUbicacion(id);
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
                })
                .sorted(Comparator.comparing(Ubicacion::getId))
                .toList());
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

    public Optional<Ubicacion> encontrarUbicacionQueContiene(Point punto){
        GeoJsonPoint coordenada = new GeoJsonPoint(punto.getX(), punto.getY());

        return ubicacionDaoMongo.findByPuntoDentroDelTerritorio(coordenada)
                .map(mongoDTO -> {
                    UbicacionJPADTO jpaDTO = ubicacionDaoJpa.findById(mongoDTO.getId()).get();
                    Ubicacion ubicacion = jpaDTO.aModelo();
                    return mongoDTO.aModelo(ubicacion);
                });
    }

    @Override
    public boolean existeUbicacionEnArea(List<Point> territorio) {
        GeoJsonPolygon area = new GeoJsonPolygon(territorio);

        return !ubicacionDaoMongo.interseccionesConArea(area).isEmpty();
    }

    @Override
    public void crearSnapshot(Snapshot snapshot) {
        snapshot.guardarEstadoSql(this.ubicacionDaoJpa.findAll().stream()
                .map(dto -> (SnapshotSql) DraftUbicacionJPADTO.guardar(dto))
                .toList());
        snapshot.guardarEstadoMongo(this.ubicacionDaoMongo.findAll().stream()
                .map(dto -> (SnapshotMongo) DraftUbicacionMONGODTO.guardar(dto))
                .toList());
        snapshot.guardarEstadoNeo(this.ubicacionDaoNeo.findAll().stream()
                .map(dto -> (SnapshotNeo) DraftUbicacionNEODTO.guardar(dto, this.ubicacionDaoNeo.ubicacionesConectadas(dto.getId())))
                .toList());
    }

    @Override
    public Optional<Ubicacion> ubicacionMasFria(){
        Long ubicacionId = this.ubicacionDAOInflux.recuperarIdDeUbicacionMasPeligrosa();
        return mappearUbicacion(ubicacionId);
    }

    @Override
    public Optional<Ubicacion> ubicacionConMayorVariacionDeTemperaturas(){

        Optional<Ubicacion> optionalUbicacion = this.ubicacionDAOInflux.recuperarIdDeUbicacionMasConMasTransitoDeEspiritus()
                .map( opId -> mappearUbicacion(opId).get());

        return optionalUbicacion;
    }

    @Override
    public List<EventoPortalDTO> eventosDePortalesOcurridos() {
        List<EventoPortalDTO> eventosDePortales = this.ubicacionDAOInflux.recuperarEventosDePortales();

        return eventosDePortales.stream()
                .map(evento -> mappearUbicacion(evento.getUbicacionId())
                        .map(ubicacion -> new EventoPortalDTO(ubicacion, evento.getTiempo(), evento.getIntensidad())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public void cambiarTemperatura(Long id, double temperatura) {
        com.influxdb.client.write.Point influxPoint = com.influxdb.client.write.Point.measurement("temperaturas")
                .addTag("ubicacion", String.valueOf(id))
                .addField("temp", temperatura)
                .addField("tempAmbiente", 20d)
                .time(Instant.now().toEpochMilli(), WritePrecision.MS);

        ubicacionDAOInflux.guardarDatos(influxPoint);
    }

    @Override
    public double getTemperatura(Long id) {
        return this.ubicacionDAOInflux.getTemperatura(id);
    }

    @Override
    public boolean tieneDeltaDeTemperaturaMenorOIgual(Long id, double deltaTemp) {
        return this.ubicacionDAOInflux.tieneDeltaDeTemperaturaMenorOIgual(id, -5);
    }

    private Optional<Ubicacion> mappearUbicacion(Long id){
        return ubicacionDaoJpa.findById(id).map(ubicacionJPADTO -> {
            UbicacionMONGODTO ubicacionMongo = ubicacionDaoMongo.findById(id).get();
            return ubicacionMongo.aModelo((ubicacionJPADTO.aModelo()));
        });
    }
}
