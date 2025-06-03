package ar.edu.unq.epersgeist.testUtils;
import ar.edu.unq.epersgeist.persistencia.dao.mongo.EspirituDAOMongo;
import ar.edu.unq.epersgeist.persistencia.dao.neo4j.UbicacionDAONEO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TestService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UbicacionDAONEO ubicacionDAONEO;
    @Autowired
    private EspirituDAOMongo espirituDAOMongo;

    public void eliminarEspiritus() {
        entityManager.createNativeQuery("DELETE FROM espiritu").executeUpdate();
        espirituDAOMongo.deleteAll();

    }

    public void eliminarUbicaciones() {
        entityManager.createNativeQuery("DELETE FROM ubicacion").executeUpdate();
        ubicacionDAONEO.deleteAll();

    }

    public void eliminarMediums() {
        entityManager.createNativeQuery("DELETE FROM medium").executeUpdate();
    }
}