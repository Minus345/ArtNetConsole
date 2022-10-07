package com.console;

import com.console.midi.Midi;
import com.console.mysql.MySQL;
import com.console.mysql.files.FileManager;
import com.console.patch.PatchReader;
import com.console.scene.Scenes;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

public class Main {

    public static MySQL mySQL;

    public static ArrayList<Lampe> Lampen = new ArrayList<>();
    private static Lampe selectedLampe;

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("Start");

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            System.out.println(networkInterface.getDisplayName() + " : " + networkInterface.getName() + "-------------" + networkInterface.getInterfaceAddresses() + "  " + networkInterface.getInetAddresses());
        }

        /*
        NetworkInterface ni = NetworkInterface.getByName("eth6"); // interface name
        InetAddress address = ni.getInetAddresses().nextElement();

        System.out.println("Network Interface: " + ni.getName() + " | " + ni.getInetAddresses().nextElement() + " | " + ni.getInterfaceAddresses());


         */
        //Add Lampen
        // PatchReader.readFile("patch.yaml");
        //YamlManager.readFile("pico_beam.yaml",1);
        //YamlManager.readFile("bar.yaml",2);
        //Lampen.add(new Lampe(2, "led", 3));
        // System.out.println("Amzahl der Lampen: " + Lampen.size());
        //Sort Lamps
        // Lampen.sort(Comparator.comparingInt(Lampe::getId));

        // Do SQL stuff
        FileManager.getInstance();
        reloadMySQL();

        //Create Midi Device
        Midi midi = new Midi();
        midi.Midi();

        /*
        //Start Ticker
        SendArtNet.tick(address); //192.168.178.131
        tick();

         */
/*
        Scenes.createScene("1", true, 0);
        byte[] data1 = new byte[512];
        Scenes.getActivScene().addStep(1, 5, 2, data1);
        byte[] data2 = new byte[512];
        data2[0] = (byte) 100;
        Scenes.getActivScene().addStep(2, 5, 2, data2);
*/
        LampActions.selectLamp();
    }

    public static void tick() {
        Runnable runnable = () -> {
            while (Thread.currentThread().isAlive()) {
                Effect.changeColor();
                Effect.dimmerEffect();
                try {
                    Effect.circle();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static ArrayList<Lampe> getLampen() {
        return Lampen;
    }

    public static Lampe getSelectedLampe() {
        return selectedLampe;
    }

    public static void setSelectedLampe(Lampe selectedLampe) {
        Main.selectedLampe = selectedLampe;
    }

    public static MySQL getMySQL() {
        return mySQL;
    }

    public static void reloadMySQL() {
        if(FileManager.getInstance().getMysqlFile().exists()) {
            JSONObject obj = FileManager.getInstance().getMysqlObject();
            if (mySQL != null && mySQL.isConnected()) {
                mySQL.disconnect();
            }
            mySQL = new MySQL(obj.get("host").toString(), obj.get("port").toString(), obj.get("database").toString(), obj.get("username").toString()
                    , obj.get("password").toString());
            mySQL.connect();
            System.out.println(obj);
            System.out.println("Connecting to mysql");
            if (mySQL.isConnected()) {
                System.out.println("connected suc");
                mySQL.executeUpdate("CREATE TABLE IF NOT EXISTS scans (NAME VARCHAR(100),CHANNELS VARCHAR(100),ID BIGINT(100))");
                mySQL.executeUpdate("CREATE TABLE IF NOT EXISTS scans_channels (ID BIGINT(100),CHANNEL BIGINT(100),NAME VARCHAR(100))");
                mySQL.executeUpdate("CREATE TABLE IF NOT EXISTS scans_channels_options (ID BIGINT(100),CHANNEL BIGINT(100),NAME VARCHAR(100),MIN_VALUE BIGINT(100),MAX_VALUE BIGINT(100))");
                mySQL.executeUpdate("CREATE TABLE IF NOT EXISTS patch (NAME VARCHAR(100),ID BIGINT(100),UNIVERSE BIGINT(100))");
                mySQL.executeUpdate("CREATE TABLE IF NOT EXISTS patch_mapping (ID BIGINT(100),START_ADDRESS BIGINT(100),SCAN_ID BIGINT(100))");
                mySQL.executeUpdate("CREATE TABLE IF NOT EXISTS scenes (NAME VARCHAR(100),ID BIGINT(100))");
                mySQL.executeUpdate("CREATE TABLE IF NOT EXISTS scenes_steps (SCENE_ID BIGINT(100),STEP_ID BIGINT(100),BLEND_TIME BIGINT(100),WAIT_TIME BIGINT(100),TOTAL_TIME BIGINT(100))");
            }
        }
    }
}
