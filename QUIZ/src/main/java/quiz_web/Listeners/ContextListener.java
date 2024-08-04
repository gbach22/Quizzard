package quiz_web.Listeners;

import quiz_web.Database.DbConnection;
import quiz_web.Database.UserDbManager;
import quiz_web.Models.User;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static quiz_web.Database.DatabaseInfo.*;

public class ContextListener implements ServletContextListener, HttpSessionListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            DbConnection dbCon = new DbConnection();
            servletContextEvent.getServletContext().setAttribute("database_connection", dbCon);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().removeAttribute("database_connection");
    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().setAttribute("curUser", null);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
