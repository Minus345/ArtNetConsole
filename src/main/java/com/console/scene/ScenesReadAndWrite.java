package com.console.scene;

import com.console.Lampe;
import com.console.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class ScenesReadAndWrite {
    private static final byte[] dmxData = new byte[512];
    private static final JSONObject obj = new JSONObject();
    private static final JSONArray data = new JSONArray();
    private static final JSONArray jsArray = new JSONArray();
    private static final JSONObject step = new JSONObject();
    public static final byte[] dmxSceneValue = new byte[512];

    /**
     * reads the courant selected scene
     */
    public static void read() {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("src/main/resources/scenes.json")) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
           // System.out.println(jsonObject);

            JSONArray data = (JSONArray) jsonObject.get("data");
            //System.out.println(data.get(0));

            JSONObject stepStart = (JSONObject) data.get(0);
            long number = (long) stepStart.get("number");
            JSONArray dmxStart = (JSONArray) stepStart.get("array");

            JSONObject stepToFade = (JSONObject) data.get(1);
            long numberToFade = (long) stepToFade.get("number");
            JSONArray dmxToFade = (JSONArray) stepToFade.get("array");

            while (!dmxStart.equals(dmxToFade)) { // fade der beiden Arrays
                System.out.println("unterschiedlich");
                for (int i = 0; i < 512; i++) {
                    //if (dmxStart.get(i).equals(dmxToFade.get(i))) return;
                    Long dmxToFadeLong = (Long) dmxToFade.get(i);
                    int dmxToFadeInt = dmxToFadeLong.intValue();
                    Long dmxStartLong = (Long) dmxStart.get(i);
                    int dmxStartInt = dmxStartLong.intValue();
                    if (dmxToFadeInt < dmxStartInt) {
                        dmxStartInt = dmxStartInt - 1;
                        Long dmxToSet = Long.valueOf(dmxStartInt);
                        dmxStart.set(i, dmxToSet);
                    }
                    if (dmxToFadeInt > dmxStartInt) {
                        dmxStartInt = dmxStartInt + 1;
                        Long dmxToSet = Long.valueOf(dmxStartInt);
                        dmxStart.set(i, dmxToSet);
                    }
                    dmxSceneValue[i] = (byte) ((Long) dmxStart.get(i)).intValue();
                    System.out.println(i);
                }
                Thread.sleep(100);
            }

        } catch (IOException | ParseException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a step to the courant scene.
     * copy the courant fixture state to the "addStep" method
     * @throws IOException
     */
    public static void saveScene() throws IOException {
        int positionDmxData = 0;
        for (Lampe lampe : Main.getLampen()) {
            System.arraycopy(lampe.getDmx(), 0, dmxData, positionDmxData, lampe.getChannel());
            positionDmxData = positionDmxData + lampe.getChannel();
        }
        System.out.println("Nummer: ");
        int number = Integer.parseInt(getLine());
        System.out.println("Zeit: ");
        int time = Integer.parseInt(getLine());
        addStep(dmxData, number, time);
        //write();
    }

    /**
     * Writes the scene to the disc
     * After that you cannot add a step to the scene
     */
    public static void write() {
        try (FileWriter file = new FileWriter("src/main/resources/scenes.json")) {
            file.write(obj.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Puts the array in the right place in the json file
     * @param myArray the dmx state to write down
     * @param number which step is it
     * @param time the transition time between this and the next step
     */
    public static void addStep(byte[] myArray, int number, int time) {
        for (int i = 0; i <= myArray.length - 1; i++) {
            int add = Byte.toUnsignedInt(myArray[i]);
            jsArray.add(add);
        }

        step.put("number", number);
        step.put("time", time);
        step.put("array", jsArray);

        data.add(step);
        obj.put("name", "Szenen Name ");
        obj.put("data", data);
    }

    /**
     * @return the courant line in the terminal
     **/
    public static String getLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        return line;
    }

}
