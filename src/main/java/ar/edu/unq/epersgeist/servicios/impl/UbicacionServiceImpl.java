package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.*;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.ClosenessResult;
import ar.edu.unq.epersgeist.persistencia.DegreeResult;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
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
    private final EspirituRepository espirituRepository;
    private final MediumRepository mediumRepository;

    public UbicacionServiceImpl(UbicacionRepository ubicacionRepository, EspirituRepository espirituRepository, MediumRepository mediumRepository) {
        this.ubicacionRepository = ubicacionRepository;
        this.espirituRepository = espirituRepository;
        this.mediumRepository = mediumRepository;
    }

    @Override
    public Ubicacion crear(Ubicacion ubicacion) {
        if (this.ubicacionRepository.existeNombre(ubicacion.getNombre(), ubicacion.getId())) throw new NombreRepetidoException("No se puede crear una ubicación con nombre repetido.");
        return this.ubicacionRepository.guardar(ubicacion);
    }

    @Override
    public Optional<Ubicacion> recuperar(Long id) {
        return this.ubicacionRepository.recuperar(id);
    }

    @Override
    public void actualizar(Ubicacion ubicacion) {
        if (this.ubicacionRepository.existeNombre(ubicacion.getNombre(), ubicacion.getId())) throw new NombreRepetidoException("No se puede crear una ubicación con nombre repetido.");
        this.ubicacionRepository.actualizar(ubicacion);
    }

    @Override
    public void eliminar(Long id) {
        if (ubicacionRepository.esReferenciado(id)) throw new EntidadTodaviaReferenciadaException("La ubicación de id: " + id + " todavía es referenciada.");
        this.ubicacionRepository.eliminar(id);
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return this.ubicacionRepository.recuperarTodos();
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return this.espirituRepository.espiritusEn(ubicacionId);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return this.mediumRepository.mediumsSinEspiritusEn(ubicacionId);
    }

    @Override
    public void conectar(Long idOrigen, Long idDestino) {
        Optional<Ubicacion> optionalUbicacionOrigen = this.ubicacionRepository.recuperar(idOrigen);
        Optional<Ubicacion> optionalUbicacionDestino = this.ubicacionRepository.recuperar(idDestino);

        if(optionalUbicacionOrigen.isEmpty()) return;
        if(optionalUbicacionDestino.isEmpty()) throw new EntidadInexistenteException("La ubicación destino no existe.");;

        ubicacionRepository.conectar(idOrigen, idDestino);

    }

    public List<Ubicacion> caminoMasCorto(Long idOrigen, Long idDestino){
        Optional<Ubicacion> optionalUbicacionOrigen = this.ubicacionRepository.recuperar(idOrigen);

        if(optionalUbicacionOrigen.isEmpty()) return null;


        this.verificarSiLasUbicacionesEstanConectadas(idOrigen, idDestino);

        List<Ubicacion> camino = ubicacionRepository.caminoMasCorto(idOrigen, idDestino);

        return camino;
    }

    @Override
    public List<Ubicacion> ubicacionesSobrecargadas(int umbralDeEnergia) {
        return ubicacionRepository.ubicacionesSobrecargadas(umbralDeEnergia);
    }

    @Override
    public Boolean estanConectadas(Long idOrigen, Long idDestino) {
        return Boolean.valueOf(ubicacionRepository.estanConectadas(idOrigen, idDestino));
    }

    private Boolean estanConectadasDeAlgunModo(Long idOrigen, Long idDestino) {
        return Boolean.valueOf(ubicacionRepository.estanConectadasDeAlgunModo(idOrigen, idDestino));
    }

    private void verificarSiLasUbicacionesEstanConectadas(Long idOrigen, Long idDestino){

        Optional<Ubicacion> optionalUbicacionDestino = this.ubicacionRepository.recuperar(idDestino);

        if (optionalUbicacionDestino.isEmpty()){
            throw new EntidadInexistenteException("La ubicación destino no existe.");
        }

        if (!this.estanConectadasDeAlgunModo(idOrigen,idDestino)){
            throw new UbicacionesNoConectadasException();
        }
    }

    @Override
    public List<ClosenessResult> closenessOf(List<Long> ids) {
        return this.ubicacionRepository.closenessOf(ids);
    }

    @Override
    public List<DegreeResult> incomingDegreeOf(List<Long> ids) {
        return this.ubicacionRepository.incomingDegreeOf(ids);
    }

    @Override
    public List<DegreeResult> outgoingDegreeOf(List<Long> ids) {
        return this.ubicacionRepository.outgoingDegreeOf(ids);
    }
}
