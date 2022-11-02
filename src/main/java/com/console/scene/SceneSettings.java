package com.console.scene;

import com.console.LampActions;
import com.console.Lampe;
import com.console.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SceneSettings {
    /**
     * User Interface for starting, stopping, delete , getActive, save, read, savescene, select, getSelect, getAllScenes  scenes
     */
    public static void sceneSettings() throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("Scene: \"start\" / \"stop\" / \"delete\" / \"getActive\" / \"save\" / \"read\" / \"exit\"");
        switch (getLine()) {
            case "start" -> start();
            case "stop" -> stop();
            case "delete" -> delete();
            case "getActiv" -> {
                for (int i = 0; i < Scenes.runningScenesArray.size(); i++) {
                    System.out.println(Scenes.runningScenesArray.get(i).getName());
                }
            }
            case "save" -> Scenes.writeSceneToFile();
            case "read" -> Scenes.readSceneFromFile();
            case "create" -> create();
            case "savescene" -> saveScene();
            case "select" -> Scenes.setActiveScene(Scenes.select(getLine()));
            case "getSelect" -> System.out.println(Scenes.getActiveScene().getName());
            case "getAllScenes" -> {
                for (int i = 0; i < Scenes.scenes.size(); i++) {
                    System.out.println(Scenes.scenes.get(i));
                }
            }
            case "exit" -> LampActions.selectLamp();
            default -> {
                System.out.println("Falsch Geschrieben");
                sceneSettings();
            }
        }
        sceneSettings();
    }

    /**
     * creates a new scene
     */
    public static void create() throws IOException {
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

    /**
     * saves the courant selected scene
     */
    public static void saveScene() throws IOException {
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

    /**
     * deletes the courant selected scene
     */
    private static void delete() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Name:");
        String delete = reader.readLine();
        Scenes.scenes.remove(delete);
        delete = null;
    }

    /**
     * stops the courant selected scene
     */
    private static void stop() throws IOException, InterruptedException, ClassNotFoundException {
        if (Scenes.runningScenesArray.isEmpty()) {
            System.out.println("Keine Szenen da, die man stoppen kann");
            return;
        }
        System.out.print("Name: ");
        for (Scene scene : Scenes.runningScenesArray) {
            System.out.println(scene.getName() + " ");
        }


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

    /**
     * starts a scene
     * user input in terminal
     */
    private static void start() throws IOException, InterruptedException, ClassNotFoundException {
        System.out.print("Name: ");
        for (Scene scene : Scenes.scenes) {
            System.out.println(scene.getName() + " ");
        }
        Scene scene = Scenes.select(getLine());
        if (scene == null) {
            sceneSettings();
        } else {
            Scenes.startScene(scene);
        }
    }

    /**
     * reads the courant line in the terminal
     *
     * @return the courant line in the terminal
     */
    private static String getLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }
}
