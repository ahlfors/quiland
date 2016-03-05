package loading;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Enumeration;
import java.util.Properties;

public class TestClassLoader {
    private final static Logger logger = LogManager.getLogger(TestClassLoader.class);

    public static void main(String[] args) {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader extClassLoader = systemClassLoader.getParent();
        ClassLoader pcl = extClassLoader.getParent();
        logger.debug(pcl == null);
        /*sun.misc.Launcher.AppClassLoader*/
        logger.debug(systemClassLoader.getClass().getCanonicalName());
        /*sun.misc.Launcher.AppClassLoader*/
        logger.debug(extClassLoader.getClass().getCanonicalName());
        logger.debug("java.ext.dirs={}", System.getProperty("java.ext.dirs"));

        Properties p = System.getProperties();
        p.list(System.out);
    }
}
