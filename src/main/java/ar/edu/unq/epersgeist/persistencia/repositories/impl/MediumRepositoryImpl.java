package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.jpa.MediumDAOJPA;
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
    private final MediumDAOJPA mediumDAOJPA;

    public MediumRepositoryImpl(MediumDAOJPA mediumDAOJPA) {
        this.mediumDAOJPA = mediumDAOJPA;
    }

    @Override
    public void actualizar(Medium medium) {
        getMediumDAOJPA().actualizar(medium.getNombre(), medium.getManaMax(),medium.getMana(), medium.getId());
    }

    @Override
    public Medium guardar(Medium medium) {
        MediumJPADTO mediumJPADTO = MediumJPADTO.desdeModelo(medium);
        getMediumDAOJPA().save(mediumJPADTO);
        return mediumJPADTO.aModelo(medium);
    }

    @Override
    public Optional<Medium> recuperar(Long id) {
        Optional<MediumJPADTO> optionalMediumJPADTO = getMediumDAOJPA().findById(id);
        return optionalMediumJPADTO.map(MediumJPADTO::aModelo);
    }

    @Override
    public void eliminar(Long id) {
        getMediumDAOJPA().deleteById(id);
    }

    @Override
    public List<Medium> recuperarTodos() {
        return getMediumDAOJPA().findAll().stream().map(MediumJPADTO::aModelo).toList();
    }

    @Override
    public boolean esReferenciado(Long idDelMedium) {
        return getMediumDAOJPA().esReferenciado(idDelMedium);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return getMediumDAOJPA().mediumsSinEspiritusEn(ubicacionId).stream().map(MediumJPADTO::aModelo).toList();
    }

    @Override
    public Long mediumConMasDemoniosEn(Long idSantuario) {
        return getMediumDAOJPA().mediumConMasDemoniosEn(idSantuario);
    }
}
