package com.console;

import ch.bildspur.artnet.ArtNetClient;
import com.console.scene.Scenes;

import java.net.InetAddress;

public class SendArtNet {

    private static final byte[] dmxData = new byte[512];
    private static ArtNetClient artNetClient;

    public static void createArtNetController(InetAddress address){
        artNetClient = new ArtNetClient();
        artNetClient.start(address);
    }

    public static void sendScene(byte[] dmx){
        sendArtNetData(dmx);
    }

    public static void sendData() {
        //1System.out.println("Sending Art Net");
        if(Scenes.isSceneActiv()) return;
        int positionDmxData = 0;
        for (Lampe lampe : Main.getLampen()) {
            System.arraycopy(lampe.getDmx(),0,dmxData,positionDmxData,lampe.getChannel());
            positionDmxData = positionDmxData + lampe.getChannel();
        }
        sendArtNetData(dmxData);
    }

    private static void sendArtNetData(byte[] data){
        artNetClient.broadcastDmx(0, 1, data);
    }

public static void tick(InetAddress address) {
        createArtNetController(address);
        Runnable runnable = () -> {
            while (Thread.currentThread().isAlive()) {
                    sendData();
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
