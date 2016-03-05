package feuyeux.io.file;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;

/**
 *
 * Created by feuyeux@gmail.com
 * Date: Feb 19 2014
 * Time: 5:50 PM
 */
public class PathTest {

    private static final Logger logger = LogManager.getLogger(PathTest.class);

    @Test
    public void readingTest() throws IOException {
        FilePath test = new FilePath();
        logger.debug("Class located: {}", test.getClassDir());
        logger.debug("Class located: {}", FilePath.getClassDir2());
        logger.debug("User dir: {}", test.getUserDir());
    }
}
