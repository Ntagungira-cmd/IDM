package com.ali.IDM.Utility;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utility {
    public static Boolean createFolder(String path){
        File pathAsFile = new File(path);
        if (!Files.exists(Paths.get(path))) {
            return pathAsFile.mkdirs();
        }
        return false;
    }
}
