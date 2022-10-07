package com.console.scene;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class Scenes {
    public static ArrayList<Scene> scenes = new ArrayList<>();
    public static ArrayList<Scene> runningScenesArray = new ArrayList<>();
    private static Scene activScene;

    public static void createScene(String name, boolean loop, int loopCount) {
        if (loop) loopCount = 10;
        Scene newScene = new Scene(name, loop, loopCount);
        scenes.add(newScene);
        activScene = newScene;
    }

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

    public static void stopScene(Scene scene) {
        runningScenesArray.remove(scene);
        Objects.requireNonNull(getThreadByName(scene.getName())).stop();
    }

    public static Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) return t;
        }
        return null;
    }

    public static void writeSceneToFile() {
        System.out.println("File wird erstellt");
        try {
            for (int i = 0; i < scenes.size(); i++) {
                FileOutputStream f = new FileOutputStream(scenes.get(i).getName() + ".lito");
                ObjectOutputStream o = new ObjectOutputStream(f);

                // Write objects to file
                o.writeObject(scenes);

                o.close();
                f.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    public static void readSceneFromFile() {
        try {
            FileInputStream fi = new FileInputStream("C:\\Users\\max\\Documents\\ArtNetConsole\\src\\main\\resources\\scenes\\1.lito");
            ObjectInputStream oi = new ObjectInputStream(fi);
            // Read objects
            ArrayList<Scene> deserializeScene = (ArrayList<Scene>) oi.readObject();
            for (int i = 0; i < deserializeScene.size(); i++) {
                System.out.println(deserializeScene.get(i).getName());
                scenes.add(deserializeScene.get(i));
                System.out.println("Scene: " + deserializeScene.get(i).getName() + " wurde hinzugefügt");
            }

            oi.close();
            fi.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Scene getActivScene() {
        return activScene;
    }

    public static void setActivScene(Scene activScene) {
        Scenes.activScene = activScene;
    }
}
