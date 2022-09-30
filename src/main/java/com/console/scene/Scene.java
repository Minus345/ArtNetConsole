package com.console.scene;

import com.console.SendArtNet;

import java.util.*;

public class Scene {
    private String name;
    private ArrayList<Step> stepList = new ArrayList<Step>();

    public Scene(String name) {
        this.name = name;
    }

    public void addStep(int number, int transitionTime, int stayTime, byte[] dmxStep) {
        Step step = new Step(number, transitionTime, stayTime, dmxStep);
        stepList.add(step);
    }

    public void sortList() {
        stepList.sort(Comparator.comparingInt(Step::getNumber));
    }

    public void readStep() throws InterruptedException {
        sortList();
        byte[] dmxStart = new byte[512];
        dmxStart = stepList.get(0).getDmxStep();

        byte[] dmxToFade = new byte[512];
        dmxToFade = stepList.get(1).getDmxStep();

        byte[] dmxResult = new byte[512];

        int y = 0;

        int x1 = 0; // x startwert 0
        int x2 = (int) (stepList.get(0).getTransitionTime()/0.025);
        for (int x = 0; x < x2; x++) {
            for (int i = 0; i < 512; i++) {
                int m = (dmxToFade[i] - dmxStart[i]) / (x2 - x1); // y2 - y1 / x2 - x1
                int t = dmxStart[i] - m * x1;  // t = y-m*x

                y = m * x + t;
                dmxResult[i] = (byte) y;
            }
            SendArtNet.sendScene(dmxResult);
            Thread.sleep(25);
        }
        Scenes.setSceneActiv(false);
    }

    public String getName() {
        return name;
    }
}
