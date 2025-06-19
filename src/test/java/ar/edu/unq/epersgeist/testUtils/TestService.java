package ar.edu.unq.epersgeist.testUtils;
import ar.edu.unq.epersgeist.persistencia.DAO.influx.LecturaDAOInflux;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TestService {

    @Autowired
    private LecturaDAOInflux influxDB;

    @PersistenceContext
    private EntityManager entityManager;

    public void eliminarEspiritus() {
        entityManager.createNativeQuery("DELETE FROM espiritu").executeUpdate();
    }

    public void eliminarUbicaciones() {
        entityManager.createNativeQuery("DELETE FROM ubicacion").executeUpdate();
    }

    public void eliminarMediums() {
        entityManager.createNativeQuery("DELETE FROM medium").executeUpdate();
    }

}