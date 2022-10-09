package com.console.lampSpecifications;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class YamlManager {
    public static void readFile(String path,int id) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(path);

        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        LampenTemplet lampenTemplet = om.readValue(file, LampenTemplet.class);

        System.out.println(lampenTemplet.toString());

        lampenTemplet.createLamp(id);
    }
}
