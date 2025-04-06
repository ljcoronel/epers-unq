package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;

public class MediumServiceImpl implements MediumService {

    private MediumDAO mediumDao;

    public MediumServiceImpl (MediumDAO mediumDao){
        this.mediumDao = mediumDao;
    }

    @Override
    public void crear(Medium medium) {
        HibernateTransactionRunner.runTrx(() -> {
            getMediumDao().guardar(medium);
            return null;
        });
    }

    @Override
    public Medium recuperar(Long mediumId) {
        return null;
    }

    @Override
    public List<Medium> recuperarTodos() {
        return HibernateTransactionRunner.runTrx(() -> this.getMediumDao().recuperarTodos());
    }

    @Override
    public void actualizar(Medium medium) {

    }

    @Override
    public void eliminar(Medium medium) {
        HibernateTransactionRunner.runTrx(() -> {
            getMediumDao().eliminar(medium);
            return null;
        });
    }

    @Override
    public void descansar(Long mediumId) {

    }

    @Override
    public void exorcisar(Long idMediumExorcista, Long idMediumAExorcisar) {

    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        return List.of();
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
        return null;
    }

    public MediumDAO getMediumDao() {
        return mediumDao;
    }
}
