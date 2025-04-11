package ar.edu.unq.epergeist.modelo;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MediumTest {
    private Ubicacion ubicacion = new Ubicacion("Washington");
    private final Angel espiritu = new Angel(79, "Poltergeist", ubicacion);
    private final Angel espiritu1 = new Angel(60, "Fantasma", ubicacion);
    Medium medium;

    @BeforeEach
    void crearMedium() {
        medium = new Medium("Francisco", 50, 30, ubicacion);
//        espiritu.
    }



}