package com.console.patch;

import com.console.Main;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class PatchReader {
    public static void readFile(String path,String pathToSave) throws IOException {
        File file = new File(path);

        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Patch patch = om.readValue(file, Patch.class);

        System.out.println(patch.toString());

        patch.readLampsFormFile(pathToSave);
    }
}
