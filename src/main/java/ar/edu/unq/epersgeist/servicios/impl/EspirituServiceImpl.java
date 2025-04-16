package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.EspirituLibreException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionDistintaException;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import lombok.Getter;
import org.hibernate.query.SortDirection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Getter
@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {

    private EspirituDAO espirituDao;
    private MediumDAO mediumDao;

    public EspirituServiceImpl(EspirituDAO espirituDao,MediumDAO mediumDAO) {
        setEspirituDao(espirituDao);
        this.mediumDao = mediumDAO;
    }

    @Override
    public Espiritu crear(Espiritu espiritu) {

            getEspirituDao().crear(espiritu);
            return null;

    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        return this.espirituDao.recuperar(espirituId);
    }

    @Override
    public List<Espiritu> recuperarTodos() {

        return espirituDao.recuperarTodos();
    }

    @Override
    public void actualizar(Espiritu espiritu) {

            getEspirituDao().actualizar(espiritu);

    }

    @Override
    public void eliminar(Long espirituId) {

            getEspirituDao().eliminar(espirituId);
    }

    @Override
    public List<Demonio> espiritusDemoniacos(SortDirection direccion, int pagina, int cantidadPorPagina) {
        if (pagina < 0) throw new IllegalArgumentException("El valor página debe ser positivo o 0.");
        if (cantidadPorPagina <= 0) throw new IllegalArgumentException("La cantidad por página debe ser positiva."); // porque no se debería hacer una consulta a la base de datos para traerse nada
        return getEspirituDao().espiritusDeTipo(direccion, pagina, cantidadPorPagina, Demonio.class);
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {


            Optional<Medium> optionalMedium = mediumDao.recuperar(mediumId);
            Optional<Espiritu> optionalEspiritu = espirituDao.recuperar(espirituId);

            verificarSiExisteMedium(optionalMedium);
            Medium medium = optionalMedium.get();

            if (optionalEspiritu.isEmpty()) return medium;
            Espiritu espiritu = optionalEspiritu.get();


            espiritu.conectarConMedium(medium);

            mediumDao.actualizar(medium);

            return medium;


    }


    private void verificarSiExisteMedium(Optional<Medium> medium){
        if (medium.isEmpty()) {
            throw new NoSuchElementException("No se encontró al Medium");
        }
    }

    private void setEspirituDao(EspirituDAO espirituDao) {
        this.espirituDao = espirituDao;
    }
}
