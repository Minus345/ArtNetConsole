package com.console;

import com.console.midi.Midi;
import com.console.patch.PatchReader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

public class Main {

    public static ArrayList<Lampe> Lampen = new ArrayList<>();
    private static Lampe selectedLampe;
    private static String sceneSavePath;

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("Start");

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            System.out.println(networkInterface.getDisplayName() + " : " + networkInterface.getName() + "-------------" + networkInterface.getInterfaceAddresses() + "  " + networkInterface.getInetAddresses());
        }

        NetworkInterface ni = NetworkInterface.getByName(args[0]); // interface name
        InetAddress address = ni.getInetAddresses().nextElement();

        System.out.println("Network Interface: " + ni.getName() + " | " + ni.getInetAddresses().nextElement() + " | " + ni.getInterfaceAddresses());

        //Add Lampene
        PatchReader.readFile(args[1], args[2]);
        System.out.println("Amzahl der Lampen: " + Lampen.size());
        //Sort Lamps
        Lampen.sort(Comparator.comparingInt(Lampe::getId));

        //Get save file path
        setSceneSavePath(args[3]);

        //Create Midi Device
        System.out.println("----Midi----");
        Midi midi = new Midi();
        midi.Midi();

        //Start Ticker
        System.out.println("----Art Net----");
        SendArtNet.tick(address); //192.168.178.131
        tick();

        System.out.println("----Serial Port----");
        SerialLink.run();

        System.out.println("----Start----");
        LampActions.selectLamp();
    }

    public static void tick() {
        Runnable runnable = () -> {
            while (Thread.currentThread().isAlive()) {
                Effect.changeColor();
                Effect.dimmerEffect();
                try {
                    Effect.circle();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static ArrayList<Lampe> getLampen() {
        return Lampen;
    }

    public static Lampe getSelectedLampe() {
        return selectedLampe;
    }

    public static void setSelectedLampe(Lampe selectedLampe) {
        Main.selectedLampe = selectedLampe;
    }

    public static String getSceneSavePath() {
        return sceneSavePath;
    }

    public static void setSceneSavePath(String sceneSavePath) {
        Main.sceneSavePath = sceneSavePath;
    }
}
