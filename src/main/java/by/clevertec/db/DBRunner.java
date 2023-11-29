package by.clevertec.db;

import by.clevertec.exception.InitializationSQLException;
import by.clevertec.util.ConnectionManager;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class DBRunner {

    /**
     * A method for executing an SQL script to create a table and insert data.
     * The method reads parameters from a .sql file.
     * In case of exceptions, the method writes an error message to the logger and throws InitializationSQLException.
     */
    public static void runSqlScripts() {
        try {
            Connection connection = ConnectionManager.getJDBCConnection();
            Statement statement = connection.createStatement();
            statement.execute(readScript());
        } catch (SQLException e) {
            throw new InitializationSQLException("Error when executing SQL script");
        }
    }

    private static String readScript() {
        Path scriptPath = Paths.get("src/main/resources/db/create_sql_V1.sql");
        try {
            return Files.readString(scriptPath, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            log.error("Error when executing SQL script", ex);
            throw new InitializationSQLException(ex.getMessage());
        }
    }
}
