package com.console.lampSpecifications;

import com.console.Lampe;
import com.console.Main;

import java.util.Arrays;

public class LampenTemplet {

    private String name;
    private int channel;
    private String[] channelData = new String[channel];
    private String[] channelName = new String[channel];

    public LampenTemplet(String name, int channel, String[] channelData, String[] channelName) {
        this.name = name;
        this.channel = channel;
        this.channelData = channelData;
        this.channelName = channelName;
    }

    // Without a default constructor, Jackson will throw an exception
    public LampenTemplet() {
    }

    public void createLamp(int id){
        Main.getLampen().add(new Lampe(id,name,channel,channelData,channelName));
    }

    // Getters and setters

    @Override
    public String toString() {
        return "\nName: " + name  + "\nChannel: "+ channel + "\nChannelData " + Arrays.toString(channelData)  + "\nChannelName: " + Arrays.toString(channelName) + "\n";
    }

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
}
