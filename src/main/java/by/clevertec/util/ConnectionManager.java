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
     * Method for getting a connection to the PostgresSQL database.
     * The method reads parameters from the yaml file and creates a new connection.
     * In case of exceptions, the method writes an error message to the logger and throws a JDBCConnectionException.
     *
     * @return connection to PostgresSQL database
     * @throws JDBCConnectionException if the database connection could not be established
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
