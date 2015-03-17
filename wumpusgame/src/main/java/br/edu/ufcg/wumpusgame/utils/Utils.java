package br.edu.ufcg.wumpusgame.utils;

import java.util.Random;

public class Utils {

    public static int randInt(final int min, final int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        final Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        final int randomNum = rand.nextInt(max - min + 1) + min;

        return randomNum;
    }
}
