package ar.edu.unq.epergeist.testUtils;

import ar.edu.unq.epersgeist.modelo.RandomGenerator.RandomStrategy;

import java.util.ArrayList;
import java.util.List;

public class FakeRandomStrategyImpl implements RandomStrategy {

    private final List<Integer> valoresFijos = new ArrayList<>();

    public FakeRandomStrategyImpl(){}

    public void addValorFijo(Integer valor){
        this.valoresFijos.add(valor);
    }

    @Override
    public int getRandom(int min, int max) {
        int valorFijo = valoresFijos.getFirst();
        valoresFijos.removeFirst();

        return valorFijo;
    }

    public void cleanUp(){
        valoresFijos.clear();
    }
}
