package com.console.scene;

import com.console.SendArtNet;

import java.util.*;

public class Scene {
    private final String name;
    private int loop = 0;
    private final ArrayList<Step> stepList = new ArrayList<Step>();

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

    public void read() throws InterruptedException {
        sortList();

        int step = 0;

        byte[] dmxStart = new byte[512];
        dmxStart = stepList.get(step).getDmxStep();

        byte[] dmxToFade = new byte[512];
        dmxToFade = stepList.get(step + 1).getDmxStep();

        byte[] dmxResult = new byte[512];
        for (int l = 0; l < loop; l++) { //wiederholungen
            while (!(stepList.get(step + 1) == null)) { //solange noch schritte da sind
                double y;
                double x1 = 0; // x startwert 0
                double x2 = (int) (stepList.get(step).getTransitionTime() / 0.025);
                for (int x = 0; x < x2; x++) { //übergangszeit
                    for (int i = 0; i < 512; i++) { //alle 512 channel
                        double dmxToFadeDouble = dmxToFade[i];
                        double dmxStartDouble = dmxStart[i];
                        double m = (dmxToFadeDouble - dmxStartDouble) / (x2 - x1); // y2 - y1 / x2 - x1
                        double t = dmxStart[i] - m * x1;  // t = y-m*x

                        y = m * x + t;
                        dmxResult[i] = (byte) Math.round(y);
                    }
                    SendArtNet.sendScene(dmxResult);
                    Thread.sleep(25);
                }
                Thread.sleep(stepList.get(step).getStayTime());
                step++;
            }
        }
        Scenes.setSceneActiv(false);
    }

    public String getName() {
        return name;
    }
}
