package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class MediumServiceImpl implements MediumService {

    private MediumDAO mediumDao;

    public MediumServiceImpl (MediumDAO mediumDao){
        this.mediumDao = mediumDao;
    }

    @Override
    public Medium crear(Medium medium) {
        return HibernateTransactionRunner.runTrx(() -> {
            getMediumDao().crear(medium);
            return null;
        });
    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) {
        return HibernateTransactionRunner.runTrx(() -> getMediumDao().recuperar(mediumId));
    }

    @Override
    public List<Medium> recuperarTodos() {
        return HibernateTransactionRunner.runTrx(() -> this.getMediumDao().recuperarTodos());
    }

    @Override
    public void actualizar(Medium medium) {
        HibernateTransactionRunner.runTrx(()-> {
            getMediumDao().actualizar(medium);
            return null;
        } );

    }

    @Override
    public void eliminar(Long idDelMedium) {
        HibernateTransactionRunner.runTrx(() -> {
            getMediumDao().eliminar(idDelMedium);
            return null;
        });
    }

    @Override
    public void descansar(Long mediumId) {
        HibernateTransactionRunner.runTrx(() -> {
            Optional<Medium> optionalMedium = mediumDao.recuperar(mediumId);
            if (!optionalMedium.isPresent()) {
                throw new NoSuchElementException("No se encontró el Medium con ID: " + mediumId);
            }
            Medium medium = optionalMedium.get();

            medium.descansar();
            this.actualizar(medium);
            return null;
        });
    }

    @Override
    public void exorcisar(Long idMediumExorcista, Long idMediumAExorcisar) {

    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        return HibernateTransactionRunner.runTrx(()->{
           Optional<Medium> medium = mediumDao.recuperar(mediumId);
           if (medium.isEmpty()){
               throw new NoSuchElementException("No se encontró el Medium con ID: " + mediumId);
           }
           Medium currentMedium = medium.get();
           List<Espiritu> espiritus = new ArrayList<>();
           espiritus.addAll(currentMedium.getAngeles());
           espiritus.addAll(currentMedium.getDemonios());
           return espiritus;
        });
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
        return null;
    }

    public MediumDAO getMediumDao() {
        return mediumDao;
    }
}
