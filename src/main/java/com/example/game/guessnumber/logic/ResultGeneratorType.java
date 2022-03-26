package com.example.game.guessnumber.logic;

public enum ResultGeneratorType {
    STATIC(() -> 7),
    RANDOM(new ResultGenerator.RandomGenerator());
    private final ResultGenerator generator;

    ResultGeneratorType(ResultGenerator generator) {
        this.generator = generator;
    }

    public ResultGenerator getGenerator() {
        return generator;
    }
}
