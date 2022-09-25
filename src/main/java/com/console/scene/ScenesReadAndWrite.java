package com.console.scene;

import com.console.Lampe;
import com.console.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;

public class ScenesReadAndWrite {

    private static final byte[] dmxData = new byte[512];
    private static final JSONArray data = new JSONArray();

    public static void write() {
        try (FileWriter file = new FileWriter("src/main/resources/scenes.json")) {
            file.write(data.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addStep(byte[] myArray, int number, int time) {
        JSONArray jsArray = new JSONArray();
        JSONObject step = new JSONObject();

        for (int i = 0; i <= myArray.length - 1; i++) {
            jsArray.add(myArray[i]);
        }

        step.put("number", number);
        step.put("time", time);
        step.put("array", jsArray);

        data.add(step);
    }

    public static void read() {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("src/main/resources/scenes.json")) {
            Object obj = jsonParser.parse(reader);
            JSONArray array = (JSONArray) obj;
            System.out.println(array);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

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

    public static String getLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        return line;
    }
}
