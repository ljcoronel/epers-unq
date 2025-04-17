package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.RandomGenerator.RandomGenerator;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.ArrayList;
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
            mediumDao.crear(medium);
            return null;
        });
    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) {
        return HibernateTransactionRunner.runTrx(() -> mediumDao.recuperar(mediumId));
    }

    @Override
    public List<Medium> recuperarTodos() {
        return HibernateTransactionRunner.runTrx(() -> mediumDao.recuperarTodos());
    }

    @Override
    public void actualizar(Medium medium) {
        HibernateTransactionRunner.runTrx(()-> {
            mediumDao.actualizar(medium);
            return null;
        } );

    }

    @Override
    public void eliminar(Long idDelMedium) {
        HibernateTransactionRunner.runTrx(() -> {
            mediumDao.eliminar(idDelMedium);
            return null;
        });
    }

    @Override
    public void descansar(Long mediumId) {
        HibernateTransactionRunner.runTrx(() -> {

            Optional<Medium> optionalMedium = mediumDao.recuperar(mediumId);

            if (optionalMedium.isEmpty()) return null;

            Medium medium = optionalMedium.get();
            medium.descansar();

            this.actualizar(medium);

            return null;
        });
    }

    @Override
    public void exorcisar(Long idMediumExorcista, Long idMediumAExorcisar) {
        HibernateTransactionRunner.runTrx(() -> {
            Optional<Medium> optionalExorcista = mediumDao.recuperar(idMediumExorcista);
            Optional<Medium> optionalPoseido = mediumDao.recuperar(idMediumAExorcisar);

            if (optionalExorcista.isEmpty()) return null;
            if (optionalPoseido.isEmpty()) return null;

            Medium exorcista = optionalExorcista.get();
            Medium exorcizado = optionalPoseido.get();

            exorcista.exorcizarA(exorcizado);

            this.actualizar(exorcista);
            this.actualizar(exorcizado);

            return null;
        });
    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        return HibernateTransactionRunner.runTrx(()->{
           Optional<Medium> medium = mediumDao.recuperar(mediumId);
           List<Espiritu> espiritus = new ArrayList<>();

           if(medium.isEmpty()) {return espiritus;}
           Medium currentMedium = medium.get();

           espiritus = espirituDao.todosLosEspiritusDeMedium(currentMedium.getId());

           return espiritus;
        });
    }


    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
        return HibernateTransactionRunner.runTrx(()->{
            Optional<Medium> optionalMedium =  mediumDao.recuperar(mediumId);
            Optional<Espiritu> optionalEspiritu = espirituDao.recuperar(espirituId);

            if (optionalEspiritu.isEmpty()) throw new NoSuchElementException();
            Espiritu espiritu = optionalEspiritu.get();
            if (optionalMedium.isEmpty()) return espiritu;

            Medium medium = optionalMedium.get();
            medium.invocar(espiritu);

            mediumDao.actualizar(medium);
            espirituDao.actualizar(espiritu);
            return espiritu;
        });
    }

}
