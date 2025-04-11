package ar.edu.unq.epersgeist.modelo.RandomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class RandomGenerator{

    private static final List<Integer> valoresFijos = new ArrayList<>();

    public static void addValorFijo(int valor) {
        valoresFijos.add(valor);
    }

    public static int getRandom(int min, int max) {
        if (!valoresFijos.isEmpty()) {
            int valor = valoresFijos.get(0);

            valoresFijos.remove(0);

            return valor;
        }

        return (int) (Math.random() * (max - min)) + min;
    }

}
