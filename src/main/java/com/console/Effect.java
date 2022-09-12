package com.console;

import java.util.ArrayList;
import java.util.Random;

public class Effect {
    public static ArrayList<Lampe> changeColor = new ArrayList<>();
    public static ArrayList<Lampe> dimmerEffect = new ArrayList<>();

    public static void changeColor() {
        if(changeColor == null) return;
        for (Lampe lampe : changeColor) {
            byte[] randomColor;
            randomColor = new byte[3];
            new Random().nextBytes(randomColor);
            lampe.setRed(randomColor[0]);
            lampe.setGreen(randomColor[1]);
            lampe.setBlue(randomColor[2]);
        }
    }

    public static void dimmerEffect(){
        if(dimmerEffect == null) return;
        for (Lampe lampe:dimmerEffect){
            lampe.setRed((byte) (lampe.getRed() + 1));
            lampe.setGreen((byte) (lampe.getGreen() + 1));
            lampe.setBlue((byte) (lampe.getBlue() + 1));
        }
    }
}