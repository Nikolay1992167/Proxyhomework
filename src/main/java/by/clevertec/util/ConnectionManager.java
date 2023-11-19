package by.clevertec.util;

import by.clevertec.config.LoadProperties;
import by.clevertec.exception.JDBCConnectionException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@UtilityClass
public class ConnectionManager {

    public Connection connection;

    /**
     * Метод для получения соединения с базой данных PostgresSQL.
     * Метод читает параметры из файла yaml и создает новое соединение.
     * В случае возникновения исключений, метод записывает сообщение об ошибке в логгер и выбрасывает JDBCConnectionException.
     *
     * @return соединение с базой данных PostgresSQL
     * @throws JDBCConnectionException если не удалось установить соединение с базой данных
     */
    public Connection getJDBCConnection() {
        if (connection == null) {
            String url = new LoadProperties().getURL_DB();
            String user = new LoadProperties().getUSER_DB();
            String password = new LoadProperties().getPASSWORD_DB();
            try {
                Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
                connection = DriverManager.getConnection(url, user, password);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException |
                     ClassNotFoundException | SQLException e) {
                log.error(e.getMessage());
                throw new JDBCConnectionException(e.getMessage());
            }
        }
        return connection;
    }
}
