package com.console.patch;

import com.console.lampSpecifications.YamlManager;

import java.io.IOException;

public class Patch {

    private String name;
    private int Universe;
    private String[] patchArray = new String[0]; //anzahl der lampen

    // Without a default constructor, Jackson will throw an exception
    public Patch() {
    }

    public void readLampsFormFile() throws IOException {
        for (int i = 0; i <= (patchArray.length - 1); i++) {
            YamlManager.readFile(patchArray[i],(i + 1));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUniverse() {
        return Universe;
    }

    public void setUniverse(int universe) {
        Universe = universe;
    }

    public String[] getPatchArray() {
        return patchArray;
    }

    public void setPatchArray(String[] patchArray) {
        this.patchArray = patchArray;
    }
}
