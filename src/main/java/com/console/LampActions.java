package com.console;

import com.console.patch.PatchWriter;
import com.console.scene.Scene;
import com.console.scene.Scenes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class LampActions {

    private static int matrix = -1;

    /**
     * User Interface for the lamp
     */
    public static void selectLamp() throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("Action Eingeben: [Lamp ID]/ \"scene\" / \"patch\"");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String selectionString = reader.readLine();
        if (Objects.equals(selectionString, "scene")) {
            sceneSettings();
        }
        if (Objects.equals(selectionString, "patch")) {
            PatchWriter.writePatch();
        }
        int selection = 0;
        try {
            selection = Integer.parseInt(selectionString);
        } catch (Exception e) {
            selectLamp();
        }

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
     */
    private static void sceneSettings() throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("Scene: \"start\" / \"stop\" / \"delete\" / \"getActive\" / \"save\" / \"read\" / \"exit\"");
        switch (getLine()) {
            case "start" -> {
                System.out.println("Name:");
                Scene scene = Scenes.select(getLine());
                if (scene == null) {
                    sceneSettings();
                } else {
                    Scenes.startScene(scene);
                }
            }
            case "stop" -> {
                System.out.println("Name:");
                Scene scene = Scenes.select(getLine());
                if (scene == null) {
                    sceneSettings();
                } else {
                    if (Scenes.runningScenesArray.contains(scene)) {
                        Scenes.stopScene(scene);
                    } else {
                        System.out.println("Scene ist gerade nicht aktiv");
                        sceneSettings();
                    }

                }
            }
            case "delete" -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Name:");
                String delete = reader.readLine();
                Scenes.scenes.remove(delete);
                delete = null;
            }
            case "getActiv" -> {
                for (int i = 0; i < Scenes.runningScenesArray.size(); i++) {
                    System.out.println(Scenes.runningScenesArray.get(i).getName());
                }
            }
            case "save" -> Scenes.writeSceneToFile();
            case "read" -> Scenes.readSceneFromFile();
            case "exit" -> selectLamp();
            default -> {
                System.out.println("Falsch Geschrieben");
                sceneSettings();
            }
        }
        sceneSettings();
    }

    public static void modifyLampe(Lampe lampe) throws IOException, InterruptedException, ClassNotFoundException {
        while (true) {
            channelData(lampe);
        }
    }

    /**
     * Set individual parameters per lamp
     */
    private static void channelData(Lampe lampe) throws IOException, InterruptedException, ClassNotFoundException {
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
            case "shutter" -> lampe.setShutter((byte) selectionParameter());
            case "strobo" -> lampe.setStrobo((byte) selectionParameter());
            case "matrix", "m" -> matrix = selectionParameter() - 1;
            case "matrixAll", "ma" -> matrix = -1;
            case "red" -> lampe.setRed((byte) selectionParameter(), matrix);
            case "green" -> lampe.setGreen((byte) selectionParameter(), matrix);
            case "blue" -> lampe.setBlue((byte) selectionParameter(), matrix);
            case "white" -> lampe.setWhite((byte) selectionParameter(), matrix);
            case "gobo" -> {
                System.out.println("Gobo Rad:");
                int goboCount = Integer.parseInt(getLine());
                lampe.setGobo((byte) selectionParameter(), goboCount);
            }
            case "e", "effect" -> switchEffect(lampe);
            case "i", "info" -> {
                for (int i = 0; i <= (lampe.getChannelName().length - 1); i++) {
                    System.out.print(i + " : " + lampe.getChannelData()[i] + " : " + lampe.getChannelName()[i] + " | ");
                }
                System.out.println();
            }
            case "clear" -> lampe.clearLampe();
            case "exit" -> selectLamp();
            case "create" -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Name:"); //String name,boolean loop, int loopCount
                String name = reader.readLine();
                System.out.println("loop: [true/false]");
                boolean loop = Boolean.parseBoolean(reader.readLine());
                int loopCount;
                if (!loop) {
                    System.out.println("loop Anzahl:");
                    loopCount = Integer.parseInt(reader.readLine());
                } else {
                    loopCount = 10;
                }
                Scenes.createScene(name, loop, loopCount);
            }
            case "save" -> {
                if (Scenes.getActiveScene() == null) {
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

                Scenes.getActiveScene().addStep(number, transitionTime, stayTime, dmxData);

            }
            case "select" -> Scenes.setActiveScene(Scenes.select(getLine()));
            case "getSelect" -> System.out.println(Scenes.getActiveScene().getName());
            case "getAllScenes" -> {
                for (int i = 0; i < Scenes.scenes.size(); i++) {
                    System.out.println(Scenes.scenes.get(i));
                }
            }
            default -> System.out.println("Falsch geschrieben");
        }
    }

    /**
     * add an effect to the lamp
     *
     * @param lampe
     * @throws IOException
     * @throws InterruptedException
     */
    private static void switchEffect(Lampe lampe) throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("Effect:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String selection = reader.readLine();
        switch (selection) {
            case "data" -> channelData(lampe);
            case "r" -> {
                lampe.setRed((byte) 255, 0);
                lampe.setGreen((byte) 0, 0);
                lampe.setBlue((byte) 0, 0);
            }
            case "g" -> {
                lampe.setRed((byte) 0, 0);
                lampe.setGreen((byte) 255, 0);
                lampe.setBlue((byte) 0, 0);
            }
            case "b" -> {
                lampe.setRed((byte) 0, 0);
                lampe.setGreen((byte) 0, 0);
                lampe.setBlue((byte) 255, 0);
            }
            case "w" -> {
                lampe.setRed((byte) 255, 0);
                lampe.setGreen((byte) 255, 0);
                lampe.setBlue((byte) 255, 0);
            }
            case "white" -> lampe.setWhite((byte) 255, 0);
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
                if (Effect.matrixBounce.contains(lampe)) {
                    Effect.matrixBounce.remove(lampe);
                } else {
                    Effect.matrixBounce.add(lampe);
                }
                System.out.println("red:");
                byte r = Byte.parseByte(getLine());
                System.out.println("green:");
                byte g = Byte.parseByte(getLine());
                System.out.println("blue:");
                byte b = Byte.parseByte(getLine());
                System.out.println("white:");
                byte w = Byte.parseByte(getLine());
                System.out.println("Starting");
                Runnable runnable = () -> {
                    try {
                        Effect.matrixBounce(r, g, b, w);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
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
        return Integer.parseInt(reader.readLine());
    }

    /**
     * reads the courant line in the terminal
     *
     * @return
     * @throws IOException
     */
    public static String getLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    public static int getMatrix() {
        return matrix;
    }
}
