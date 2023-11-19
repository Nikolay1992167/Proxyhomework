package by.clevertec.config;

import by.clevertec.exception.PropertiesException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.InputStream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoadProperties {

    private int CACHE_CAPACITY = configuration.getInt("capacity");
    private final String CACHE_ALGORITHM = configuration.getString("algorithm");
    private final String URL_DB = configuration.getString("url");
    private final String USER_DB = configuration.getString("user");
    private final String PASSWORD_DB = configuration.getString("password");

    private static final Configuration configuration;

    static {
        final InputStream resourceAsStream = LoadProperties.class.getResourceAsStream("/application.yaml");
        final YAMLConfiguration yamlConfiguration = new YAMLConfiguration();
        try {
            yamlConfiguration.read(resourceAsStream);
        } catch (ConfigurationException e) {
            throw new PropertiesException();
        }
        configuration = yamlConfiguration;
    }
}
