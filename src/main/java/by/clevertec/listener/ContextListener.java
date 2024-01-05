package by.clevertec.listener;

import by.clevertec.config.SpringConfig;
import by.clevertec.controller.CarServlet;
import by.clevertec.exception.InitializationSQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
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
@WebListener
@Component
public class ContextListener implements ServletContextListener {

    private static final String PATH_SQL_CREATE = "/db/create_sql_V1.sql";
    private static final String PATH_SQL_DROP = "/db/drop_sql_V1.sql";

    private AnnotationConfigApplicationContext context;

    private Connection connection;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        context = new AnnotationConfigApplicationContext(SpringConfig.class);
        connection = context.getBean(Connection.class);

        CarServlet carServlet = context.getBean(CarServlet.class);

        ServletContext servletContext = sce.getServletContext();
        servletContext.addServlet("cars", carServlet).addMapping("/cars");

        try {
            Statement statement = connection.createStatement();
            statement.execute(readScript(PATH_SQL_CREATE));

        } catch (SQLException e) {
            throw new InitializationSQLException("Error when executing SQL script");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        try {
            Statement statement = connection.createStatement();
            statement.execute(readScript(PATH_SQL_DROP));

        } catch (SQLException e) {

            throw new InitializationSQLException("Error when executing SQL script DROP");
        }
    }

    private static String readScript(String pathSQLScript) {

        try (InputStream in = ContextListener.class.getResourceAsStream(pathSQLScript);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {

            return reader.lines().collect(Collectors.joining("\n"));

        } catch (IOException ex) {

            log.error("Error when reading SQL script", ex);
            throw new InitializationSQLException(ex.getMessage());
        }
    }
}
