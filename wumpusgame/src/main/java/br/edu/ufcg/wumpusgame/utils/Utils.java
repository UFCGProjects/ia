package br.edu.ufcg.wumpusgame.utils;

import java.util.Random;

public class Utils {

    static Random mRandom;

    public static int randInt(final int min, final int max) {
        if (mRandom == null) {
            System.err.println("Random seed not configured. You should call configureRandomSeed() before use randInt()");
            System.exit(1);
        }

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        final int randomNum = mRandom.nextInt(max - min + 1) + min;


        return randomNum;
    }

    public static void configureRandomSeed(Long seed) {
        mRandom = new Random(seed);
    }
}
