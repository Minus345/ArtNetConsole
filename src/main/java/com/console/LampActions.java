package com.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;

public class LampActions {
    //Lampen Auswahl über int
    public static void selectLamp() throws IOException {
        System.out.println("Lampen Id eigeben");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int selection = Integer.parseInt(reader.readLine());

        Iterator<Lampe> iterator = Main.Lampen.iterator();
        while (iterator.hasNext()) {
            Lampe lampe = iterator.next();
            if (lampe.getId() == selection) {
                System.out.println("Lampe gefunden");
                modifyLampe(lampe);
                Main.setSelectedLampe(lampe);
            } else {
                System.out.println("Lampe nicht gefunden");
            }
        }
    }

    //Effect auswahl über String in switch Effect
    public static void modifyLampe(Lampe lampe) throws IOException {
        while (true) {
            channelData(lampe);
        }
    }

    //Set Channell individual
    private static void channelData(Lampe lampe) throws IOException {
        System.out.println("Channel auswählen");
        for (int i = 0; i <= (lampe.getChannelName().length - 1); i++) {
            System.out.print(i + " : " + lampe.getChannelData()[i] + " | ");
        }
        System.out.println();

        BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
        String line = reader1.readLine();
        switch (line) {
            case "pan" -> lampe.setPan((byte) selectionParameter());
            case "panfein" -> lampe.setPanfein((byte) selectionParameter());
            case "til" -> lampe.setTilt((byte) selectionParameter());
            case "tilfein" -> lampe.setTiltfein((byte) selectionParameter());
            case "speed" -> lampe.setSpeed((byte) selectionParameter());
            case "dimmer" -> lampe.setDimmer((byte) selectionParameter());
            case "strobo" -> lampe.setStrobo((byte) selectionParameter());
            case "red" -> lampe.setRed((byte) selectionParameter());
            case "green" -> lampe.setGreen((byte) selectionParameter());
            case "blue" -> lampe.setBlue((byte) selectionParameter());
            case "white" -> lampe.setWhite((byte) selectionParameter());
            case "e", "effect" -> switchEffect(lampe);
            case "i", "info" -> {
                for (int i = 0; i <= (lampe.getChannelName().length - 1); i++) {
                    System.out.print(i + " : " + lampe.getChannelData()[i] + " : " + lampe.getChannelName()[i] + " | ");
                }
                System.out.println();
            }
            default -> System.out.println("Falsch geschrieben");
        }
    }

    private static int selectionParameter() throws IOException {
        System.out.println("Wert auswähle");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return Integer.parseInt(reader.readLine());
    }

    private static void switchEffect(Lampe lampe) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String selection = reader.readLine();
        switch (selection) {
            case "data" -> {
                channelData(lampe);
            }
            case "r" -> {
                lampe.setRed((byte) 255);
                lampe.setGreen((byte) 0);
                lampe.setBlue((byte) 0);
            }
            case "g" -> {
                lampe.setRed((byte) 0);
                lampe.setGreen((byte) 255);
                lampe.setBlue((byte) 0);
            }
            case "b" -> {
                lampe.setRed((byte) 0);
                lampe.setGreen((byte) 0);
                lampe.setBlue((byte) 255);
            }
            case "w" -> {
                lampe.setRed((byte) 255);
                lampe.setGreen((byte) 255);
                lampe.setBlue((byte) 255);
            }
            case "white" -> lampe.setWhite((byte) 255);
            case "dimmer" -> lampe.setDimmer((byte) 255);
            case "changeColor" -> Effect.changeColor.add(lampe);
            case "dimmerEffect" -> Effect.dimmerEffect.add(lampe);
            case "exit" -> selectLamp();
            case "clear" -> lampe.clearLampe();
            default -> System.out.println("Falsch geschrieben");
        }
    }
}
