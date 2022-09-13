package com.console;

public class Lampe {
    private int id;
    private String name;
    private byte red;
    private byte green;
    private byte blue;
    private int channel;
    private final byte[] dmx;

    public Lampe(int newId, String newName, int newChannel) {
        id = newId;
        name = newName;
        channel = newChannel;
        dmx = new byte[newChannel];
    }

    public void setDmx() {
            dmx[0] = red;
            dmx[1] = green;
            dmx[2] = blue;
    }
    
    public void clearLampe(){
        setRed((byte) 0);
        setGreen((byte) 0);
        setBlue((byte) 0);
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
}
