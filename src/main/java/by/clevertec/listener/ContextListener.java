package by.clevertec.listener;

import by.clevertec.util.ConnectionManager;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@Slf4j
@WebListener
public class ContextListener implements ServletContextListener {
    private Liquibase liquibase;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (JdbcConnection jdbcConnection = new JdbcConnection(ConnectionManager.getJDBCConnection());
             ClassLoaderResourceAccessor classLoaderResourceAccessor = new ClassLoaderResourceAccessor()) {
            liquibase = new Liquibase("db.changelog/changelog.yaml", classLoaderResourceAccessor,
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection));
            liquibase.update(new Contexts());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (liquibase != null) {
            try {
                liquibase.getDatabase().getConnection().close();
            } catch (DatabaseException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
