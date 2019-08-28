package core.utils;

import java.io.File;

public class FileUtils {
    public static boolean fileExists(String location) {
        File file = new File(location);
        return file.exists();
    }
}
