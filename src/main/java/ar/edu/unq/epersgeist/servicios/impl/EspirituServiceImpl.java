package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
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
        if (espiritu.getNombre().isEmpty()) throw new IllegalArgumentException("Es espiritu debe tener un nombre");
        if (this.existeConNombre(espiritu.getNombre())) throw new IllegalArgumentException("ya existe un espiritu con el nombre: " + espiritu.getNombre());

        return getEspirituDao().save(espiritu);

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
            getEspirituDao().save(espiritu);

    }

    @Override
    public void eliminar(Long espirituId) {
            getEspirituDao().deleteById(espirituId);
    }

    @Override
    public List<Demonio> espiritusDemoniacos(Sort.Direction direccion, int pagina, int cantidadPorPagina) {
        if (pagina < 0) throw new IllegalArgumentException("El valor página debe ser positivo o 0.");
        if (cantidadPorPagina <= 0) throw new IllegalArgumentException("La cantidad por página debe ser positiva."); // porque no se debería hacer una consulta a la base de datos para traerse nada

        return getEspirituDao().findAll(Example.of(new Demonio()), PageRequest.of(pagina, cantidadPorPagina, direccion, "nivelDeConexion")).toList();
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {

            Optional<Medium> optionalMedium = mediumDao.findById(mediumId);
            Optional<Espiritu> optionalEspiritu = espirituDao.findById(espirituId);

            verificarSiExisteMedium(optionalMedium);
            Medium medium = optionalMedium.get();

            if (optionalEspiritu.isEmpty()) return medium;
            Espiritu espiritu = optionalEspiritu.get();

            espiritu.conectarConMedium(medium);

            mediumDao.save(medium);

            return medium;

    }

    @Override
    public boolean existeConNombre(String nombre) {
        Espiritu espirituConNombre = this.espirituDao.espirituConNombre(nombre); // TODO preguntar si se debe devolver true o false, o si así es suficiente
        return espirituConNombre != null;
    }

    private void verificarSiExisteMedium(Optional<Medium> medium){
        if (medium.isEmpty()) {
            throw new NoSuchElementException("No se encontró al Medium");
        }
    }
}
