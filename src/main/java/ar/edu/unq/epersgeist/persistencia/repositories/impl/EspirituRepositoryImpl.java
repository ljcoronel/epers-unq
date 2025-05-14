package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
@Setter @Getter
public class EspirituRepositoryImpl implements EspirituRepository {

    private final EspirituDAO espirituDAO;

    public EspirituRepositoryImpl(EspirituDAO espirituDAO) {
        this.espirituDAO = espirituDAO;
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        getEspirituDAO().actualizar(espiritu.getNombre(), espiritu.getNivelDeConexion(), espiritu.getId());
    }

    @Override
    public Espiritu guardar(Espiritu espiritu) {
        EspirituJPADTO espirituJPADTO = EspirituJPADTO.desdeModelo(espiritu);

        return getEspirituDAO().save(espirituJPADTO).aModelo();
    }

    @Override
    public Optional<Espiritu> recuperar(Long id) {
        Optional<EspirituJPADTO> optionalEspirituJPADTO = getEspirituDAO().findById(id);

        if (optionalEspirituJPADTO.isEmpty()) return Optional.empty();

        return Optional.of(optionalEspirituJPADTO.get().aModelo());
    }

    @Override
    public void eliminar(Long id) {
        getEspirituDAO().deleteById(id);
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return getEspirituDAO().findAll().stream().map(EspirituJPADTO::aModelo).toList();
    }

    @Override
    public boolean espirituConNombre(String nombre, Long id) {
        return getEspirituDAO().espirituConNombre(nombre, id);
    }

    @Override
    public List<Demonio> espiritusDemoniacos(Sort.Direction direccion, int pagina, int cantidadPorPagina) {
       return getEspirituDAO().findAll
                       (Example.of
                               (EspirituJPADTO.desdeModelo(new Demonio()))
                               , PageRequest.of(
                                       pagina
                                       ,cantidadPorPagina,
                                       direccion, "nivelDeConexion"))
               .map(dto -> (Demonio) dto.aModelo()).toList();
    }
}
