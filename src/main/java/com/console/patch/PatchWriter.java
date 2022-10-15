package com.console.patch;

import com.console.Main;

import java.io.*;

public class PatchWriter {
    public static void writePatch() throws IOException {
        System.out.println("Writing Patch");
        System.out.println("Path:");
        String path = getLine();
        writeFile(path);
    }

    public static void writeFile(String path) throws IOException {
        File fout = new File(path + "\\patch.txt");
        System.out.println("path: " + path + "\\patch");
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        int addr = 1;
        bw.write("Patch: ");
        bw.newLine();
        for (int i = 0; i < Main.getLampen().size(); i++) {
            bw.write(Main.getLampen().get(i).getId() + " | " + Main.getLampen().get(i).getName() + " | " + Main.getLampen().get(i).getChannel() + " | " + "Addresse: " + addr);
            addr = addr + Main.getLampen().get(i).getChannel();
            bw.newLine();
        }

        bw.close();
    }

    public static String getLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }
}
