package by.clevertec.listener;

import by.clevertec.exception.InitializationSQLException;
import by.clevertec.util.ConnectionManager;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

@Slf4j
@WebListener
public class ContextListener implements ServletContextListener {

    /**
     * A method for executing an SQL script to create a table and insert data.
     * The method reads parameters from a .sql file.
     * In case of exceptions, the method writes an error message to the logger and throws InitializationSQLException.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Connection connection = ConnectionManager.getJDBCConnection();
            Statement statement = connection.createStatement();
            statement.execute(readScript());
        } catch (SQLException e) {
            throw new InitializationSQLException("Error when executing SQL script");
        }
    }

    private static String readScript() {
        try (InputStream in = ContextListener.class.getResourceAsStream("/db/create_sql_V1.sql");
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException ex) {
            log.error("Error when executing SQL script", ex);
            throw new InitializationSQLException(ex.getMessage());
        }
    }
}
