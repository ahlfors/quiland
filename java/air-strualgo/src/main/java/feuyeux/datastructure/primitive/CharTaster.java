package feuyeux.datastructure.primitive;

import java.io.UnsupportedEncodingException;

/**
 * @author feuyeux@gmail.com 2011-1-1
 */
public class CharTaster {
    private static final String utf_8 = "utf-8";
    private static final String GBK = "GBK";
    private static final String unicode = "unicode";
    private static final String GB2312 = "GB2312";

    public static void main(final String[] args) throws UnsupportedEncodingException {
        final char[] chars = Character.toChars(65);
        for (int i = 0; i < chars.length; i++) {
            System.out.println(chars[i]);
        }

        // String string = "A";
        final String string = "汉";

        System.out.println(string.charAt(0));

        byte[] bytes = string.getBytes(CharTaster.GBK);
        System.out.print("GBK ");
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + " ");
        }
        System.out.println();

        bytes = string.getBytes(CharTaster.GB2312);
        System.out.print("GB2312 ");
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + " ");
        }
        System.out.println();

        bytes = string.getBytes(CharTaster.utf_8);
        System.out.print("utf-8 ");
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + " ");
        }
        System.out.println();

        bytes = string.getBytes(CharTaster.unicode);
        System.out.print("unicode ");
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + " ");
        }
        System.out.println();
    }
}
