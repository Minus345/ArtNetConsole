package com.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

public class Main {

    public static ArrayList<Lampe> Lampen = new ArrayList<>();

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
        Lampen.add(new Lampe(2, "led", 3));
        Lampen.add(new Lampe(1, "led", 3));
        Lampen.add(new Lampe(3, "led", 3));

        Lampen.sort(Comparator.comparingInt(Lampe::getId));

        SendArtNet.tick(address); //192.168.178.131
        selectLamp();


    }

    public static void selectLamp() throws IOException {

        System.out.println("Lampen Id eigeben");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int selection = Integer.parseInt(reader.readLine());

        Iterator<Lampe> iterator = Lampen.iterator();
        while (iterator.hasNext()) {
            Lampe lampe = iterator.next();
            if (lampe.getId() == selection) {
                System.out.println("Lampe gefunden");
                modifyLampe(lampe);
            }
        }
    }

    public static void modifyLampe(Lampe lampe) throws IOException {
        System.out.println("Effect auswählen");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String selection = reader.readLine();

        switch (selection) {
            case "r":
                lampe.setRed((byte) 255);
                lampe.setGreen((byte) 0);
                lampe.setBlue((byte) 0);
                break;
            case "g":
                lampe.setRed((byte) 0);
                lampe.setGreen((byte) 255);
                lampe.setBlue((byte) 0);
                break;
            case "b":
                lampe.setRed((byte) 0);
                lampe.setGreen((byte) 0);
                lampe.setBlue((byte) 255);
                break;
            case "w":
                lampe.setRed((byte) 255);
                lampe.setGreen((byte) 255);
                lampe.setBlue((byte) 255);
                break;
        }
    }

    public static ArrayList<Lampe> getLampen() {
        return Lampen;
    }
}
