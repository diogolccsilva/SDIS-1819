package utils;

import java.io.File;

/**
 * Utils
 */
public class Utils {

    public static boolean isInteger(String s) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i), 10) < 0) return false;
        }
        return true;
    }

    public static boolean fileExists(String filePath) {
        return new File(filePath).isFile();
    }
}