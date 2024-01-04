package by.clevertec.config;

import by.clevertec.adapter.LocalDateTimeAdapter;
import by.clevertec.cach.Cache;
import by.clevertec.cach.factory.CacheFactory;
import by.clevertec.exception.JDBCConnectionException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Configuration
@ComponentScan("by.clevertec")
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class SpringConfig {

    private final Environment env;

    private final CacheFactory cacheFactory;

    @Bean
    public Cache<?,?> cache(){
        return cacheFactory.createCacheType();
    }

    @Bean
    public HikariDataSource hikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(env.getRequiredProperty("spring.datasource.driver"));
        hikariConfig.setJdbcUrl(env.getRequiredProperty("spring.datasource.url"));
        hikariConfig.setUsername(env.getRequiredProperty("spring.datasource.username"));
        hikariConfig.setPassword(env.getRequiredProperty("spring.datasource.password"));
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public Connection getConnection() {
        try {
            return hikariDataSource().getConnection();
        } catch (SQLException e) {
            throw new JDBCConnectionException(e.getMessage());
        }
    }

    @Bean
    public Gson getGson(){
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }
}
