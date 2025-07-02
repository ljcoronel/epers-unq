package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.PortalDemonios;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.geo.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.testUtils.TestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PresentacionTest {

    @Autowired
    private MediumServiceImpl mediumService;
    @Autowired
    private TestService testService;
    @Autowired
    private EspirituServiceImpl espirituService;
    @Autowired
    private UbicacionServiceImpl ubicacionService;
    @Autowired
    private UbicacionRepository ubicacionRepository;

    private Ubicacion ubicacion;

    private Demonio demonio;

    private PortalDemonios portalDemonio;

    private Medium medium1;
    private Medium medium2;
    private Medium medium3;
    private Medium medium4;

    @BeforeEach
    void crearInstancias(){
        this.ubicacion = new Santuario("Bernal",50, List.of(
                new Coordenada(0,0),
                new Coordenada(1,0),
                new Coordenada(1,1),
                new Coordenada(0,1),
                new Coordenada(0,0)),5);

        demonio = new Demonio(100,"Malote",new Coordenada(0.5,0.5));

        medium1 = new Medium("Elen", 100,50,new Coordenada(0.5,0.5));
        medium2 = new Medium("Marti", 100,50,new Coordenada(0.5,0.5));
        medium3 = new Medium("Esteban", 100,50,new Coordenada(0.5,0.5));
        medium4 = new Medium("Ana", 100,50,new Coordenada(0.5,0.5));

        portalDemonio = new PortalDemonios(new Coordenada(0.1,0.1));
    }

    private void crearDemoniosParaBajar2GradosDeTemperatura()throws InterruptedException{
        for(int i = 0;i < 10;i++){
            Thread.sleep(1000);
            espirituService.crear(new Demonio(
                    1,
                    "dem" + i, new Coordenada(0.2,0.2)
            ));
        }
    }
    @Test
    public void probarTP()throws InterruptedException{
        ubicacion.setTemperatura(10);
        ubicacionService.crear(ubicacion);

        mediumService.crear(medium1);
        mediumService.crear(medium2);
        mediumService.crear(medium3);
        mediumService.crear(medium4);

        crearDemoniosParaBajar2GradosDeTemperatura();

        demonio.setNivelDeConexion(50);

        espirituService.crear(demonio);

        espirituService.crearPortal(portalDemonio);

        Thread.sleep(70000);

        mediumService.meditar(ubicacion.getId(),0.5,0.5);

        assertTrue(true);

    }

    @AfterEach
    void eliminarModelo() {
        testService.eliminarEspiritus();
        testService.eliminarMediums();
        testService.eliminarUbicaciones();
    }
}
