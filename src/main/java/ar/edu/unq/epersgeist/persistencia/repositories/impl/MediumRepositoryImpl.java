package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.repositories.dto.sql.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Setter @Getter
@Repository
public class MediumRepositoryImpl implements MediumRepository {

    private final MediumDAO mediumDAO;

    public MediumRepositoryImpl(MediumDAO mediumDAO) {
        this.mediumDAO = mediumDAO;
    }

    @Override
    public void actualizar(Medium medium) {
        getMediumDAO().actualizar(medium.getNombre(), medium.getManaMax(),medium.getMana(), medium.getId());
    }

    @Override
    public Medium guardar(Medium medium) {
        MediumJPADTO mediumJPADTO = MediumJPADTO.desdeModelo(medium);

        return getMediumDAO().save(mediumJPADTO).aModelo(medium);
    }

    @Override
    public Optional<Medium> recuperar(Long id) {

        Optional<MediumJPADTO> optionalMediumJPADTO = getMediumDAO().findById(id);

        if (optionalMediumJPADTO.isEmpty()) return Optional.empty();

        return Optional.of(optionalMediumJPADTO.get().aModelo());
    }

    @Override
    public void eliminar(Long id) {
        getMediumDAO().deleteById(id);
    }

    @Override
    public List<Medium> recuperarTodos() {
        return getMediumDAO().findAll().stream().map(MediumJPADTO::aModelo).toList();
    }

    @Override
    public boolean esReferenciado(Long idDelMedium) {
        return getMediumDAO().esReferenciado(idDelMedium);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return getMediumDAO().mediumsSinEspiritusEn(ubicacionId);
    }

    @Override
    public Long mediumConMasDemoniosEn(Long idSantuario) {
        return getMediumDAO().mediumConMasDemoniosEn(idSantuario);
    }
}
