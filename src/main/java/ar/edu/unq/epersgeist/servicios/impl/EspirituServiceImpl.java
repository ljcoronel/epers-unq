package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.modelo.exception.NombreRepetidoException;
import ar.edu.unq.epersgeist.modelo.exception.ParametroIncorrectoException;
import ar.edu.unq.epersgeist.persistencia.DAO.jpa.EspirituDAOJPA;
import ar.edu.unq.epersgeist.persistencia.DAO.jpa.MediumDAOJPA;
import ar.edu.unq.epersgeist.persistencia.DAO.jpa.UbicacionDAOJPA;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import lombok.Getter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Getter
@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {

    private final EspirituDAOJPA espirituDAOJPA;
    private final MediumDAOJPA mediumDAOJPA;
    private final UbicacionDAOJPA ubicacionDAOJPA;

    public EspirituServiceImpl(EspirituDAOJPA espirituDAOJPA, MediumDAOJPA mediumDAOJPA, UbicacionDAOJPA ubicacionDAOJPA) {
        this.espirituDAOJPA = espirituDAOJPA;
        this.mediumDAOJPA = mediumDAOJPA;
        this.ubicacionDAOJPA = ubicacionDAOJPA;
    }


    @Override
    public Espiritu crear(Espiritu espiritu) {

        verificarNombreRepetido(espiritu);

        Ubicacion ubicacion = ubicacionDAOJPA.findById(espiritu.getUbicacion().getId())
                .orElseThrow(()-> new EntidadInexistenteException("No se encontró la ubicación del espíritu."));
        espiritu.setUbicacion(ubicacion);

        return getEspirituDAOJPA().save(espiritu);

    }

    private void verificarNombreRepetido(Espiritu espiritu) {
        if (this.existeConNombre(espiritu)) throw new NombreRepetidoException("Ya existe un espíritu con el nombre: " + espiritu.getNombre());
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        return getEspirituDAOJPA().findById(espirituId);
    }

    @Override
    public List<Espiritu> recuperarTodos() {

        return espirituDAOJPA.findAll();
    }

    @Override
    public void actualizar(Espiritu espiritu) {

        verificarNombreRepetido(espiritu);

        getEspirituDAOJPA().actualizar(espiritu.getNombre(), espiritu.getNivelDeConexion(), espiritu.getId());
    }

    @Override
    public void eliminar(Long espirituId) {
        getEspirituDAOJPA().deleteById(espirituId);
    }

    @Override
    public List<Demonio> espiritusDemoniacos(Sort.Direction direccion, int pagina, int cantidadPorPagina) {
        if (pagina < 0) throw new ParametroIncorrectoException("El valor página debe ser positivo o 0.");
        if (cantidadPorPagina <= 0) throw new ParametroIncorrectoException("La cantidad por página debe ser positiva."); // porque no se debería hacer una consulta a la base de datos para traerse nada

        return getEspirituDAOJPA().findAll(Example.of(new Demonio()), PageRequest.of(pagina, cantidadPorPagina, direccion, "nivelDeConexion")).toList();
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {

            Optional<Medium> optionalMedium = mediumDAOJPA.findById(mediumId);

            Optional<Espiritu> optionalEspiritu = espirituDAOJPA.findById(espirituId);

            if (optionalEspiritu.isEmpty()) return optionalMedium.orElse(null);
            Espiritu espiritu = optionalEspiritu.get();

            espiritu.conectarConMedium(optionalMedium);

            Medium medium = optionalMedium.get();

            mediumDAOJPA.save(medium);

            return medium;

    }


    @Override
    public boolean existeConNombre(Espiritu espiritu) {
        return this.espirituDAOJPA.espirituConNombre(espiritu.getNombre(), espiritu.getId());
    }

}
