package com.console.scene;

import java.util.ArrayList;
import java.util.Objects;

public class Scenes {
    public static ArrayList<Scene> scenes = new ArrayList<>();
    public static ArrayList<Scene> runningScenesArray = new ArrayList<>();
    private static boolean sceneActiv;
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

    public static void manageScene(){
        for (Scene runningScene : runningScenesArray) {
            Runnable runnable = () -> {
                try {
                    runningScene.read();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            Thread thread = new Thread(runnable);
            thread.setName(runningScene.getName());
            thread.start();
        }
    }

    public static void startScene(Scene scene) {
        runningScenesArray.add(scene);
        Scenes.setSceneActiv(true);
        manageScene();
    }

    public static void stopScene(Scene scene) {
        runningScenesArray.remove(scene);
        Objects.requireNonNull(getThreadByName(scene.getName())).stop();
        Scenes.setSceneActiv(false);
    }

    public static Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) return t;
        }
        return null;
    }

    public static boolean isSceneActiv() {
        return sceneActiv;
    }

    public static void setSceneActiv(boolean sceneActiv) {
        Scenes.sceneActiv = sceneActiv;
    }

    public static Scene getActivScene() {
        return activScene;
    }

    public static void setActivScene(Scene activScene) {
        Scenes.activScene = activScene;
    }
}
