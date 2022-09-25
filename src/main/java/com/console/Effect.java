package com.console;

import java.util.ArrayList;
import java.util.Random;

public class Effect {
    public static ArrayList<Lampe> changeColor = new ArrayList<>();
    public static ArrayList<Lampe> dimmerEffect = new ArrayList<>();
    public static ArrayList<Lampe> circle = new ArrayList<>();

    private static int i;

    public static void changeColor() {
        if (changeColor == null) return;
        for (Lampe lampe : changeColor) {
            byte[] randomColor;
            randomColor = new byte[3];
            new Random().nextBytes(randomColor);
            lampe.setRed(randomColor[0]);
            lampe.setGreen(randomColor[1]);
            lampe.setBlue(randomColor[2]);
        }
    }

    public static void dimmerEffect() {
        if (dimmerEffect == null) return;
        for (Lampe lampe : dimmerEffect) {
            lampe.setDimmer((byte) (lampe.getDimmer() + 1));
        }
    }

    public static void circle() throws InterruptedException {
        for (int i = 0; i <= 356; i ++) {
            for (Lampe lampe : circle) {
                lampe.setPan((byte) (64 * Math.cos(i)));
                lampe.setTilt((byte) (64 * Math.sin(i)));
            }
            Thread.sleep(100);
        }
    }
}