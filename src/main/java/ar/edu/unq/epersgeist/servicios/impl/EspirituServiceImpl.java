package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.EspirituLibreException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionDistintaException;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import lombok.Getter;
import org.hibernate.query.SortDirection;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Getter
public class EspirituServiceImpl implements EspirituService {

    private EspirituDAO espirituDao;
    private MediumDAO mediumDao;

    public EspirituServiceImpl(EspirituDAO espirituDao,MediumDAO mediumDAO) {
        setEspirituDao(espirituDao);
        this.mediumDao = mediumDAO;
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
    public List<Demonio> espiritusDemoniacos(SortDirection direccion, int pagina, int cantidadPorPagina) {
        return HibernateTransactionRunner.runTrx(() -> getEspirituDao().espiritusDeTipo(direccion, pagina, cantidadPorPagina, Demonio.class));
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
         return HibernateTransactionRunner.runTrx(() -> {

            Optional<Medium> optionalMedium = mediumDao.recuperar(mediumId);
            Optional<Espiritu> optionalEspiritu = espirituDao.recuperar(espirituId);
            this.verificarExistenciaEspirituYMedium(optionalEspiritu,optionalMedium);
            Medium medium = optionalMedium.get();
            Espiritu espiritu = optionalEspiritu.get();

            if (!(espiritu.getUbicacion() == medium.getUbicacion())){
                throw new UbicacionDistintaException(espiritu, medium);
             }
            if (!espiritu.estaLibre()) {
                throw new EspirituLibreException(espiritu);
            }
            int conexionAAumentar = (int) Math.round(medium.getMana() * 0.20);
            espiritu.aumentarNivelDeConexionEn(conexionAAumentar);
            espiritu.setMedium(medium);

            mediumDao.actualizar(medium);

            return medium;
        });

    }

    private void verificarSiExisteEspiritu(Optional<Espiritu> espiritu){
        if (!espiritu.isPresent()) {
            throw new NoSuchElementException("No se encontró el Espiritu");
        }
    }

    private void verificarSiExisteMedium(Optional<Medium> medium){
        if (medium.isEmpty()) {
            throw new NoSuchElementException("No se encontró el Medium");
        }
    }

    private void verificarExistenciaEspirituYMedium(Optional<Espiritu> espiritu,Optional<Medium> medium){
        this.verificarSiExisteEspiritu(espiritu);
        this.verificarSiExisteMedium(medium);

    }

    private void setEspirituDao(EspirituDAO espirituDao) {
        this.espirituDao = espirituDao;
    }
}
