package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.testUtils.TestService;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class EstadisticaServiceImplTest {

    @Autowired
    EstadisticaServiceImpl estadisticaService;

    @Autowired
    private TestService limpiador;
}