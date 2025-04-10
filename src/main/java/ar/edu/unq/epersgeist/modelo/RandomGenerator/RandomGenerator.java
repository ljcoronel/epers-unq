package ar.edu.unq.epersgeist.modelo.RandomGenerator;

import java.util.Random;

public class RandomGenerator{

    private Random random;

    private Integer  valorFijo =null;

    public RandomGenerator() {
        this.random = new Random();
    }

    public RandomGenerator(long seed){
        this.random = new Random(seed);
    }

    public void setValorFijo(int valor) {
        this.valorFijo = valor;
    }

    public void eliminarValorFijo() {
        this.valorFijo = null;
    }

    public int getRandom(int min, int max) {
        if (valorFijo != null) {
            return valorFijo;
        }
        return random.nextInt(max - min + 1) + min;
    }
}
