import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;


public class ConfigLoader {

    private Properties properties;

    public ConfigLoader(String configFileName) throws IOException {
        properties = new Properties();
        try (InputStream input = new FileInputStream(configFileName)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getValue(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    // public Integer getValue(String key, String defaultValue) {
    //     return properties.getProperty(key, defaultValue);
    // }

        
    // public int getDefaultMaxClicks() {
    //     return Integer.parseInt(properties.getProperty("default.max.clicks", "100"));
    // }

    // public long getDefaultExpiryTime() {
    //     return Long.parseLong(properties.getProperty("default.expiry.time", "86400000"));
    // }
}
