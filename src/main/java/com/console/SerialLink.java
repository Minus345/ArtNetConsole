package com.console;

import com.fazecast.jSerialComm.SerialPort;

import java.io.InputStream;
import java.util.Arrays;

public class SerialLink {
    private static String build;

    public static void run() {
        Runnable runnable = SerialLink::action;
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static void action() {
        System.out.println("List COM ports");
        SerialPort[] comPorts = SerialPort.getCommPorts();
        for (int i = 0; i < comPorts.length; i++)
            System.out.println("comPorts[" + i + "] = " + comPorts[i].getDescriptivePortName());
        int port = Main.getSerialPort();     // array index to select COM port
        comPorts[port].openPort();
        System.out.println("open port comPorts[" + port + "]  " + comPorts[port].getDescriptivePortName());
        comPorts[port].setBaudRate(9600);
        comPorts[port].setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        InputStream in = comPorts[port].getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            try {
                stringBuilder.append((char) in.read());
                build = stringBuilder.toString();
                if (build.endsWith("\n")) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    String build1 = stringBuilder.toString();
                    try {
                        changeDmxSerialInput(build1, comPorts[port]);
                    }catch (Exception ignored){}

                    stringBuilder = new StringBuilder();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void changeDmxSerialInput(String s, SerialPort port) {
        String[] split = s.split("\\|");

        if (split[20].equals("c")) {
            clear();
            return;
        }
        if (!split[20].equals("0")) {
            changeLampeSerialInput(split, port);
            return;
        }
        if (Main.getSelectedLampe() == null) return;

        Main.getSelectedLampe().setPan((byte) Integer.parseInt(split[0]));
        Main.getSelectedLampe().setPanfein((byte) Integer.parseInt(split[1]));
        Main.getSelectedLampe().setTilt((byte) Integer.parseInt(split[2]));
        Main.getSelectedLampe().setTiltfein((byte) Integer.parseInt(split[3]));
        Main.getSelectedLampe().setSpeed((byte) Integer.parseInt(split[4]));

        Main.getSelectedLampe().setDimmer((byte) Integer.parseInt(split[5]));
        Main.getSelectedLampe().setShutter((byte) Integer.parseInt(split[6]));
        Main.getSelectedLampe().setStrobo((byte) Integer.parseInt(split[7]));
        Main.getSelectedLampe().setFokus((byte) Integer.parseInt(split[8]));
        Main.getSelectedLampe().setPrisma((byte) Integer.parseInt(split[9]));

        if (Main.getSelectedLampe().getMatrixCount() != 0) {
            Main.getSelectedLampe().setRed((byte) Integer.parseInt(split[10]), 0);
            Main.getSelectedLampe().setGreen((byte) Integer.parseInt(split[11]), 0);
            Main.getSelectedLampe().setBlue((byte) Integer.parseInt(split[12]), 0);
            Main.getSelectedLampe().setWhite((byte) Integer.parseInt(split[13]), 0);
            if ((byte) Integer.parseInt(split[14]) > Main.getSelectedLampe().getRed().size() || (byte) Integer.parseInt(split[14]) < 0) {
                LampActions.setMatrix(0);
            } else {
                LampActions.setMatrix((byte) Integer.parseInt(split[14]));
            }
        }
        int goboCount = ((byte) Integer.parseInt(split[16]));
        if (!Main.getSelectedLampe().getGobo().isEmpty()) Main.getSelectedLampe().setGobo((byte) Integer.parseInt(split[15]), goboCount);
        Main.getSelectedLampe().setColorWheel((byte) Integer.parseInt(split[17]));

        SystemPrint();
    }

    private static void changeLampeSerialInput(String[] split, SerialPort port) {
        try {
            int id = Integer.parseInt(split[20]);
            for (Lampe lampe : Main.Lampen) {
                if (id > Main.getLampen().size()) {
                    System.out.println("die Lampe gibt es nicht");
                }
                if (lampe.getId() == id) {
                    System.out.println("Lampe gefunden");
                    Main.setSelectedLampe(lampe);
                }
            }
            System.out.println(Main.getSelectedLampe().getName());
            System.out.println("Channel auswählen");
            for (int i = 0; i < Main.getSelectedLampe().getChannelName().length; i++) {
                System.out.print(i + " : " + Main.getSelectedLampe().getChannelData()[i] + " | ");
            }
            System.out.println();
            writeBytesToArduino(port);
        } catch (Exception ignored) {
        }
    }

    private static void clear() {
        Main.setSelectedLampe(Main.getLampen().get(0));
        for (int i = 0; i < Main.getLampen().size(); i++) {
            Main.getLampen().get(i).clearLampe();
        }
    }

    private static void SystemPrint() {
        Lampe lampe = Main.getSelectedLampe();
        if (lampe.equals(Main.getLampen().get(0))) return;
        for (int i = 0; i < lampe.getChannelData().length; i++) {
            System.out.print(lampe.getChannelData()[i] + ":" + (lampe.getDmx()[i] & 0xff) + " | ");
        }
        System.out.println();
    }

    private static void writeBytesToArduino(SerialPort port) {
        Lampe lampe = Main.getSelectedLampe();
        StringBuilder stringBuilder = new StringBuilder();

        byte[] allAttributes = lampe.getAllAttributes();
        for (int i = 0; i < 20; i++) {
            stringBuilder.append(allAttributes[i]);
            stringBuilder.append("|");
        }

        build = stringBuilder.toString();
        byte[] buildBytes = build.getBytes();
        System.out.println("bytes: " + build);
        port.writeBytes(buildBytes, buildBytes.length);
    }
}
