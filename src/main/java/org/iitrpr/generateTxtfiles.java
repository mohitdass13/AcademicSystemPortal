package org.iitrpr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class generateTxtfiles {
    public static void generateTranscript(String entryNo, String Transcript) {
        String home = System.getProperty("user.home");
        String os = System.getProperty("os.name").toLowerCase();
        String dDir;
        if (os.contains("win")) {
            dDir = "\\Documents\\AcademicSystem\\";
        } else if (os.contains("mac")) {
            dDir = "/Documents/AcademicSystem/";
        } else {
            dDir = "/Documents/AcademicSystem/";
        }

        String fullPath = home + dDir + "Transcripts/";
        File folder = new File(fullPath);
        folder.mkdirs();
        String fileName = String.format("%s_Transcript", entryNo);
        File file = new File(fullPath + fileName + ".txt");

        if (file.exists()) {
            int i = 1;
            do {
                file = new File(fullPath + fileName + "(" + i + ").txt");
                i++;
            } while (file.exists());
        }


        try {
            boolean success = file.createNewFile();
            if (success) {
            } else {
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(Transcript);

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

