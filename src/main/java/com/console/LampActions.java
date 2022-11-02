package com.console;

import com.console.patch.PatchWriter;
import com.console.scene.SceneSettings;
import com.console.scene.Scenes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LampActions {

    private static int matrix = -1;

    /**
     * User Interface for the lamp
     */
    public static void selectLamp() throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("Action Eingeben: [Lamp ID]/ \"scene\" / \"patch\"");
        String selectionString = getLine();
        switch (selectionString) {
            case "scene", "s" -> SceneSettings.sceneSettings();
            case "patch", "p" -> PatchWriter.writePatch();
        }

        int selection = 0;
        try {
            selection = Integer.parseInt(selectionString);
        } catch (Exception e) {
            selectLamp();
        }

        for (Lampe lampe : Main.Lampen) {
            if (selection > Main.getLampen().size()) {
                System.out.println("die Lampe gibt es nicht");
                selectLamp();
            }
            if (lampe.getId() == selection) {
                System.out.println("Lampe gefunden");
                Main.setSelectedLampe(lampe);
                channelData(Main.getSelectedLampe());
                modifyLampe();
            }
        }
    }

    public static void modifyLampe() throws IOException, InterruptedException, ClassNotFoundException {
        while (true) {
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
            String line = reader1.readLine();
            switch (line) {
                case "pan" -> Main.getSelectedLampe().setPan((byte) selectionParameter());
                case "panfein" -> Main.getSelectedLampe().setPanfein((byte) selectionParameter());
                case "tilt" -> Main.getSelectedLampe().setTilt((byte) selectionParameter());
                case "tiltfein" -> Main.getSelectedLampe().setTiltfein((byte) selectionParameter());
                case "speed" -> Main.getSelectedLampe().setSpeed((byte) selectionParameter());
                case "dimmer" -> Main.getSelectedLampe().setDimmer((byte) selectionParameter());
                case "shutter" -> Main.getSelectedLampe().setShutter((byte) selectionParameter());
                case "colorwheel" -> Main.getSelectedLampe().setColorWheel((byte) selectionParameter());
                case "strobo" -> Main.getSelectedLampe().setStrobo((byte) selectionParameter());
                case "fokus" -> Main.getSelectedLampe().setFokus((byte) selectionParameter());
                case "prisma" -> Main.getSelectedLampe().setPrisma((byte) selectionParameter());
                case "matrix", "m" -> matrix = selectionParameter() - 1;
                case "matrixAll", "ma" -> matrix = -1;
                case "red" -> Main.getSelectedLampe().setRed((byte) selectionParameter(), matrix);
                case "green" -> Main.getSelectedLampe().setGreen((byte) selectionParameter(), matrix);
                case "blue" -> Main.getSelectedLampe().setBlue((byte) selectionParameter(), matrix);
                case "white" -> Main.getSelectedLampe().setWhite((byte) selectionParameter(), matrix);
                case "gobo" -> {
                    System.out.println("Gobo Rad:");
                    int goboCount = getIntFormLine() - 1; //lsit starting with 0
                    Main.getSelectedLampe().setGobo((byte) selectionParameter(), goboCount);
                }
                case "e", "effect" -> switchEffect(Main.getSelectedLampe());
                case "i", "info" -> {
                    for (int i = 0; i <= (Main.getSelectedLampe().getChannelName().length - 1); i++) {
                        System.out.print(i + " : " + Main.getSelectedLampe().getChannelData()[i] + " : " + Main.getSelectedLampe().getChannelName()[i] + " | ");
                    }
                    System.out.println();
                }
                case "clear" -> Main.getSelectedLampe().clearLampe();
                case "exit" -> selectLamp();
                case "create" -> SceneSettings.create();
                case "save" -> SceneSettings.saveScene();
                case "select" -> Scenes.setActiveScene(Scenes.select(getLine()));
                case "getSelect" -> System.out.println(Scenes.getActiveScene().getName());
                case "getAllScenes" -> {
                    for (int i = 0; i < Scenes.scenes.size(); i++) {
                        System.out.println(Scenes.scenes.get(i));
                    }
                }
                default -> System.out.println("Falsch geschrieben");
            }
            System.out.println("-");
        }
    }

    private static void channelData(Lampe lampe) throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("Channel auswählen");
        for (int i = 0; i <= (lampe.getChannelName().length - 1); i++) {
            System.out.print(i + " : " + lampe.getChannelData()[i] + " | ");
        }
        System.out.println();
    }

    /**
     * add an effect to the lamp
     *
     * @param lampe
     */
    private static void switchEffect(Lampe lampe) throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("Effect:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String selection = reader.readLine();
        switch (selection) {
            case "data" -> channelData(lampe);
            case "r" -> {
                lampe.setRed((byte) 255, -1);
                lampe.setGreen((byte) 0, -1);
                lampe.setBlue((byte) 0, -1);
            }
            case "g" -> {
                lampe.setRed((byte) 0, -1);
                lampe.setGreen((byte) 255, -1);
                lampe.setBlue((byte) 0, -1);
            }
            case "b" -> {
                lampe.setRed((byte) 0, -1);
                lampe.setGreen((byte) 0, -1);
                lampe.setBlue((byte) 255, -1);
            }
            case "w" -> {
                lampe.setRed((byte) 255, -1);
                lampe.setGreen((byte) 255, -1);
                lampe.setBlue((byte) 255, -1);
            }
            case "white" -> lampe.setWhite((byte) 255, -1);
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
            case "matrix" -> {
                Effect effect = new Effect();
                effect.matrixBounce(lampe);
            }
            case "exit" -> selectLamp();
            default -> System.out.println("Falsch geschrieben");
        }
    }

    /**
     * Gets the parameter in the terminal
     *
     * @return
     * @throws IOException
     */
    private static int selectionParameter() throws IOException {
        System.out.println("Wert auswähle");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            return Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Falsche Eingabe");
            return 0;
        }

    }

    private static String getLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    private static int getIntFormLine() {
        try {
            return Integer.parseInt(getLine());
        } catch (IOException e) {
            return 0;
        }
    }

    public static int getMatrix() {
        return matrix;
    }

    public static void setMatrix(int matrix) {
        LampActions.matrix = matrix;
    }
}
