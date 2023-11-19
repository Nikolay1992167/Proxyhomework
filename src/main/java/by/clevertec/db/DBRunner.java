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
     * Метод для выполнения скрипта SQL создания таблицы и вставки данных.
     * Метод читает параметры из файла .sql.
     * В случае возникновения исключений, метод записывает сообщение об ошибке в логгер и выбрасывает InitializationSQLException.
     */
    public static void runSqlScripts() {
        try {
            Connection connection = ConnectionManager.getJDBCConnection();
            Statement statement = connection.createStatement();
            statement.execute(readScript());
        } catch (SQLException e) {
            log.error("Ошибка при выполнении SQL скрипта", e);
        }
    }

    private static String readScript() {
        Path scriptPath = Paths.get("src/main/resources/db/create_sql_V1.sql");
        try {
            return Files.readString(scriptPath, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            log.error("Ошибка при чтении файла скрипта", ex);
            throw new InitializationSQLException(ex.getMessage());
        }
    }
}
