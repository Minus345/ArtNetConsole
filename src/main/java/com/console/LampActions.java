package com.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

public class LampActions {
    public static void selectLamp() throws IOException {
        System.out.println("Lampen Id eigeben");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int selection = Integer.parseInt(reader.readLine());

        Iterator<Lampe> iterator = Main.Lampen.iterator();
        while (iterator.hasNext()) {
            Lampe lampe = iterator.next();
            if (lampe.getId() == selection) {
                System.out.println("Lampe gefunden");
                modifyLampe(lampe);
                Main.setSelectedLampe(lampe);
            }
        }
    }

    public static void modifyLampe(Lampe lampe) throws IOException {
        while (true) {
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
                case "changeColor":
                    Effect.changeColor.add(lampe);
                    break;
                case "dimmer":
                    Effect.dimmerEffect.add(lampe);
                    break;
                case "exit":
                    selectLamp();
                    break;
                case "clear":
                    lampe.clearLampe();
                    break;
                default:
                    System.out.println("Falsch geschrieben");
            }
        }
    }
}
