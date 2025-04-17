package ar.edu.unq.epergeist.testUtils;

import ar.edu.unq.epersgeist.persistencia.EspirituDAO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TestService {

    private EspirituDAO espDao;

    public TestService(EspirituDAO espDao) {
        this.espDao = espDao;
    }

    public void eliminarEspíritus() {
        espDao.deleteAll();
    }
}