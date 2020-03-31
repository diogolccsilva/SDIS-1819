package utils;

import java.io.File;

/**
 * Utils
 */
public class Utils {

    public static boolean isInteger(String s) {
        if (s.isEmpty())
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1)
                    return false;
                else
                    continue;
            }
            if (Character.digit(s.charAt(i), 10) < 0)
                return false;
        }
        return true;
    }

    public static boolean fileExists(String filePath) {
        return new File(filePath).isFile();
    }

    public static boolean isFloat(String s) {
        return s.matches("[0-9]*\\.?[0-9]+");
    }

    public static boolean deleteDirectoryRecursively(File file) /*throws IOException */{
        /*if (file.isDirectory()) {
            File[] entries = file.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    deleteDirectoryRecursively(entry);
                }
            }
        }
        return file.delete();
        if (!file.delete()) {
            throw new IOException("Failed to delete " + file);
        }*/
        File[] files = file.listFiles();
        if(files!=null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteDirectoryRecursively(f);
                } else {
                    f.delete();
                }
            }
        }
        return file.delete();
    }
}