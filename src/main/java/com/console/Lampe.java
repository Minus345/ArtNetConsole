package com.console;

import java.util.*;

public class Lampe {
    private int id;
    private String name;
    private final int channel;
    private String[] channelData;
    private String[] channelName;
    private final ArrayList<Byte> red = new ArrayList<>();
    private final ArrayList<Byte> green = new ArrayList<>();
    private final ArrayList<Byte> blue = new ArrayList<>();
    private final ArrayList<Byte> white = new ArrayList<>();
    private byte pan;
    private byte panfein;
    private byte tilt;
    private byte tiltfein;
    private byte dimmer;
    private byte speed;
    private byte strobo;
    private byte shutter;
    private final ArrayList<Byte> gobo = new ArrayList<>();
    private final byte[] dmx;
    private final int matrixCount;


    public Lampe(int id, String name, int channel, String[] channelData, String[] channelName, int rgbMatrix) {
        this.id = id;
        this.name = name;
        this.channel = channel;
        this.channelData = channelData;
        this.channelName = channelName;
        this.matrixCount = rgbMatrix;
        dmx = new byte[channel];
        for (int i = 0; i < matrixCount; i++) {
            red.add((byte) 0);
            green.add((byte) 0);
            blue.add((byte) 0);
            white.add((byte) 0);
        }
    }

    public void setDmx() {
        //System.out.println(channelData.length);
        for (int i = 0; i <= (channelData.length - 1); i++) {
            //System.out.println(i);
            switch (channelData[i]) {
                case "pan" -> dmx[i] = pan;
                case "tilt" -> dmx[i] = tilt;
                case "dimmer" -> dmx[i] = dimmer;
                case "speed" -> dmx[i] = speed;
                case "strobo" -> dmx[i] = strobo;
                case "panfein" -> dmx[i] = panfein;
                case "tiltfein" -> dmx[i] = tiltfein;
                case "shutter" -> dmx[i] = shutter;
                case "custom" -> dmx[i] = 0;
            }
            if (channelData[i].startsWith("red")) {
                String[] split = channelData[i].split(":");
                dmx[i] = red.get(Integer.parseInt(split[1]) - 1);
            }
            if (channelData[i].startsWith("green")) {
                String[] split = channelData[i].split(":");
                dmx[i] = green.get(Integer.parseInt(split[1]) - 1);
            }
            if (channelData[i].startsWith("blue")) {
                String[] split = channelData[i].split(":");
                dmx[i] = blue.get(Integer.parseInt(split[1]) - 1);
            }
            if (channelData[i].startsWith("white")) {
                String[] split = channelData[i].split(":");
                dmx[i] = white.get(Integer.parseInt(split[1]) - 1);
            }
            if (channelData[i].startsWith("gobo")) {
                String[] split = channelData[i].split(":");
                dmx[i] = gobo.get(Integer.parseInt(split[1]) - 1);
            }


        }
    }

    public void clearLampe() {
        setRed((byte) 0, -1);
        setGreen((byte) 0, -1);
        setBlue((byte) 0, -1);
        setWhite((byte) 0, -1);
    }

    public byte[] getDmx() {
        setDmx();
        return dmx;
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

    public void setRed(byte red, int matrix) {
        if (matrix == -1) {
            Collections.fill(this.red, red);
        } else {
            this.red.set(matrix, red);
        }
    }

    public void setGreen(byte green, int matrix) {
        if (matrix == -1) {
            Collections.fill(this.green, green);
        } else {
            this.green.set(matrix, green);
        }
    }

    public void setBlue(byte blue, int matrix) {
        if (matrix == -1) {
            Collections.fill(this.blue, blue);
        } else {
            this.blue.set(matrix, blue);
        }
    }

    public void setWhite(byte white, int matrix) {
        if (matrix == -1) {
            Collections.fill(this.white, white);
        } else {
            this.white.set(matrix, white);
        }
    }

    public void setGobo(byte gobo, int gobocount) {
        this.white.set(gobocount, gobo);
    }

    public int getChannel() {
        return channel;
    }

    public String[] getChannelData() {
        return channelData;
    }

    public void setChannelData(String[] channelData) {
        this.channelData = channelData;
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

    public byte getShutter() {
        return shutter;
    }

    public void setShutter(byte shutter) {
        this.shutter = shutter;
    }

    public int getMatrixCount() {
        return matrixCount;
    }
}
