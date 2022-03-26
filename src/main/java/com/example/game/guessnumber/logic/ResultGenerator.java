package com.example.game.guessnumber.logic;

import java.util.Random;

public interface ResultGenerator {
    int generate();

    class RandomGenerator implements ResultGenerator {
        @Override
        public int generate() {
            return new Random().nextInt(10) + 1;
        }
    }
}
