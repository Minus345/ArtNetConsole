package com.console.midi;

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

        System.out.println("Button Typ: " + midiMassage[0]);
        System.out.println("Value: " + midiMassage[1]);
        System.out.println("pressed: " + midiMassage[2]);
        System.out.println();

        if(Main.getSelectedLampe() == null){
            return;
        }else{
            if(midiMassage[0] == -80){
                switch (midiMassage[1]){
                    case 1://red
                        Main.getSelectedLampe().setRed((byte) (255 / 127 * midiMassage[2])); //map 0-127 zu 0 -255
                        break;

                    case 2://green
                        Main.getSelectedLampe().setGreen((byte) (255 / 127 * midiMassage[2])); //map 0-127 zu 0 -255
                        break;

                    case 3://blue
                        Main.getSelectedLampe().setBlue((byte) (255 / 127 * midiMassage[2])); //map 0-127 zu 0 -255
                        break;
                }
            }
        }
        System.out.println("send midi");

    }

    public void close() {
    }
}

