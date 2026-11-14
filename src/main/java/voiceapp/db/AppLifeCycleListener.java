package voiceapp.db;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;

import java.sql.SQLException;

/**
 * Class responsible for listening when the application quits so the database can be closed.
 */
@WebListener
public class AppLifeCycleListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) { }

    public void contextDestroyed(ServletContextEvent event) {
        try {
            DBManager.instance.closeDBConn();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
