package by.clevertec.util;

import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Getter
public class YamlUtil {

    private final Map<String, Map<String, String>> yamlMap;

    /**
     * Constructor for uploading a file application.xml from resources and converting it to a map using Yaml.
     */
    public YamlUtil() {

        Yaml yaml = new Yaml();

        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("application.yaml");

        yamlMap = yaml.load(inputStream);
    }
}
