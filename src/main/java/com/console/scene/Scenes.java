package com.console.scene;

import java.util.ArrayList;

public class Scenes {
    public static ArrayList<Scene> scenes = new ArrayList<>();
    private static boolean sceneActiv;
    private static Scene activScene;

    public static void createScene(String name, boolean loop, int loopCount) {
        if(loop) loopCount = 10;
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
