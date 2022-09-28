package com.console;

import com.console.midi.MidiInputReceiver;
import com.console.scene.ScenesReadAndWrite;

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

        if(selection == 0){
            System.out.println("reading scene");
            ScenesReadAndWrite.read();
            selectLamp();
            return;
        }

        for (Lampe lampe : Main.Lampen) {
            if (selection > Main.getLampen().size()) {
                System.out.println("die Lampe gibt es nicht");
                selectLamp();
            }
            if (lampe.getId() == selection) {
                System.out.println("Lampe gefunden");
                Main.setSelectedLampe(lampe);
                modifyLampe(lampe);
            } else {
                //System.out.println("Lampe nicht gefunden");
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
            case "tilt" -> lampe.setTilt((byte) selectionParameter());
            case "tiltfein" -> lampe.setTiltfein((byte) selectionParameter());
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
            case "midi" -> {

            }
            case "exit" -> selectLamp();
            case "save" -> ScenesReadAndWrite.saveScene();
            case "write" -> ScenesReadAndWrite.write();
            default -> System.out.println("Falsch geschrieben");
        }
    }

    private static int selectionParameter() throws IOException {
        System.out.println("Wert auswähle");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return Integer.parseInt(reader.readLine());
    }

    private static void switchEffect(Lampe lampe) throws IOException {
        System.out.println("Effect:");
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
            case "changeColor" -> {
                if (Effect.changeColor.contains(lampe)) {
                    Effect.changeColor.remove(lampe);
                }else {
                    Effect.changeColor.add(lampe);
                }
            }
            case "dimmerEffect" ->{
                if (Effect.dimmerEffect.contains(lampe)){
                    Effect.dimmerEffect.remove(lampe);
                }else{
                    Effect.dimmerEffect.add(lampe);
                }
            }
            case "circle" ->{
                if (Effect.circle.contains(lampe)){
                    Effect.circle.remove(lampe);
                }else{
                    Effect.circle.add(lampe);
                }
            }
            case "exit" -> selectLamp();
            case "clear" -> lampe.clearLampe();
            default -> System.out.println("Falsch geschrieben");
        }
    }
}
