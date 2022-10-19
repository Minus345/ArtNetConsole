package com.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Effect {
    public static ArrayList<Lampe> changeColor = new ArrayList<>();
    public static ArrayList<Lampe> dimmerEffect = new ArrayList<>();
    public static ArrayList<Lampe> circle = new ArrayList<>();

    public static ArrayList<Lampe> matrixBounce = new ArrayList<>();

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

    public void matrixBounce(Lampe lampe) throws IOException {
        if(matrixBounce.contains(lampe)){
            matrixBounce.remove(lampe);
            lampe.clearLampe();
            return;
        }

        System.out.println("red:");
        byte r = getByteFromLine();
        System.out.println("green:");
        byte g = getByteFromLine();
        System.out.println("blue:");
        byte b = getByteFromLine();
        System.out.println("white:");
        byte w = getByteFromLine();
        System.out.println("time:");
        int time = getIntFromLine();
        System.out.println("Starting");

        matrixBounce.add(lampe);

        Runnable runnable = () -> {
            try {
                while (true) {
                    for (int i = 1; i < lampe.getMatrixCount() + 1; i++) {
                        if(!matrixBounce.contains(lampe)){
                            lampe.clearLampe();
                            Thread.currentThread().interrupt();
                        }

                        lampe.setRed(r, i - 1);
                        lampe.setGreen(g, i - 1);
                        lampe.setBlue(b, i - 1);
                        lampe.setWhite(w, i - 1);

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
                        Thread.sleep(time);
                    }
                }
            } catch (InterruptedException e) {
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private static String getLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    private static byte getByteFromLine() throws IOException {
        try {
            return (byte) Integer.parseInt(getLine());
        }catch (NumberFormatException e){
            System.out.println("Falsche Eingabe");
            return 0;
        }
    }

    private static int getIntFromLine() throws IOException {
        try {
            return Integer.parseInt(getLine());
        }catch (NumberFormatException e){
            System.out.println("Falsche Eingabe");
            return 0;
        }
    }
}