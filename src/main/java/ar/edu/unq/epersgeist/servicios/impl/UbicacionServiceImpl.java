package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateUbicacionDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;

public class UbicacionServiceImpl {

    private final HibernateUbicacionDAO ubicacionDAO;

    public UbicacionServiceImpl(HibernateUbicacionDAO ubicacionDAO) {
        this.ubicacionDAO = ubicacionDAO;
    }

    public void crear(Ubicacion ubicacion) {
        HibernateTransactionRunner.runTrx(() -> {
            this.ubicacionDAO.crear(ubicacion);
            return null;
        });
    }

    public Ubicacion recuperar(Long id) {
        return HibernateTransactionRunner.runTrx(() -> this.ubicacionDAO.recuperar(id));
    }

    public void actualizar(Ubicacion ubicacionUno) {
        HibernateTransactionRunner.runTrx(() -> {
            this.ubicacionDAO.actualizar(ubicacionUno);
            return null;
        });
    }

    public void eliminar(Ubicacion ubicacionUno) {
        HibernateTransactionRunner.runTrx(() -> {
            this.ubicacionDAO.eliminar(ubicacionUno);
            return null;
        });
    }

    public List<Ubicacion> recuperarTodos() {
        return HibernateTransactionRunner.runTrx(() -> this.ubicacionDAO.recuperarTodos());
    }
}
