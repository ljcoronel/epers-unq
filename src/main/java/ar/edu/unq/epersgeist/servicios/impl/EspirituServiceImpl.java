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
        if (pagina < 0) throw new IllegalArgumentException("El valor página debe ser positivo o 0.");
        if (cantidadPorPagina <= 0) throw new IllegalArgumentException("La cantidad por página debe ser positiva."); // porque no se debería hacer una consulta a la base de datos para traerse nada
        return HibernateTransactionRunner.runTrx(() -> getEspirituDao().espiritusDeTipo(direccion, pagina, cantidadPorPagina, Demonio.class));
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
         return HibernateTransactionRunner.runTrx(() -> {

            Optional<Medium> optionalMedium = mediumDao.recuperar(mediumId);
            Optional<Espiritu> optionalEspiritu = espirituDao.recuperar(espirituId);

            verificarSiExisteMedium(optionalMedium);
            Medium medium = optionalMedium.get();

            if (optionalEspiritu.isEmpty()) return medium; // @TODO
            Espiritu espiritu = optionalEspiritu.get();

            verificarEspirituYMediumEnMismaUbicacion(espiritu, medium);
            verificarEspirituLibre(espiritu);

            espiritu.conectarConMedium(medium);

            mediumDao.actualizar(medium);

            return medium;
        });

    }

    private static void verificarEspirituLibre(Espiritu espiritu) {
        if (!espiritu.estaLibre()) {
            throw new EspirituLibreException(espiritu);
        }
    }

    private static void verificarEspirituYMediumEnMismaUbicacion(Espiritu espiritu, Medium medium) throws UbicacionDistintaException {
        if (!(espiritu.getUbicacion() == medium.getUbicacion())){
            throw new UbicacionDistintaException(espiritu, medium);
         }
    }

    private void verificarSiExisteMedium(Optional<Medium> medium){
        if (medium.isEmpty()) {
            throw new NoSuchElementException("No se encontró el Medium");
        }
    }

    private void setEspirituDao(EspirituDAO espirituDao) {
        this.espirituDao = espirituDao;
    }
}
