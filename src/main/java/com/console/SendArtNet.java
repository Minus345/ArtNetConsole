package com.console;

import ch.bildspur.artnet.ArtNetClient;
import com.console.scene.Scenes;

import java.net.InetAddress;
import java.util.Arrays;

public class SendArtNet {

    private static final byte[] dmxData = new byte[512];
    private static ArtNetClient artNetClient;

    // TODO: 05.10.2022 Getter fon Allen Jetzt laufneden Szenen; die übernander legen (jeden channel addieren + user imput) dann senden

    public static void createArtNetController(InetAddress address) {
        artNetClient = new ArtNetClient();
        artNetClient.start(address);
    }

    public static void sendData() {
        //if (Scenes.isSceneActiv()) return;
        int positionDmxData = 0;
        for (Lampe lampe : Main.getLampen()) {
            System.arraycopy(lampe.getDmx(), 0, dmxData, positionDmxData, lampe.getChannel());
            positionDmxData = positionDmxData + lampe.getChannel();
        }
        for (int sceneNumber = 0; sceneNumber < Scenes.runningScenesArray.size(); sceneNumber++) {
            for (int i = 0; i < 512; i++) {
                dmxData[i] = (byte) (dmxData[i] + Scenes.runningScenesArray.get(sceneNumber).getFinishDmxData()[i]);
            }
        }
        sendArtNetData(dmxData);
        Arrays.fill(dmxData, (byte) 0);
    }

    private static void sendArtNetData(byte[] data) {
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
