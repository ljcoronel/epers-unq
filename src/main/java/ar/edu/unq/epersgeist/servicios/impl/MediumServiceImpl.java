package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.exception.EntidadTodaviaReferenciadaException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.EntidadInexistenteException;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MediumServiceImpl implements MediumService {

    private final MediumRepository mediumRepository;
    private final EspirituRepository espirituRepository;
    private final UbicacionRepository ubicacionRepository;


    public MediumServiceImpl (MediumRepository mediumRepository, EspirituRepository espirituRepository, UbicacionRepository ubicacionRepository) {
        this.mediumRepository = mediumRepository;
        this.espirituRepository = espirituRepository;
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    public Medium crear(Medium medium) {

        Ubicacion ubicacion =  ubicacionRepository.recuperar(medium.getUbicacion().getId())
                .orElseThrow(()-> new EntidadInexistenteException("La Ubicacion no existe"));

        medium.setUbicacion(ubicacion);

        return mediumRepository.guardar(medium);

    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) throws EntidadInexistenteException {
        return mediumRepository.recuperar(mediumId);
    }

    @Override
    public List<Medium> recuperarTodos() {
        return mediumRepository.recuperarTodos();
    }

    @Override
    public void actualizar(Medium medium) {
        mediumRepository.actualizar(medium);

    }

    @Override
    public void eliminar(Long idDelMedium) {
        if (this.mediumRepository.esReferenciado(idDelMedium)) throw new EntidadTodaviaReferenciadaException("El médium de id: " + idDelMedium + " todavía es referenciado.");
        mediumRepository.eliminar(idDelMedium);
    }

    @Override
    public void descansar(Long mediumId) {

        Optional<Medium> optionalMedium = mediumRepository.recuperar(mediumId);

        if (optionalMedium.isEmpty()) return;

        Medium medium = optionalMedium.get();
        medium.descansar();

        mediumRepository.guardar(medium);

    }

    @Override
    public void exorcisar(Long idMediumExorcista, Long idMediumAExorcisar) {

        Optional<Medium> optionalExorcista = mediumRepository.recuperar(idMediumExorcista);
        Optional<Medium> optionalPoseido = mediumRepository.recuperar(idMediumAExorcisar);

        if (optionalExorcista.isEmpty()) return;

        Medium exorcista = optionalExorcista.get();

        exorcista.exorcizarA(optionalPoseido);

        Medium exorcizado = optionalPoseido.get();

        mediumRepository.guardar(exorcista);
        mediumRepository.guardar(exorcizado);

    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {

        Optional<Medium> medium = mediumRepository.recuperar(mediumId);
        List<Espiritu> espiritus;

        if(medium.isEmpty()) return null;
        Medium currentMedium = medium.get();

        espiritus = espirituRepository.todosLosEspiritusDeMedium(currentMedium.getId());

        return espiritus;

    }


    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {


        Optional<Medium> optionalMedium =  mediumRepository.recuperar(mediumId);
        Optional<Espiritu> optionalEspiritu = espirituRepository.recuperar(espirituId);

        if (optionalMedium.isEmpty()) return optionalEspiritu.orElse(null);

        Medium medium = optionalMedium.get();

        medium.invocar(optionalEspiritu);

        Espiritu espiritu = optionalEspiritu.get();

        mediumRepository.guardar(medium);
        espirituRepository.guardar(espiritu);
        return espiritu;
    }

    @Override
    public void mover(Long mediumId, Long ubicacionId) {
        Medium medium = mediumRepository.recuperar(mediumId)
                .orElse(null);

        Ubicacion ubicacion = ubicacionRepository.recuperar(ubicacionId)
                .orElse(null);

        medium.setUbicacion(ubicacion);

        mediumRepository.guardar(medium);

    }

}
