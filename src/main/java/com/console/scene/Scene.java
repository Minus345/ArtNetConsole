package com.console.scene;

import com.console.SendArtNet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

public class Scene {
    private final String name;
    private final int loopCount;
    private final boolean loop;
    private final ArrayList<Step> stepList = new ArrayList<>();

    public Scene(String name, boolean loop, int loopCount) {
        this.name = name;
        this.loop = loop;
        this.loopCount = loopCount;
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
        int[] dmxResult = new int[512];

        double y;
        double x1 = 0; // x startwert 0

        for (int l = 0; l < loopCount; l++) { //wiederholungen
            if (loop) l = 1; //loop

            int[] dmxStart = new int[512];
            int[] dmxToFade = new int[512];
            double x2;
            int a = step;
            int b = step;
            int c = step;

            if(step == stepList.size()){
                step = 0;
            }

            if (step == (stepList.size() - 1)) { //------------Fehler  bei der zweiten ausführung
                a = stepList.size() - 1;
                b = 0;
                c = stepList.size() - 1;
            } else {
                a = step;
                b = step + 1;
                c = step;
            }

            for (int i = 0; i < 512; i++) {
                dmxStart[i] = Byte.toUnsignedInt(stepList.get(a).getDmxStep()[i]);
            }
            for (int i = 0; i < 512; i++) {
                dmxToFade[i] = Byte.toUnsignedInt(stepList.get(b).getDmxStep()[i]);
            }
            x2 = (int) (stepList.get(c).getTransitionTime() / 0.025);

            for (int x = 0; x <= x2; x++) { //übergangszeit
                for (int i = 0; i < 512; i++) { //alle 512 channel
                    double dmxToFadeDouble = dmxToFade[i];
                    double dmxStartDouble = dmxStart[i];
                    double m = (dmxToFadeDouble - dmxStartDouble) / (x2 - x1); // y2 - y1 / x2 - x1
                    double t = dmxStart[i] - m * x1;  // t = y-m*x

                    y = m * x + t;
                    dmxResult[i] = (int) Math.round(y);
                }
                SendArtNet.sendScene(dmxResult);
                Thread.sleep(25);
            }
            Thread.sleep(stepList.get(step).getStayTime() * 1000L);
            step++;

        }
        Scenes.setSceneActiv(false);
    }

    public String getName() {
        return name;
    }
}
