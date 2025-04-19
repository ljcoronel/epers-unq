package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.EspirituDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.persistencia.MediumDAO;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Getter
@Service
@Transactional
public class MediumServiceImpl implements MediumService {

    private final MediumDAO mediumDao;
    private final EspirituDAO espirituDao;


    public MediumServiceImpl (MediumDAO mediumDao, EspirituDAO espirituDao) {
        this.mediumDao = mediumDao;
        this.espirituDao = espirituDao;
    }

    @Override
    public Medium crear(Medium medium) {
        mediumDao.save(medium);
        return null;

    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) {

        return mediumDao.findById(mediumId);
    }

    @Override
    public List<Medium> recuperarTodos() {

        //return mediumDao.recuperarTodos();
        return null;//borrar despues
    }

    @Override
    public void actualizar(Medium medium) {

           // mediumDao.actualizar(medium);


    }

    @Override
    public void eliminar(Long idDelMedium) {

        //    mediumDao.eliminar(idDelMedium);

    }

    @Override
    public void descansar(Long mediumId) {
/*

            Optional<Medium> optionalMedium = mediumDao.recuperar(mediumId);

            if (optionalMedium.isEmpty()) return;

            Medium medium = optionalMedium.get();
            medium.descansar();

            this.actualizar(medium);
*/
    }

    @Override
    public void exorcisar(Long idMediumExorcista, Long idMediumAExorcisar) {
/*
            Optional<Medium> optionalExorcista = mediumDao.recuperar(idMediumExorcista);
            Optional<Medium> optionalPoseido = mediumDao.recuperar(idMediumAExorcisar);

            if (optionalExorcista.isEmpty()) return ;
            if (optionalPoseido.isEmpty()) return ;

            Medium exorcista = optionalExorcista.get();
            Medium exorcizado = optionalPoseido.get();

            exorcista.exorcizarA(exorcizado);

            this.actualizar(exorcista);
            this.actualizar(exorcizado);
*/
    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
/*
           Optional<Medium> medium = mediumDao.recuperar(mediumId);
           List<Espiritu> espiritus = new ArrayList<>();

           if(medium.isEmpty()) {return espiritus;}
           Medium currentMedium = medium.get();

           espiritus = espirituDao.todosLosEspiritusDeMedium(currentMedium.getId());

           return espiritus;
*/
        return null;//borrar despues
    }


    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
/*
            Optional<Medium> optionalMedium =  mediumDao.recuperar(mediumId);
            Optional<Espiritu> optionalEspiritu = espirituDao.recuperar(espirituId);

            if (optionalEspiritu.isEmpty()) throw new NoSuchElementException();
            Espiritu espiritu = optionalEspiritu.get();
            if (optionalMedium.isEmpty()) return espiritu;

            Medium medium = optionalMedium.get();
            medium.invocar(espiritu);

            mediumDao.actualizar(medium);
            espirituDao.actualizar(espiritu);
            return espiritu;
*/return null;//borrar despues
    }

}
