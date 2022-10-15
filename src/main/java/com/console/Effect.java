package com.console;

import java.util.ArrayList;
import java.util.Random;

public class Effect {
    public static ArrayList<Lampe> changeColor = new ArrayList<>();
    public static ArrayList<Lampe> dimmerEffect = new ArrayList<>();
    public static ArrayList<Lampe> circle = new ArrayList<>();
    public static ArrayList<Lampe> matrixBounce = new ArrayList<>();

    private static int i;

    public static void changeColor() {
        if (changeColor == null) return;
        for (Lampe lampe : changeColor) {
            byte[] randomColor;
            randomColor = new byte[3];
            new Random().nextBytes(randomColor);
            lampe.setRed(randomColor[0], 0);
            lampe.setGreen(randomColor[1], 0);
            lampe.setBlue(randomColor[2], 0);
        }
    }

    public static void dimmerEffect() {
        if (dimmerEffect == null) return;
        for (Lampe lampe : dimmerEffect) {
            lampe.setDimmer((byte) (lampe.getDimmer() + 1));
        }
    }

    public static void circle() throws InterruptedException {
        for (int i = 0; i <= 356; i++) {
            for (Lampe lampe : circle) {
                lampe.setPan((byte) (64 * Math.cos(i)));
                lampe.setTilt((byte) (64 * Math.sin(i)));
            }
            Thread.sleep(100);
        }
    }

    public static void matrixBounce(byte red, byte green, byte blue, byte white) throws InterruptedException {
        while (true) {
            for (Lampe lampe : matrixBounce) {
                for (int i = 1; i < lampe.getMatrixCount() + 1; i++) {
                    lampe.setRed(red, i - 1);
                    lampe.setGreen(green, i - 1);
                    lampe.setBlue(blue, i - 1);
                    lampe.setWhite(white, i - 1);

                    if (i - 2 != -1) {
                        lampe.setRed((byte) 0, i - 2);
                        lampe.setGreen((byte) 0, i - 2);
                        lampe.setBlue((byte) 0, i - 2);
                        lampe.setWhite((byte) 0, i - 2);
                    } else {
                        lampe.setRed((byte) 0, lampe.getMatrixCount() - 1);
                        lampe.setGreen((byte) 0, lampe.getMatrixCount() - 1);
                        lampe.setBlue((byte) 0, lampe.getMatrixCount() - 1);
                        lampe.setWhite((byte) 0, lampe.getMatrixCount() - 1);
                    }
                    Thread.sleep(1000);
                    if(matrixBounce.size() == 0){
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}