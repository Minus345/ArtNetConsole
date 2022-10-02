package com.console;

import com.console.midi.Midi;
import com.console.patch.PatchReader;
import com.console.scene.Scene;
import com.console.scene.Scenes;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

public class Main {

    public static ArrayList<Lampe> Lampen = new ArrayList<>();
    private static Lampe selectedLampe;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Start");

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            System.out.println(networkInterface.getDisplayName() + " : " + networkInterface.getName() + "-------------" + networkInterface.getInterfaceAddresses() + "  " + networkInterface.getInetAddresses());
        }

        NetworkInterface ni = NetworkInterface.getByName("eth6"); // interface name
        InetAddress address = ni.getInetAddresses().nextElement();

        System.out.println("Network Interface: " + ni.getName() + " | " + ni.getInetAddresses().nextElement() + " | " + ni.getInterfaceAddresses());

        //Add Lampen
        PatchReader.readFile("patch.yaml");
        //YamlManager.readFile("pico_beam.yaml",1);
        //YamlManager.readFile("bar.yaml",2);
        //Lampen.add(new Lampe(2, "led", 3));
        System.out.println("Amzahl der Lampen: " + Lampen.size());
        //Sort Lamps
        Lampen.sort(Comparator.comparingInt(Lampe::getId));


        //Create Midi Device
        Midi midi = new Midi();
        midi.Midi();

        //Start Ticker
        SendArtNet.tick(address); //192.168.178.131
        tick();

        Scenes.createScene("1", true, 0);
        byte[] data1 = new byte[512];
        Scenes.getActivScene().addStep(1, 5, 2, data1);
        byte[] data2 = new byte[512];
        data2[0] = (byte) 100;
        Scenes.getActivScene().addStep(2, 5, 2, data2);
        byte[] data3 = new byte[512];
        data3[0] = (byte) 0;
       // Scenes.getActivScene().addStep(3, 2, 2, data3);

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
}
