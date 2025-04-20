package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
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
        return this.ubicacionDAO.save(ubicacion);
    }

    @Override
    public Optional<Ubicacion> recuperar(Long id) {
        return this.ubicacionDAO.findById(id);
    }

    @Override
    public void actualizar(Ubicacion ubicacionUno) {
        
        this.ubicacionDAO.save(ubicacionUno);

    }

    @Override
    public void eliminar(Long id) {
        
            this.ubicacionDAO.deleteById(id);
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return this.ubicacionDAO.findAll();
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        //return this.espirituDAO.espiritusEn(ubicacionId);
        return null;
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        //return this.mediumDAO.mediumsSinEspiritusEn(ubicacionId);
        return null;
    }
}
