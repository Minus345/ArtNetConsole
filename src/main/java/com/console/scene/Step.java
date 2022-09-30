package com.console.scene;

public class Step {
    private int number;
    private int transitionTime;
    private int stayTime;
    private byte[] dmxStep = new byte[512];

    public Step(int number, int transitionTime, int stayTime, byte[] dmxStep) {
        this.number = number;
        this.transitionTime = transitionTime;
        this.stayTime = stayTime;
        this.dmxStep = dmxStep;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTransitionTime() {
        return transitionTime;
    }

    public void setTransitionTime(int transitionTime) {
        this.transitionTime = transitionTime;
    }

    public int getStayTime() {
        return stayTime;
    }

    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
    }

    public byte[] getDmxStep() {
        return dmxStep;
    }

    public void setDmxStep(byte[] dmxStep) {
        this.dmxStep = dmxStep;
    }
}
