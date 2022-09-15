package com.console.yamlfile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class YamlManager {
    public static void readFile() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("lampe.yaml")).getFile());

        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        LampenTempletReader lampenTempletReader = om.readValue(file, LampenTempletReader.class);

        System.out.println(lampenTempletReader.toString());

        lampenTempletReader.createLamp();
    }
}
