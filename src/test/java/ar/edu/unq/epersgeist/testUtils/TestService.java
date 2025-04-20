package ar.edu.unq.epersgeist.testUtils;

import ar.edu.unq.epersgeist.persistencia.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.UbicacionDAO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TestService {

    private EspirituDAO espDao;
    private UbicacionDAO ubDao;
    private MediumDAO medDao;

    public TestService(EspirituDAO espDao, UbicacionDAO ubDao, MediumDAO medDao) {
        this.espDao = espDao;
        this.ubDao = ubDao;
        this.medDao = medDao;
    }

    public void eliminarEspíritus() {
        espDao.deleteAll();
    }

    public void eliminarUbicaciones() {
        ubDao.deleteAll();
    }

    public void eliminarMediums() {
        medDao.deleteAll();
    }
}