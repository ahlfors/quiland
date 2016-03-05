package feuyeux.io.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BufferedFileReader {
    public static String readByReader(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String s;
        while ((s = reader.readLine()) != null) {
            sb.append(s).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String readByStream(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = bis.read(buffer)) != -1) {
            String s = new String(buffer, 0, bytesRead);
            sb.append(s).append("\n");
        }
        bis.close();
        return sb.toString();
    }

    public static String readHybrid(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
        BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
        String s;
        while ((s = reader.readLine()) != null) {
            sb.append(s).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}