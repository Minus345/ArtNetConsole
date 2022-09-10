package com.console;

import ch.bildspur.artnet.ArtNetBuffer;
import ch.bildspur.artnet.ArtNetClient;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Main {

    public static void main(String[] args) throws SocketException {
        System.out.println("Start");

        Lampe lampe =  new Lampe(1,"led",3);

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            System.out.println(networkInterface.getDisplayName() + " : " + networkInterface.getName());
        }

        ArtNetClient artnet = new ArtNetClient(new ArtNetBuffer(), 8000, 8000);

        NetworkInterface ni = NetworkInterface.getByName(""); // interface name
        InetAddress address = ni.getInetAddresses().nextElement();

        artnet.start(address);
        while (true) {
            byte[] data = artnet.readDmxData(0, 0);
            for (int i = 1; i < 512; i++) {
                System.out.print((data[i] & 0xFF) + "  ");
            }
            System.out.println("  ");
        }
        //artnet.stop();
    }
}
