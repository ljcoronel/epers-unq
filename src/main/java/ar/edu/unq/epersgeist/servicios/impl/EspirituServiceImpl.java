package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;

public class EspirituServiceImpl implements EspirituService {

    private EspirituDAO espirituDao;

    public EspirituServiceImpl(EspirituDAO espirituDao) {
        setEspirituDao(espirituDao);
    }

    @Override
    public void crear(Espiritu espiritu) {
        HibernateTransactionRunner.runTrx(() -> {
            getEspirituDao().guardar(espiritu);
            return null;
        });
    }

    @Override
    public Espiritu recuperar(Long espirituId) {
        return HibernateTransactionRunner.runTrx(() -> {
            return this.espirituDao.recuperar(espirituId);
        });
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return HibernateTransactionRunner.runTrx(() -> {
            return espirituDao.recuperarTodos();
        });
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        HibernateTransactionRunner.runTrx(() -> {
            getEspirituDao().actualizar(espiritu);
            return null;
        });
    }

    @Override
    public void eliminar(Espiritu espiritu) { // se podría redefinir la interface para que acepte espiritus en vez de espirituId?
        HibernateTransactionRunner.runTrx(() -> { // hacer un try catch para decir "no se puede eliminar si no existe?"
            getEspirituDao().eliminar(espiritu);
            return null;
        });
    }

//    @Override
//    public List<Espiritu> espiritusDemoniacos() {
//        return HibernateTransactionRunner.runTrx(() -> getEspirituDao().espiritusDeTipo("Demonio"));
//    }

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
