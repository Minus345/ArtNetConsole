package com.console.mysql.files;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

import static org.apache.commons.io.IOUtils.DEFAULT_BUFFER_SIZE;

public class FileManager {

    public static final String dirName = "ArtNetConsole//";

    private static FileManager instance;

    public static FileManager getInstance() {
        if(instance == null){
            instance = new FileManager();
        }
        return instance;
    }

    public FileManager() {
        try {
            loadFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadFiles() throws IOException {
        if(!new File(dirName.replace("//", "")).exists()){
            new File(dirName.replace("//", "")).mkdir();
        }
        File mysqlFile = new File(dirName + "mysql.json");
        if(!mysqlFile.exists()) copyResourceFile("mysql.json", "mysql.json");
    }

    public File getMysqlFile(){
        return new File(dirName + "mysql.json");
    }

    public JSONObject getMysqlObject(){
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(dirName + "mysql.json")){
            return (JSONObject) parser.parse(reader);
        }catch (IOException | ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    public void copyResourceFile(String src, String dest) throws IOException {
        InputStream resourceURL = this.getClass().getResourceAsStream("/defaults/" + src);

        File file2 = new File(dirName + dest);

        copyInputStreamToFile(resourceURL, file2);

    }

    private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        // append = false
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }

    }

}
