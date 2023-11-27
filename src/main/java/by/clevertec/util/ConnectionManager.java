package by.clevertec.util;

import by.clevertec.exception.JDBCConnectionException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static by.clevertec.config.LoadProperties.PASSWORD_DB;
import static by.clevertec.config.LoadProperties.URL_DB;
import static by.clevertec.config.LoadProperties.USER_DB;

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
            try {
                Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
                connection = DriverManager.getConnection(URL_DB, USER_DB, PASSWORD_DB);
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
