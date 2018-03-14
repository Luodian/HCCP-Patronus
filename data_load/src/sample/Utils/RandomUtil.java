/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved. DO NOT
 * ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */
package sample.Utils;


import java.util.Random;

import static java.lang.Math.random;
import static java.lang.Math.round;

public class RandomUtil {

    private static Random rand = new Random();

    public static double getRandom(double variation) {
        return (random() * variation * 2 + 1 - variation);
    }

    public static double getGaussianRandom(double mean, double deviation) {
        return mean + deviation * rand.nextGaussian();
    }

    public static double getGaussianRandom(double from, double to, double mean, double deviation) {
        double result = 0;
        do {
            result = getGaussianRandom(mean, deviation);
        } while (result < from || result > to);
        return result;
    }

    public static int getRandomIndex(int from, int to) {
        return (int) round(random() * (to - from)) + from;
    }
}
