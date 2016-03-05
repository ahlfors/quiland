import java.lang.reflect.Field;
import sun.misc.*;
/**
 * 本机直接内存溢出
 *-Xmx20M -XX:MaxDirectMemorySize=10M 
 */
public class DirectMemoryOOM {
        private static final int _1MB = 1024*1024;
        public static void main(String[] args) {
                Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        }
}