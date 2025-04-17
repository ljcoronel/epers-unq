package ar.edu.unq.epersgeist.modelo.RandomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGenerator{

    private RandomStrategy strategy;
    private static  RandomGenerator uniqueInstance;

    public static RandomGenerator getInstance(){
        if(uniqueInstance == null){
            uniqueInstance =  new RandomGenerator(new RandomStrategyDefault());
        }
        return uniqueInstance;
    }


    private RandomGenerator(RandomStrategy strategy){
        this.strategy = strategy;
    }

    public void setStrategy(RandomStrategy strategy){
        this.strategy = strategy;
    }

    public int getRandom(int min, int max){
        return this.strategy.getRandom(min,max);
    }


}
