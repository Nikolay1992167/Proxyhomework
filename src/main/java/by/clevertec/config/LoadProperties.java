package by.clevertec.config;

import by.clevertec.util.YamlUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class LoadProperties {

    static Map<String, String> postgresData = new YamlUtil().getYamlMap().get("postgres");
    public static final String URL_DB = postgresData.get("url");
    public static final String USER_DB = postgresData.get("user");
    public static final String PASSWORD_DB = postgresData.get("password");

    static Map<String, String> cacheSettings = new YamlUtil().getYamlMap().get("cache");
    public static final int CACHE_CAPACITY = Integer.parseInt(cacheSettings.get("capacity"));
    public static final String CACHE_ALGORITHM = postgresData.get("algorithm");
}
