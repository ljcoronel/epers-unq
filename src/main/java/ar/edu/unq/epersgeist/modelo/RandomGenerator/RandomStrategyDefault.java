package ar.edu.unq.epersgeist.modelo.RandomGenerator;

public class RandomStrategyDefault implements RandomStrategy {

    @Override
    public int getRandom(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }
}
