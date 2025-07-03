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
    private Ubicacion ubicacion2;
    private Ubicacion ubicacion3;

    private Demonio demonio;
    private Demonio demonio2;
    private Demonio demonio3;
    private Demonio demonio4;

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

        this.ubicacion2 = new Santuario("Quilmes",50, List.of(
                new Coordenada(2,0),
                new Coordenada(3,0),
                new Coordenada(3,1),
                new Coordenada(2,1),
                new Coordenada(2,0)),5);

        this.ubicacion3 = new Santuario("Unq",50, List.of(
                new Coordenada(0,2),
                new Coordenada(1,2),
                new Coordenada(1,3),
                new Coordenada(0,3),
                new Coordenada(0,2)),5);

        demonio = new Demonio(50,"malito",new Coordenada(0.5,0.5));
        demonio2 = new Demonio(30,"Malote",new Coordenada(2.5,0.5));
        demonio3 = new Demonio(40,"Demo",new Coordenada(0.5,2.5));
        demonio4 = new Demonio(30,"Coco",new Coordenada(0.5,0.58));

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
    public void probarMeditacion()throws InterruptedException{
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

    @Test
    public void probarCreacionDePortales() throws InterruptedException {
        //Thread.sleep(60000);
        ubicacion.setTemperatura(20);
        ubicacion2.setTemperatura(17);
        ubicacion3.setTemperatura(23);

        this.ubicacionService.crear(ubicacion);
        this.ubicacionService.crear(ubicacion2);
        this.ubicacionService.crear(ubicacion3);

        mediumService.crear(medium1);
        mediumService.crear(medium2);
        mediumService.crear(medium3);
        mediumService.crear(medium4);

        espirituService.crear(demonio2);
        espirituService.crear(demonio);
        espirituService.crear(demonio3);
        espirituService.crear(demonio4);

        ubicacionRepository.setTemperatura(ubicacion.getId(),17.8);
        Thread.sleep(750);
        ubicacionRepository.setTemperatura(ubicacion2.getId(),14.8);
        Thread.sleep(750);
        ubicacionRepository.setTemperatura(ubicacion3.getId(),20.8);
        Thread.sleep(750);

        new Thread(() -> {try {bajarTempUb3();} catch (InterruptedException e) {throw new RuntimeException(e);}}).start();

        espirituService.crearPortal(new PortalDemonios(new Coordenada(0.5, 0.5)));

        Thread.sleep(1000);

        espirituService.crearPortal(new PortalDemonios(new Coordenada(0.5, 0.5)));

        Thread.sleep(500);
        new Thread(() -> {try {bajarTempUb2();} catch (InterruptedException e) {throw new RuntimeException(e);}}).start();

        espirituService.crearPortal(new PortalDemonios(new Coordenada(2.5, 0.5)));
        Thread.sleep(50000);

        mediumService.meditar(ubicacion.getId(),0.5,0.5);

        for (int i = 0; i < 10; i++) {
            Thread.sleep(7000);
            ubicacionRepository.setTemperatura(ubicacion3.getId(), getTempUb3());
            ubicacionRepository.setTemperatura(ubicacion2.getId(), getTempUb2());
            ubicacionRepository.setTemperatura(ubicacion.getId(), getTempUb1());
        }

        Thread.sleep(20000);
    }

    private double getTempUb1() {
        return ubicacionRepository.recuperar(ubicacion.getId()).get().getTemperatura();
    }

    private double getTempUb2() {
        return ubicacionRepository.recuperar(ubicacion2.getId()).get().getTemperatura();
    }

    private double getTempUb3() {
        return ubicacionRepository.recuperar(ubicacion3.getId()).get().getTemperatura();
    }

    private void bajarTempUb3() throws InterruptedException {
        double tempActual = 20.8;
        for (int i = 0; i < 7; i++) {
            tempActual -= 0.5 * Math.random();
            ubicacionRepository.setTemperatura(ubicacion3.getId(),tempActual);
            Thread.sleep((long) (7000 * Math.random()));
        }
        for (int i = 0; i < 7; i++) {
            tempActual += (0.6 * Math.random()) * Math.random();
            ubicacionRepository.setTemperatura(ubicacion3.getId(),tempActual);
            Thread.sleep(6000);
        }
    }

    private void bajarTempUb2() throws InterruptedException {
        double tempActual;
        for (int i = 0; i < 4; i++) {
            tempActual = getTempUb2() * (0.3 * Math.random());
            ubicacionRepository.setTemperatura(ubicacion2.getId(),tempActual);
            Thread.sleep((long) (7000 * Math.random()));
        }
        for (int i = 0; i < 4; i++) {
            tempActual = getTempUb2() + (0.6 * Math.random()) * Math.random();
            ubicacionRepository.setTemperatura(ubicacion2.getId(),tempActual);
            Thread.sleep(9000);
        }
    }

    @AfterEach
    void eliminarModelo() {
        testService.eliminarEspiritus();
        testService.eliminarMediums();
        testService.eliminarUbicaciones();
    }
}
