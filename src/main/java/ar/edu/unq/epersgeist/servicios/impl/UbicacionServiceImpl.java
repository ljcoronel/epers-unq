package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionConMismoNombreException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.persistencia.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.UbicacionDAO;
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

    public UbicacionServiceImpl(UbicacionDAO ubicacionDAO, EspirituDAO espirituDAO, MediumDAO mediumDAO) {
        this.ubicacionDAO = ubicacionDAO;
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
    }

    @Override
    public Ubicacion crear(Ubicacion ubicacion) {
        if (this.ubicacionDAO.existeNombre(ubicacion.getNombre())) throw new UbicacionConMismoNombreException();
        return this.ubicacionDAO.save(ubicacion);
    }

    @Override
    public Optional<Ubicacion> recuperar(Long id) {
        if (this.ubicacionDAO.findById(id).isEmpty()) throw new UbicacionNoEncontradaException();
        return this.ubicacionDAO.findById(id);
    }

    @Override
    public void actualizar(Ubicacion ubicacion) {
        if (this.ubicacionDAO.findById(ubicacion.getId()).isEmpty()) throw new UbicacionNoEncontradaException();
        if (this.ubicacionDAO.existeNombre(ubicacion.getNombre())) throw new UbicacionConMismoNombreException();
        this.ubicacionDAO.save(ubicacion);
    }

    @Override
    public void eliminar(Long id) {
        if (this.ubicacionDAO.findById(id).isEmpty()) throw new UbicacionNoEncontradaException();
        this.ubicacionDAO.deleteById(id);
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return List.copyOf(this.ubicacionDAO.findAll());
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        if (this.ubicacionDAO.findById(ubicacionId).isEmpty()) throw new UbicacionNoEncontradaException();
        return this.espirituDAO.espiritusEn(ubicacionId);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        if (this.ubicacionDAO.findById(ubicacionId).isEmpty()) throw new UbicacionNoEncontradaException();
        return this.mediumDAO.mediumsSinEspiritusEn(ubicacionId);
    }
}
