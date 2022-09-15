package com.console;

public class Lampe {
    private int id;
    private String name;
    private int channel;
    private String[] channelData = new String[channel];
    private String[] channelName = new String[channel];
    private byte red;
    private byte green;
    private byte blue;
    private byte white;
    private byte pan;
    private byte panfein;
    private byte tilt;
    private byte tiltfein;
    private byte dimmer;
    private byte speed;
    private byte strobo;
    private final byte[] dmx;

    public Lampe(int id, String name, int channel, String[] channelData,String[] channelName ) {
        this.id = id;
        this.name = name;
        this.channel = channel;
        this.channelData = channelData;
        this.channelName = channelName;
        dmx = new byte[channel];
    }

    public void setDmx() {
        //System.out.println(channelData.length);
        for (int i = 0; i <= (channelData.length - 1); i++) {
            //System.out.println(i);
            switch (channelData[i]) {
                case "pan" -> dmx[i] = pan;
                case "tilt" -> dmx[i] = tilt;
                case "dimmer" -> dmx[i] = dimmer;
                case "red" -> dmx[i] = red;
                case "green" -> dmx[i] = green;
                case "blue" -> dmx[i] = blue;
                case "speed" -> dmx[i] = speed;
                case "strobo" -> dmx[i] = strobo;
                case "panfein" -> dmx[i] = panfein;
                case "tiltfein" -> dmx[i] = tiltfein;
                case "custom" -> dmx[i] = 0;
            }
        }
    }

    public void clearLampe() {
        setRed((byte) 0);
        setGreen((byte) 0);
        setBlue((byte) 0);
        setWhite((byte) 0);
    }

    public byte[] getDmx() {
        setDmx();
        return dmx;
    }

    public void changeColor() throws InterruptedException {
        red++;
        green++;
        blue++;
        Thread.sleep(100);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getRed() {
        return red;
    }

    public void setRed(byte red) {
        this.red = red;
    }

    public byte getGreen() {
        return green;
    }

    public void setGreen(byte green) {
        this.green = green;
    }

    public byte getBlue() {
        return blue;
    }

    public void setBlue(byte blue) {
        this.blue = blue;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String[] getChannelData() {
        return channelData;
    }

    public void setChannelData(String[] channelData) {
        this.channelData = channelData;
    }

    public byte getWhite() {
        return white;
    }

    public void setWhite(byte white) {
        this.white = white;
    }

    public byte getPan() {
        return pan;
    }

    public void setPan(byte pan) {
        this.pan = pan;
    }

    public byte getTilt() {
        return tilt;
    }

    public void setTilt(byte tilt) {
        this.tilt = tilt;
    }

    public byte getDimmer() {
        return dimmer;
    }

    public void setDimmer(byte dimmer) {
        this.dimmer = dimmer;
    }

    public byte getSpeed() {
        return speed;
    }

    public void setSpeed(byte speed) {
        this.speed = speed;
    }

    public String[] getChannelName() {
        return channelName;
    }

    public void setChannelName(String[] channelName) {
        this.channelName = channelName;
    }

    public byte getStrobo() {
        return strobo;
    }

    public void setStrobo(byte strobo) {
        this.strobo = strobo;
    }

    public byte getPanfein() {
        return panfein;
    }

    public void setPanfein(byte panfein) {
        this.panfein = panfein;
    }

    public byte getTiltfein() {
        return tiltfein;
    }

    public void setTiltfein(byte tiltfein) {
        this.tiltfein = tiltfein;
    }
}
