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
    private static final List<Byte> abstand = new List<Byte>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<Byte> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(Byte aByte) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends Byte> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends Byte> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public Byte get(int index) {
            return null;
        }

        @Override
        public Byte set(int index, Byte element) {
            return null;
        }

        @Override
        public void add(int index, Byte element) {

        }

        @Override
        public Byte remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<Byte> listIterator() {
            return null;
        }

        @Override
        public ListIterator<Byte> listIterator(int index) {
            return null;
        }

        @Override
        public List<Byte> subList(int fromIndex, int toIndex) {
            return null;
        }
    };

    public static void write() {
        try (FileWriter file = new FileWriter("src/main/resources/scenes.json")) {
            file.write(obj.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addStep(byte[] myArray, int number, int time) {
        for (int i = 0; i <= myArray.length - 1; i++) {
            jsArray.add(myArray[i]);
        }

        step.put("number", number);
        step.put("time", time);
        step.put("array", jsArray);

        data.add(step);
        obj.put("name", "Szenen Name ");
        obj.put("data", data);
    }

    public static void read() {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("src/main/resources/scenes.json")) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            System.out.println(jsonObject);

            JSONArray data = (JSONArray) jsonObject.get("data");
            System.out.println(data.get(0));

            JSONObject stepStart = (JSONObject) data.get(0);
            long number = (long) stepStart.get("number");
            JSONArray dmxStart = (JSONArray) stepStart.get("array");

            JSONObject stepToFade = (JSONObject) data.get(1);
            long numberToFade = (long) stepToFade.get("number");
            JSONArray dmxToFade = (JSONArray) stepToFade.get("array");

            for (int i = 0; i < dmxStart.size(); i++) {
                Long a = (Long) dmxStart.get(i);
                int a1 =  a.intValue();
                Long b = (Long) dmxToFade.get(i);
                int b1 =  b.intValue();
                byte ab =  (byte) (a1 - b1);
                abstand.set(i, ab);
            }
            for (int i = 0; i < Collections.max(abstand); i++)
                for (int j = 0; j < dmxStart.size(); j++) {
                    if (dmxStart.get(j) == dmxToFade.get(j)) return;
                    if (abstand.get(j) > 0) { //positiv
                        dmxStart.set(j, (int) dmxStart.get(j) + 1);
                    }
                    if (abstand.get(j) < 0) { //negativ
                        dmxStart.set(j, (int) dmxStart.get(j) - 1);
                    }
                }


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
