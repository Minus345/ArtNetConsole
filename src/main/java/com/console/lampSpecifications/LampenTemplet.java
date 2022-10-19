package com.console.lampSpecifications;

import com.console.Lampe;
import com.console.Main;

public class LampenTemplet {

    private String name;
    private int channel;
    private int rgbmatrix;
    private int gobo;
    private String[] channelData = new String[channel];
    private String[] channelName = new String[channel];

    public LampenTemplet(String name, int channel, String[] channelData, String[] channelName, int rgbmatrix, int gobo) {
        this.name = name;
        this.channel = channel;
        this.channelData = channelData;
        this.channelName = channelName;
        this.rgbmatrix = rgbmatrix;
        this.gobo = gobo;
    }

    // Without a default constructor, Jackson will throw an exception
    public LampenTemplet() {
    }

    public void createLamp(int id) {
        Main.getLampen().add(new Lampe(id, name, channel, channelData, channelName, rgbmatrix, gobo));
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String[] getChannelName() {
        return channelName;
    }

    public void setChannelName(String[] channelName) {
        this.channelName = channelName;
    }

    public int getRgbmatrix() {
        return rgbmatrix;
    }

    public void setRgbmatrix(int rgbmatrix) {
        this.rgbmatrix = rgbmatrix;
    }

    public int getGobo() {
        return gobo;
    }

    public void setGobo(int gobo) {
        this.gobo = gobo;
    }
}
