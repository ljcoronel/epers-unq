package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exception.NombreRepetidoException;
import ar.edu.unq.epersgeist.persistencia.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.dao.DataIntegrityViolationException;
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
        if (this.ubicacionDAO.existeNombre(ubicacion.getNombre(), ubicacion.getId())) throw new NombreRepetidoException("No se puede crear una ubicación con nombre repetido.");
        return this.ubicacionDAO.save(ubicacion);
    }

    @Override
    public Optional<Ubicacion> recuperar(Long id) {
        return this.ubicacionDAO.findById(id);
    }

    @Override
    public void actualizar(Ubicacion ubicacion) {
        if (this.ubicacionDAO.existeNombre(ubicacion.getNombre(), ubicacion.getId())) throw new NombreRepetidoException("No se puede crear una ubicación con nombre repetido.");
        this.ubicacionDAO.actualizar(ubicacion.getId(), ubicacion.getNombre());
    }

    @Override
    public void eliminar(Long id) {
        this.ubicacionDAO.deleteById(id);
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return List.copyOf(this.ubicacionDAO.findAll());
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return this.espirituDAO.espiritusEn(ubicacionId);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return this.mediumDAO.mediumsSinEspiritusEn(ubicacionId);
    }
}
