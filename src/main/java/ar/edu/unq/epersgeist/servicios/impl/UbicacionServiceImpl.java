package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateUbicacionDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;
import java.util.Optional;

public class UbicacionServiceImpl {

    private final HibernateUbicacionDAO ubicacionDAO;
    private final HibernateEspirituDAO espirituDAO;

    public UbicacionServiceImpl(HibernateUbicacionDAO ubicacionDAO, HibernateEspirituDAO espirituDAO) {
        this.ubicacionDAO = ubicacionDAO;
        this.espirituDAO = espirituDAO;
    }

    public Ubicacion crear(Ubicacion ubicacion) {
        return HibernateTransactionRunner.runTrx(() -> this.ubicacionDAO.crear(ubicacion));
    }

    public Optional<Ubicacion> recuperar(Long id) {
        return HibernateTransactionRunner.runTrx(() -> this.ubicacionDAO.recuperar(id));
    }

    public void actualizar(Ubicacion ubicacionUno) {
        HibernateTransactionRunner.runTrx(() -> {
            this.ubicacionDAO.actualizar(ubicacionUno);
            return null;
        });
    }

    public void eliminar(Long id) {
        HibernateTransactionRunner.runTrx(() -> {
            this.ubicacionDAO.eliminar(id);
            return null;
        });
    }

    public List<Ubicacion> recuperarTodos() {
        return HibernateTransactionRunner.runTrx(this.ubicacionDAO::recuperarTodos);
    }

    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return HibernateTransactionRunner.runTrx(() -> this.espirituDAO.espiritusEn(ubicacionId));
    }
}
