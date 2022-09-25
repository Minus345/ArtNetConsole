package com.console;

import com.console.midi.Midi;
import com.console.patch.PatchReader;
import com.console.scene.ScenesReadAndWrite;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

public class Main {

    public static ArrayList<Lampe> Lampen = new ArrayList<>();
    private static Lampe selectedLampe;

    public static void main(String[] args) throws IOException {
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

        //ScenesReadAndWrite.write();

        //Create Midi Device
        Midi midi = new Midi();
        midi.Midi();

        //Start Ticker
        SendArtNet.tick(address); //192.168.178.131
        tick();
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
