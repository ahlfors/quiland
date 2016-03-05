package feuyeux.io.file;

import feuyeux.io.common.ENV;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;

/**
 *
 * Created by feuyeux@gmail.com
 * Date: Feb 19 2014
 * Time: 5:33 PM
 */
public class ReadingFileTest {
    private static final Logger logger = LogManager.getLogger(ReadingFileTest.class);

    @Test
    public void readingTest() throws IOException {
        logger.debug(BufferedFileReader.readByReader(ENV.TESTING_FILE));
        logger.debug(BufferedFileReader.readByStream(ENV.TESTING_FILE));
        logger.debug(BufferedFileReader.readHybrid(ENV.TESTING_FILE));
    }
}
