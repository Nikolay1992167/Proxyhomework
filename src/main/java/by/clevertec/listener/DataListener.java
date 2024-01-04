package by.clevertec.listener;

import by.clevertec.exception.InitializationSQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataListener implements ApplicationListener<ApplicationEvent> {

    private final Connection connection;
    private static final String PATH_SQL_CREATE = "/db/create_sql_V1.sql";
    private static final String PATH_SQL_DROP = "/db/drop_sql_V1.sql";

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        try {
            Statement statement = connection.createStatement();
            if (event instanceof ContextRefreshedEvent) {
                statement.execute(readScript(PATH_SQL_CREATE));
            } else if (event instanceof ContextStoppedEvent) {
                statement.execute(readScript(PATH_SQL_DROP));
            }
        } catch (SQLException e) {
            throw new InitializationSQLException("Error when executing SQL script");
        }
    }

    private static String readScript(String pathSQLScript) {
        try (InputStream in = DataListener.class.getResourceAsStream(pathSQLScript);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException ex) {
            log.error("Error when reading SQL script", ex);
            throw new InitializationSQLException(ex.getMessage());
        }
    }
}
