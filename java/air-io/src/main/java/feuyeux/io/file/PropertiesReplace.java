package feuyeux.io.file;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author hanl
 */
public class PropertiesReplace {
    public static final String LOG4JAPPENDER_RSYSLOG_THRESHOLD = "log4j.appender.Rsyslog.Threshold";

    public static void main(String[] ss) throws IOException {
        Properties p = new Properties();
        String[] files = {
                "D:\\workspace0\\UTF0\\UTF\\Scheduler\\src\\main\\resources\\log4j_staging.properties",
                "D:\\workspace0\\UTF0\\UTF\\Common\\src\\main\\resources\\log4j_development.properties",
                "D:\\workspace0\\UTF0\\UTF\\Isf\\src\\main\\resources\\log4j_staging.properties\\",
                "D:\\workspace0\\UTF0\\UTF\\TestDirectory\\src\\main\\resources\\log4j_staging.properties"
        };
        for (String filename : files) {
            try (FileWriter fileWriter = new FileWriter(filename); FileReader fileReader = new FileReader(filename)) {
                p.load(fileReader);
                System.out.println(p.getProperty(LOG4JAPPENDER_RSYSLOG_THRESHOLD));
                p.setProperty(LOG4JAPPENDER_RSYSLOG_THRESHOLD, "INFO");
                p.store(fileWriter, null);
            }
        }
    }
}
