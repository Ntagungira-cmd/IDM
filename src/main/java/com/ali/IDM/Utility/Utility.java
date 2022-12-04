package com.ali.IDM.Utility;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utility {
    public static void createFolder(String path){
        File pathAsFile = new File(path);
        System.out.println(path+"creating folder");
        if (!Files.exists(Paths.get(path))) {
            pathAsFile.mkdirs();
        }
    }
}
