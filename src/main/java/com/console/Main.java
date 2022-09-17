package com.console;

import com.console.midi.Midi;
import com.console.yamlfile.YamlManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        YamlManager.readFile();
        //Lampen.add(new Lampe(2, "led", 3));

        //Sort Lamps
        Lampen.sort(Comparator.comparingInt(Lampe::getId));

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
                    Thread.sleep(100);
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
