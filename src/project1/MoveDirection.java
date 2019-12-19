package project1;

import java.util.Random;

public enum MoveDirection {
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN;

    public static MoveDirection getRandom() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

    @Override
    public String toString() {
        return super.toString();
    }
}


