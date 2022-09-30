package com.console;

import com.console.scene.Scene;
import com.console.scene.Scenes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LampActions {
    //Lampen Auswahl über int
    public static void selectLamp() throws IOException, InterruptedException {
        System.out.println("Lampen Id eigeben");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int selection = Integer.parseInt(reader.readLine());

        if (selection == 0) {
            System.out.println("reading scene");
            startScene();
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
    public static void modifyLampe(Lampe lampe) throws IOException, InterruptedException {
        while (true) {
            channelData(lampe);
        }
    }

    //Set Channell individual
    private static void channelData(Lampe lampe) throws IOException, InterruptedException {
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
            case "exit" -> selectLamp();
            case "create" -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Name:");
                Scenes.createScene(reader.readLine());
            }
            case "save" -> {
                if (Scenes.getActivScene() == null) {
                    System.out.println("keine Scene ausgewält");
                    return;
                }

                System.out.println("Nummer: ");
                int number = Integer.parseInt(getLine());
                System.out.println("Übergangszeit: ");
                int transitionTime = Integer.parseInt(getLine());
                System.out.println("Wartezeit: ");
                int stayTime = Integer.parseInt(getLine());

                byte[] dmxData = new byte[512];
                int positionDmxData = 0;

                for (Lampe alampe : Main.getLampen()) {
                    System.arraycopy(alampe.getDmx(), 0, dmxData, positionDmxData, alampe.getChannel());
                    positionDmxData = positionDmxData + alampe.getChannel();
                }

                Scenes.getActivScene().addStep(number, transitionTime, stayTime, dmxData);

            }
            case "select" -> {
                Scenes.setActivScene(Scenes.select(getLine()));
            }
            case "getSelect" -> {
                System.out.println(Scenes.getActivScene().getName());
            }
            case "getAllScenes" -> {
                for (int i = 0; i < Scenes.scenes.size(); i++) {
                    System.out.println(Scenes.scenes.get(i));
                }
            }
            default -> System.out.println("Falsch geschrieben");
        }
    }

    private static int selectionParameter() throws IOException {
        System.out.println("Wert auswähle");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return Integer.parseInt(reader.readLine());
    }

    private static void switchEffect(Lampe lampe) throws IOException, InterruptedException {
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
                } else {
                    Effect.changeColor.add(lampe);
                }
            }
            case "dimmerEffect" -> {
                if (Effect.dimmerEffect.contains(lampe)) {
                    Effect.dimmerEffect.remove(lampe);
                } else {
                    Effect.dimmerEffect.add(lampe);
                }
            }
            case "circle" -> {
                if (Effect.circle.contains(lampe)) {
                    Effect.circle.remove(lampe);
                } else {
                    Effect.circle.add(lampe);
                }
            }
            case "exit" -> selectLamp();
            case "clear" -> lampe.clearLampe();
            default -> System.out.println("Falsch geschrieben");
        }
    }

    public static String getLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    public static void startScene() throws InterruptedException, IOException {
        System.out.println("Name:");
        Scene scene = Scenes.select(getLine());
        Scenes.setSceneActiv(true);
        Runnable runnable = () -> {
            try {
                assert scene != null;
                scene.read();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

}
