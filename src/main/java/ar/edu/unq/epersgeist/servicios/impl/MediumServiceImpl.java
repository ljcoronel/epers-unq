package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.RandomGenerator.RandomGenerator;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

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
        HibernateTransactionRunner.runTrx(() -> {
            Optional<Medium> optionalExorcista = mediumDao.recuperar(idMediumExorcista);
            Optional<Medium> optionalPoseido = mediumDao.recuperar(idMediumAExorcisar);

            this.verificarSiExistenLosMediums(optionalExorcista,optionalPoseido);

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
        return List.of();
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
        return null;
    }

    private void verificarSiExisteMedium(Optional<Medium> medium){
        if (medium.isEmpty()) {
            throw new NoSuchElementException("No se encontró el Medium");
        }
    }

    private void verificarSiExistenLosMediums(Optional<Medium> medium,Optional<Medium> medium2){
        this.verificarSiExisteMedium(medium);
        this.verificarSiExisteMedium(medium2);

    }

}
