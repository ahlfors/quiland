package feuyeux.io.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @author feuyeux@gmail.com 2011-6-18
 */
public class FileProcessor {

    public void changeDirectory(String filename, String oldpath, String newpath, boolean cover) {
        if (!oldpath.equals(newpath)) {
            File oldFile = new File(oldpath + "/" + filename);
            File newFile = new File(newpath + "/" + filename);
            if (newFile.exists()) {// 若在待转移目录下，已经存在待转移文件
                if (cover) {
                    oldFile.renameTo(newFile);
                } else {
                    System.out.println("在新目录下已经存在：" + filename);
                }
            } else {
                oldFile.renameTo(newFile);
            }
        }
    }

    public void copyFile(String src, String dest) throws IOException {
        FileInputStream in = new FileInputStream(src);
        File file = new File(dest);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file);
        int c;
        byte buffer[] = new byte[1024];
        while ((c = in.read(buffer)) != -1) {
            for (int i = 0; i < c; i++) {
                out.write(buffer[i]);
            }
        }
        in.close();
        out.close();
    }

    public void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public void createFile(String path, String filename) throws IOException {
        File file = new File(path + "/" + filename);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public void delDir(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            File[] tmp = dir.listFiles();
            if (tmp != null) {
                for (File aTmp : tmp) {
                    if (aTmp.isDirectory()) {
                        delDir(path + "/" + aTmp.getName());
                    } else {
                        aTmp.delete();
                    }
                }
            }
            dir.delete();
        }
    }

    public void delFile(String path, String filename) {
        File file = new File(path + "/" + filename);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    public String getConsoleInput() throws IOException {
        System.out.println("please type:"); // 不支持中文输入
        byte buffer[] = new byte[1024];
        int count = System.in.read(buffer);
        char[] ch = new char[count - 2]; // 最后两位为结束符，删去不要
        for (int i = 0; i < count - 2; i++) {
            ch[i] = (char) buffer[i];
        }
        return new String(ch);
    }

    public void printToFile(String name, StringBuilder content) throws IOException {
        File file = new File(name);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file, true);
        out.write(content.toString().getBytes("utf-8"));
        out.close();
    }

    public void printToFilePrintStream(String name, StringBuilder content) throws FileNotFoundException, UnsupportedEncodingException {
        FileOutputStream out = new FileOutputStream(name);
        PrintStream p = new PrintStream(out);
        p.println(content.toString());
        p.close();
    }

    public void printToFile1(String name, StringBuilder content) throws FileNotFoundException, UnsupportedEncodingException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(name))) {
            out.write(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> readFileBufferedReader(String path) throws IOException {
        ArrayList<String> contentList = new ArrayList<>();
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
        String temp;
        while ((temp = bufferReader.readLine()) != null) {
            contentList.add(temp.trim());
        }
        bufferReader.close();
        return contentList;
    }

    public void renameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            if (newfile.exists()) {
                System.out.println(newname + "已经存在！");
            } else {
                oldfile.renameTo(newfile);
            }
        }
    }

    public void fileData(File f) {
        System.out.println("Absolute path: " + f.getAbsolutePath() + "\n Can read: " + f.canRead() + "\n Can write: " + f.canWrite() + "\n getName: "
                + f.getName() + "\n getParent: " + f.getParent() + "\n getPath: " + f.getPath() + "\n length: " + f.length() + "\n lastModified: "
                + f.lastModified());
        if (f.isFile()) {
            System.out.println("It's a file");
        } else if (f.isDirectory()) {
            System.out.println("It's a directory");
        }
    }
}