package feuyeux.io.file;

import java.net.URL;

public class FilePath {
    public String getClassDir() {
        return this.getClass().getResource("/").getPath();
    }

    public static String getClassDir2() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        URL resource = contextClassLoader.getResource("");
        if (resource != null) {
            return resource.getPath();
        } else {
            return "";
        }
    }

    public static String getUserDir() {
        return System.getProperty("user.dir");
    }
}
