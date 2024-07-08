package model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static controller.constants.FilePaths.JsonFolderPath;
import static controller.constants.UIMessageConstants.*;
import static org.example.Main.scanner;

public abstract class JsonOperator {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static boolean proceedToSaveLoad = true;

    public static void JsonInitiate() throws IOException {
        loadState();
        Timer jsonTimer = new Timer(10, null);
        jsonTimer.addActionListener(e -> {
            if (proceedToSaveLoad) {
                try {
                    saveState();
                    loadState();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else jsonTimer.stop();
        });
        jsonTimer.setCoalesce(true);
        jsonTimer.start();
    }

    public static void saveState() {
        Profile.getCurrent().updateINSTANCE();
        if (Profile.getCurrent() != null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            String jsonWrite = gson.toJson(Profile.getCurrent(), Profile.class);
            try {
                FileWriter writer = new FileWriter(getFilePath(Profile.getCurrent().profileId));
                writer.write(jsonWrite);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadState() throws IOException {
        String id;
        if (Profile.getCurrent() == null) {
            System.out.println(ANSI_GREEN + ENTER_PROFILE_ID_MESSAGE.getValue() + ANSI_RESET);
            id = scanner.next();
            if (id.equals(NEW_PROFILE_CODE.getValue())) {
                Profile.setCurrent(new Profile());
                return;
            }
            File json = new File(JsonFolderPath.getValue() + id + SAVE_FILE_EXTENSION.getValue());
            if (json.exists()) Profile.setCurrent(new ObjectMapper().readValue(json, Profile.class));
            else {
                System.out.println(ANSI_RED + INVALID_PROFILE_MESSAGE.getValue() + ANSI_RESET);
                loadState();
            }
        } else {
            id = Profile.getCurrent().profileId;
            File json = new File(JsonFolderPath.getValue() + id + SAVE_FILE_EXTENSION.getValue());
            Profile.setCurrent(json.exists() ? new ObjectMapper().readValue(json, Profile.class) : new Profile());
        }
    }

    public static String getFilePath(String fileName) {
        return JsonFolderPath.getValue() + fileName + SAVE_FILE_EXTENSION.getValue();
    }
}

