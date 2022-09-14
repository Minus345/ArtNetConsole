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
            }else{
                System.out.println("Lampe nicht gefunden");
            }
        }
    }
    //Effect auswahl über String in switch Effect
    public static void modifyLampe(Lampe lampe) throws IOException {
        while (true) {
            System.out.println("Effect auswählen");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String selection = reader.readLine();

            switchEffect(selection,lampe);
        }
    }

    private static void switchEffect(String selection,Lampe lampe) throws IOException {
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

    private static void channelData(Lampe lampe) throws IOException {
        System.out.println("Channel auswählen");
        for (int i = 0; i <= (lampe.getChannelName().length - 1); i++){
            System.out.println(i + " : " + lampe.getChannelName()[i]);
        }
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
        String line = reader1.readLine();
        switch (line){
            case "pan" -> lampe.setPan((byte) selectionParameter());
        }
    }

    private static int selectionParameter() throws IOException {
        System.out.println("Wert auswähle");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return Integer.parseInt(reader.readLine());
    }
}
