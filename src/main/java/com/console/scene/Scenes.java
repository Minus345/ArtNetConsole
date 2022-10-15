package com.console.scene;

import com.console.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class Scenes {
    public static ArrayList<Scene> scenes = new ArrayList<>();
    public static ArrayList<Scene> runningScenesArray = new ArrayList<>();
    private static Scene activeScene;

    //TODO Scene Löschen

    /**
     * creates the scene with the parameters
     * @param name name
     * @param loop loop true/false
     * @param loopCount if loop is false how often should ist repeat
     */
    public static void createScene(String name, boolean loop, int loopCount) {
        if (loop) loopCount = 10;
        Scene newScene = new Scene(name, loop, loopCount);
        scenes.add(newScene);
        activeScene = newScene;
    }

    /**
     * returns the selected scene
     * @param name scene name
     * @return scene object
     */
    public static Scene select(String name) {
        for (Scene scene : scenes) {
            if (scene.getName().equals(name)) {
                System.out.println("Scene gefunden");
                return scene;
            }
        }
        System.out.println("Keine Scene mit diesem Namen gefunden");
        return null;
    }

    /**
     * Starts the scene with in a new thread
     * @param scene scene object
     */
    public static void startScene(Scene scene) {
        runningScenesArray.add(scene);
        Runnable runnable = () -> {
            try {
                scene.read();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.setName(scene.getName());
        thread.start();

    }

    /**
     * stops the scenes thread
      * @param scene scene object
     */
    public static void stopScene(Scene scene) {
        runningScenesArray.remove(scene);
        Objects.requireNonNull(getThreadByName(scene.getName())).interrupt();
    }

    /**
     * Gets the thread by its name
     * @param threadName name of the thread
     * @return returns the thread object
     */
    private static Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) return t;
        }
        return null;
    }

    /**
     * Saves the scenes object with all the scenes in it
     */
    public static void writeSceneToFile() {
        System.out.println("File wird erstellt");
        try {
            FileOutputStream f = new FileOutputStream(Main.getSceneSavePath());
            ObjectOutputStream o = new ObjectOutputStream(f);
            // Write objects to file
            o.writeObject(scenes);

            o.close();
            f.close();
            //}
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    /**
     * Reads the scenes object from the file
     */
    public static void readSceneFromFile() {
        try {
            FileInputStream fi = new FileInputStream(Main.getSceneSavePath());
            ObjectInputStream oi = new ObjectInputStream(fi);
            // Read objects
            ArrayList<Scene> deserializeScene = (ArrayList<Scene>) oi.readObject();
            for (Scene scene : deserializeScene) {
                scenes.add(scene);
                System.out.println("Scene: " + scene.getName() + " wurde hinzugefügt");
            }

            oi.close();
            fi.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the courant active scene
     */
    public static Scene getActiveScene() {
        return activeScene;
    }

    /**
     * @param activeScene set the active scene
     */
    public static void setActiveScene(Scene activeScene) {
        Scenes.activeScene = activeScene;
    }
}
