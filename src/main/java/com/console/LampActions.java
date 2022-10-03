package com.console;

import com.console.scene.Scene;
import com.console.scene.Scenes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LampActions {
    /**
     * User Interface for the lamp
     * @throws IOException
     * @throws InterruptedException
     */
    public static void selectLamp() throws IOException, InterruptedException {
        System.out.println("Lampen Id eigeben");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int selection = Integer.parseInt(reader.readLine());

        if (selection == 0) {
            sceneSettings();
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
            }
        }
    }

    /**
     * User Interface for starting and stopping scenes
     * @throws IOException
     * @throws InterruptedException
     */
    private static void sceneSettings() throws IOException, InterruptedException {
        System.out.println("Scene");
        switch (getLine()){
            case "start" ->{
                System.out.println("Name:");
                Scene scene = Scenes.select(getLine());
                if (scene == null){
                    selectLamp();
                }else {
                    Scenes.startScene(scene);
                }
            }
            case "stop" ->{
                System.out.println("Name:");
                Scene scene = Scenes.select(getLine());
                if (scene == null){
                    selectLamp();
                }else {
                    Scenes.stopScene(scene);
                }
            }
            default -> {
                System.out.println("Falsch Geschrieben");
                selectLamp();
            }
        }
        selectLamp();
    }

    public static void modifyLampe(Lampe lampe) throws IOException, InterruptedException {
        while (true) {
            channelData(lampe);
        }
    }

    /**
     * Set individual parameters per lamp
     * @param lampe
     * @throws IOException
     * @throws InterruptedException
     */
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
                System.out.println("Name:"); //String name,boolean loop, int loopCount
                String name = reader.readLine();
                System.out.println("loop: [true/false]");
                boolean loop = Boolean.parseBoolean(reader.readLine());
                System.out.println("loop Anzahl:");
                int loopCount = Integer.parseInt(reader.readLine());
                Scenes.createScene(name, loop, loopCount);
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
            case "select" -> Scenes.setActivScene(Scenes.select(getLine()));

            case "getSelect" -> System.out.println(Scenes.getActivScene().getName());
            case "getAllScenes" -> {
                for (int i = 0; i < Scenes.scenes.size(); i++) {
                    System.out.println(Scenes.scenes.get(i));
                }
            }
            default -> System.out.println("Falsch geschrieben");
        }
    }

    /**
     * Gets the parameter in the terminal
     * @return
     * @throws IOException
     */
    private static int selectionParameter() throws IOException {
        System.out.println("Wert auswähle");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return Integer.parseInt(reader.readLine());
    }

    /**
     * add an effect to the lamp
     * @param lampe
     * @throws IOException
     * @throws InterruptedException
     */
    private static void switchEffect(Lampe lampe) throws IOException, InterruptedException {
        System.out.println("Effect:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String selection = reader.readLine();
        switch (selection) {
            case "data" -> channelData(lampe);
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

    /**
     * reads the courant line in the terminal
     * @return
     * @throws IOException
     */
    public static String getLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

}
