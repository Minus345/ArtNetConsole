package com.console;

import com.fazecast.jSerialComm.SerialPort;

import java.io.InputStream;
import java.util.Arrays;

public class SerialLink {

    private static String build;
    private static Lampe lampeAlt = Main.getSelectedLampe();
    private static byte actuell[] = new byte[20];
    private static byte posAlt[] = new byte[20];
    private static int goboCount = 0;

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
        int port = 1;     // array index to select COM port
        comPorts[port].openPort();
        System.out.println("open port comPorts[" + port + "]  " + comPorts[port].getDescriptivePortName());
        comPorts[port].setBaudRate(9600);
        comPorts[port].setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        InputStream in = comPorts[port].getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            try {
                //System.out.print((char) in.read());
                stringBuilder.append((char) in.read());
                build = stringBuilder.toString();
                if (build.endsWith("\n")) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    String build1 = stringBuilder.toString();
                    //System.out.println(build);
                    //changeDmxPoti(build);
                    changeDmxSerialInput(build1);
                    stringBuilder = new StringBuilder();
                }
                //in.close();
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    private static void changeDmxSerialInput(String s) {
        String[] split = s.split("\\|");

        if (!split[20].equals("0")) {
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
                System.out.println("Channel auswählen");
                for (int i = 0; i <= (Main.getSelectedLampe().getChannelName().length - 1); i++) {
                    System.out.print(i + " : " + Main.getSelectedLampe().getChannelData()[i] + " | ");
                }
                System.out.println();
            } catch (Exception e) {
            }
        }

        if (Main.getSelectedLampe() == null) return;
        if (Main.getSelectedLampe() == lampeAlt) {
            lampeAlt = Main.getSelectedLampe();
        } else {
            for (int i = 0; i < actuell.length; i++) {
                actuell[i] = (byte) Integer.parseInt(split[i]);
            }
            posAlt[0] = Main.getSelectedLampe().getPan();
            posAlt[1] = Main.getSelectedLampe().getPanfein();
            posAlt[2] = Main.getSelectedLampe().getTilt();
            posAlt[3] = Main.getSelectedLampe().getTiltfein();
            posAlt[4] = Main.getSelectedLampe().getSpeed();

            posAlt[5] = Main.getSelectedLampe().getDimmer();
            posAlt[6] = Main.getSelectedLampe().getShutter();
            posAlt[7] = Main.getSelectedLampe().getStrobo();
            posAlt[8] = Main.getSelectedLampe().getFokus();
            posAlt[9] = Main.getSelectedLampe().getPrisma();

            posAlt[10] = Main.getSelectedLampe().getRed().get(0);
            posAlt[11] = Main.getSelectedLampe().getGreen().get(0);
            posAlt[12] = Main.getSelectedLampe().getBlue().get(0);
            posAlt[13] = Main.getSelectedLampe().getWhite().get(0);
            posAlt[14] = (byte) Main.getSelectedLampe().getMatrixCount();

            if (!Main.getSelectedLampe().getGobo().isEmpty()) posAlt[15] = Main.getSelectedLampe().getGobo().get(0);
            posAlt[16] = (byte) goboCount;
            //posAlt[17] = Main.getSelectedLampe().getPan();
            //posAlt[18] = Main.getSelectedLampe().getPan();
            //posAlt[19] = Main.getSelectedLampe().getPan();

            lampeAlt = Main.getSelectedLampe();
        }

        int result[] = new int[20];
        Arrays.fill(result, 0);
        for (int i = 0; i < actuell.length; i++) {
            try {
                result[i] = (Integer.parseInt(split[i]) - actuell[i] + posAlt[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Main.getSelectedLampe().setPan((byte) result[0]);
        Main.getSelectedLampe().setPanfein((byte) result[1]);
        Main.getSelectedLampe().setTilt((byte) result[2]);
        Main.getSelectedLampe().setTiltfein((byte) result[3]);
        Main.getSelectedLampe().setSpeed((byte) result[4]);

        Main.getSelectedLampe().setDimmer((byte) result[5]);
        Main.getSelectedLampe().setShutter((byte) result[6]);
        Main.getSelectedLampe().setStrobo((byte) result[7]);
        Main.getSelectedLampe().setFokus((byte) result[8]);
        Main.getSelectedLampe().setPrisma((byte) result[9]);

        Main.getSelectedLampe().setRed((byte) result[10], 0);
        Main.getSelectedLampe().setGreen((byte) result[11], 0);
        Main.getSelectedLampe().setBlue((byte) result[12], 0);
        Main.getSelectedLampe().setWhite((byte) result[13], 0);
        LampActions.setMatrix((byte) result[14]);

        goboCount = ((byte) result[16]);
        if(!Main.getSelectedLampe().getGobo().isEmpty()) Main.getSelectedLampe().setGobo((byte) result[15], goboCount);
        Main.getSelectedLampe().setColorWheel((byte) result[17]);

    }


    private static void changeDmxPoti(String s) {
        String[] split = s.split("\\|");

        if (!split[20].equals("0")) {
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
                System.out.println("Channel auswählen");
                for (int i = 0; i <= (Main.getSelectedLampe().getChannelName().length - 1); i++) {
                    System.out.print(i + " : " + Main.getSelectedLampe().getChannelData()[i] + " | ");
                }
                System.out.println();
            } catch (Exception e) {
            }
        }

        if (Main.getSelectedLampe() == null) return;
        try {
            Main.getSelectedLampe().setPan((byte)Integer.parseInt(split[0]));
            Main.getSelectedLampe().setPanfein((byte)Integer.parseInt(split[1]));
            Main.getSelectedLampe().setTilt((byte)Integer.parseInt(split[2]));
            Main.getSelectedLampe().setTiltfein((byte)Integer.parseInt(split[3]));
            Main.getSelectedLampe().setSpeed((byte)Integer.parseInt(split[4]));

            Main.getSelectedLampe().setDimmer((byte)Integer.parseInt(split[5]));
            Main.getSelectedLampe().setShutter((byte)Integer.parseInt(split[6]));
            Main.getSelectedLampe().setStrobo((byte)Integer.parseInt(split[7]));
            Main.getSelectedLampe().setFokus((byte)Integer.parseInt(split[8]));
            Main.getSelectedLampe().setPrisma((byte)Integer.parseInt(split[9]));

            Main.getSelectedLampe().setRed((byte)Integer.parseInt(split[10]), LampActions.getMatrix());
            Main.getSelectedLampe().setGreen((byte)Integer.parseInt(split[11]), LampActions.getMatrix());
            Main.getSelectedLampe().setBlue((byte)Integer.parseInt(split[12]), LampActions.getMatrix());
            Main.getSelectedLampe().setWhite((byte)Integer.parseInt(split[13]), LampActions.getMatrix());
            LampActions.setMatrix((byte)Integer.parseInt(split[14]));

            //Main.getSelectedLampe().setGobo((byte)Integer.parseInt()(split[15]), goboCount);
            goboCount = ((byte)Integer.parseInt(split[16]));
            Main.getSelectedLampe().setColorWheel((byte)Integer.parseInt(split[17]));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
