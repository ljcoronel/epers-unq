package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateMediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateUbicacionDAO;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;
import java.util.Optional;

public class UbicacionServiceImpl implements UbicacionService {

    private final HibernateUbicacionDAO ubicacionDAO;
    private final HibernateEspirituDAO espirituDAO;
    private final HibernateMediumDAO mediumDAO;

    public UbicacionServiceImpl(HibernateUbicacionDAO ubicacionDAO, HibernateEspirituDAO espirituDAO, HibernateMediumDAO mediumDAO) {
        this.ubicacionDAO = ubicacionDAO;
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
    }

    @Override
    public Ubicacion crear(Ubicacion ubicacion) {
        return HibernateTransactionRunner.runTrx(() -> this.ubicacionDAO.crear(ubicacion));
    }

    @Override
    public Optional<Ubicacion> recuperar(Long id) {
        return HibernateTransactionRunner.runTrx(() -> this.ubicacionDAO.recuperar(id));
    }

    @Override
    public void actualizar(Ubicacion ubicacionUno) {
        HibernateTransactionRunner.runTrx(() -> {
            this.ubicacionDAO.actualizar(ubicacionUno);
            return null;
        });
    }

    @Override
    public void eliminar(Long id) {
        HibernateTransactionRunner.runTrx(() -> {
            this.ubicacionDAO.eliminar(id);
            return null;
        });
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return HibernateTransactionRunner.runTrx(this.ubicacionDAO::recuperarTodos);
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return HibernateTransactionRunner.runTrx(() -> this.espirituDAO.espiritusEn(ubicacionId));
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return HibernateTransactionRunner.runTrx(() -> this.mediumDAO.mediumsSinEspiritusEn(ubicacionId));
    }
}
