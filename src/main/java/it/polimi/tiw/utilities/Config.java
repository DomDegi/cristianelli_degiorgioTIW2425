package it.polimi.tiw.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static Properties props = null;

    public static Properties getProperties() throws IOException {
        if (props == null) {
            props = new Properties();
            try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new IOException("Impossibile trovare config.properties nel classpath.");
                }
                props.load(input);
            }
        }
        return props;
    }
}
