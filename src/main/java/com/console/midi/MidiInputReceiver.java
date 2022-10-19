package com.console.midi;

import com.console.LampActions;
import com.console.Main;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class MidiInputReceiver implements Receiver {
    public String name;

    public MidiInputReceiver(String name) {
        this.name = name;
    }

    public void send(MidiMessage msg, long timeStamp) {
        byte[] midiMassage = msg.getMessage();
        // take the MidiMessage msg and store it in a byte array

        //System.out.println("length: " + msg.getLength()); //returns the length of the message in bytes

        /*
        System.out.println("Button Typ: " + midiMassage[0]);
        System.out.println("Value: " + midiMassage[1]);
        System.out.println("pressed: " + midiMassage[2]);
        System.out.println();

         */
        if (Main.getSelectedLampe() == null) {
            System.out.println("keine Lampe festgelegt");
            return;
        } else {
            if (midiMassage[0] == -80) {
                switch (midiMassage[1]) {
                    case 1 -> Main.getSelectedLampe().setPan((byte) (255 / 127 * midiMassage[2])); //map 0-127 zu 0 -255
                    case 2 -> Main.getSelectedLampe().setTilt((byte) (255 / 127 * midiMassage[2])); //map 0-127 zu 0 -255
                    case 3 -> Main.getSelectedLampe().setDimmer((byte) (255 / 127 * midiMassage[2])); //map 0-127 zu 0 -255
                    case 4 -> Main.getSelectedLampe().setRed((byte) (255 / 127 * midiMassage[2]), LampActions.getMatrix()); //map 0-127 zu 0 -255
                    case 5 -> Main.getSelectedLampe().setGreen((byte) (255 / 127 * midiMassage[2]), LampActions.getMatrix()); //map 0-127 zu 0 -255
                    case 6 -> Main.getSelectedLampe().setBlue((byte) (255 / 127 * midiMassage[2]), LampActions.getMatrix()); //map 0-127 zu 0 -255
                    case 7 -> Main.getSelectedLampe().setWhite((byte) (255 / 127 * midiMassage[2]), LampActions.getMatrix()); //map 0-127 zu 0 -255
                    case 8 -> Main.getSelectedLampe().setStrobo((byte) (255 / 127 * midiMassage[2])); //map 0-127 zu 0 -255
                    case 9 -> Main.getSelectedLampe().setSpeed((byte) (255 / 127 * midiMassage[2])); //map 0-127 zu 0 -255
                }
            }
        }
    }

    public void close() {
    }
}

