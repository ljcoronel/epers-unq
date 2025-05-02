package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.modelo.exception.NombreRepetidoException;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;
import ar.edu.unq.epersgeist.persistencia.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.MediumDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import lombok.Getter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.caseSensitive;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Getter
@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {

    private final EspirituDAO espirituDao;
    private final MediumDAO mediumDao;

    public EspirituServiceImpl(EspirituDAO espirituDao,MediumDAO mediumDAO) {
        this.espirituDao = espirituDao;
        this.mediumDao = mediumDAO;
    }


    @Override
    public Espiritu crear(Espiritu espiritu) {
        verificarNombreValido(espiritu);

        return getEspirituDao().save(espiritu);

    }

    private void verificarNombreValido(Espiritu espiritu) {
        if (espiritu.getNombre().isEmpty()) throw new ParametroIncorrectoException("El espíritu debe tener un nombre.");
        if (this.existeConNombre(espiritu)) throw new NombreRepetidoException("Ya existe un espíritu con el nombre: " + espiritu.getNombre());
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        return getEspirituDao().findById(espirituId);
    }

    @Override
    public List<Espiritu> recuperarTodos() {

        return espirituDao.findAll();
    }

    @Override
    public void actualizar(Espiritu espiritu) {

            Espiritu espirituDelService = this.recuperar(espiritu.getId())
                    .orElseThrow(()->new EntidadInexistenteException("No se encontró el id de la espíritu."));

            espirituDelService.setNombre(espiritu.getNombre());
            espirituDelService.setNivelDeConexion(espiritu.getNivelDeConexion());
            espirituDelService.setUbicacion(espiritu.getUbicacion());


            verificarNombreValido(espiritu);

            getEspirituDao().save(espirituDelService);
    }

    @Override
    public void eliminar(Long espirituId) {
            getEspirituDao().deleteById(espirituId);
    }

    @Override
    public List<Demonio> espiritusDemoniacos(Sort.Direction direccion, int pagina, int cantidadPorPagina) {
        if (pagina < 0) throw new ParametroIncorrectoException("El valor página debe ser positivo o 0.");
        if (cantidadPorPagina <= 0) throw new ParametroIncorrectoException("La cantidad por página debe ser positiva."); // porque no se debería hacer una consulta a la base de datos para traerse nada

        return getEspirituDao().findAll(Example.of(new Demonio()), PageRequest.of(pagina, cantidadPorPagina, direccion, "nivelDeConexion")).toList();
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {

            Medium medium = mediumDao.findById(mediumId)
                    .orElseThrow(() -> new EntidadInexistenteException("No se encontró al Medium"));

            Optional<Espiritu> optionalEspiritu = espirituDao.findById(espirituId);

            if (optionalEspiritu.isEmpty()) return medium;
            Espiritu espiritu = optionalEspiritu.get();

            espiritu.conectarConMedium(medium);

            mediumDao.save(medium);

            return medium;

    }


    @Override
    public boolean existeConNombre(Espiritu espiritu) {
        return this.espirituDao.espirituConNombre(espiritu.getNombre(), espiritu.getId()); // TODO preguntar si se debe devolver true o false, o si así es suficiente
    }

}
