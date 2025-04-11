package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class MediumServiceImpl implements MediumService {

    private MediumDAO mediumDao;
    private EspirituDAO espirituDao;


    public MediumServiceImpl (MediumDAO mediumDao, EspirituDAO espirituDao) {
        this.mediumDao = mediumDao;
        this.espirituDao = espirituDao;
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
        return List.of();
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
        //dado un medium y un espíritu, el medium deber invocar al espíritu a su ubicación
        // generándole un costo de 10 puntos de mana. si el medium no tiene mana suficiente
        // no hace nada. si el espíritu un esta libre, lanzar una excepción.
        return HibernateTransactionRunner.runTrx(()->{
            Optional<Medium> optionalMedium =  mediumDao.recuperar(mediumId);
            Optional<Espiritu> optionalEspiritu = espirituDao.recuperar(espirituId);

            if (optionalMedium.isEmpty()|| optionalEspiritu.isEmpty()) return null;

            Medium medium = optionalMedium.get();
            Espiritu espiritu = optionalEspiritu.get();

            medium.invocar(espiritu);
            mediumDao.actualizar(medium);
            espirituDao.actualizar(espiritu);
            return espiritu;
        });
    }

    public MediumDAO getMediumDao() {
        return mediumDao;
    }
}
