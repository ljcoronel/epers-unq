package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;
import java.util.Optional;

public class EspirituServiceImpl implements EspirituService {

    private EspirituDAO espirituDao;

    public EspirituServiceImpl(EspirituDAO espirituDao) {
        setEspirituDao(espirituDao);
    }

    @Override
    public Espiritu crear(Espiritu espiritu) {
        return HibernateTransactionRunner.runTrx(() -> {
            getEspirituDao().crear(espiritu);
            return null;
        });
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        return HibernateTransactionRunner.runTrx(() -> this.espirituDao.recuperar(espirituId));
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return HibernateTransactionRunner.runTrx(() -> espirituDao.recuperarTodos());
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        HibernateTransactionRunner.runTrx(() -> {
            getEspirituDao().actualizar(espiritu);
            return null;
        });
    }

    @Override
    public void eliminar(Long espirituId) {
        HibernateTransactionRunner.runTrx(() -> {
            getEspirituDao().eliminar(espirituId);
            return null;
        });
    }

    @Override
    public List<Demonio> espiritusDemoniacos(Direccion direccion, int pagina, int cantidadPorPagina) {
        return HibernateTransactionRunner.runTrx(() -> getEspirituDao().espiritusDeTipo(direccion, pagina, cantidadPorPagina, Demonio.class));
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
//        Espiritu espiritu = recuperar(espirituId);
//
//        medium.conectarseAEspiritu(espiritu);
//
//        getEspirituDao().actualizar(espiritu); //garantiza ACID
//        return medium;
        return null;
    }

    public EspirituDAO getEspirituDao() {
        return espirituDao;
    }

    private void setEspirituDao(EspirituDAO espirituDao) {
        this.espirituDao = espirituDao;
    }
}
