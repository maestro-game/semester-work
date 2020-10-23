package listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import html.HtmlManagerImpl;
import repositories.*;
import utils.CookieManager;
import utils.CookieManagerImpl;
import utils.SimpleLoginManager;
import utils.SimpleRegisterManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class ContextListener implements javax.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setServletContextForTemplateLoading(servletContext, "/WEB-INF/templates");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        servletContext.setAttribute("cfg", cfg);
        Properties properties = new Properties();
        try {
            properties.load(servletContext.getResourceAsStream("/WEB-INF/properties/db.properties"));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty("db.jdbc.url"));
        config.setDriverClassName(properties.getProperty("db.jdbc.driver-class-name"));
        config.setUsername(properties.getProperty("db.jdbc.username"));
        config.setPassword(properties.getProperty("db.jdbc.password"));
        config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.jdbc.hikari.max-pool-size")));

        DataSource dataSource = new HikariDataSource(config);
        UsersRepository usersRepository = new UsersRepositoryJdbcImpl(dataSource);
        CommentsRepository commentsRepository = new CommentsRepositoryJdbcImpl(dataSource, usersRepository);
        PostsRepository postsRepository = new PostsRepositoryJdbcImpl(dataSource, usersRepository);
        CookieRepository cookieRepository = new CookieRepositoryJdbcImpl(dataSource);
        CookieManager cookieManager = new CookieManagerImpl(cookieRepository);
        ObjectMapper objectMapper = new ObjectMapper();

        servletContext.setAttribute("cookieManager", cookieManager);
        servletContext.setAttribute("dataSource", dataSource);
        servletContext.setAttribute("htmlManager", new HtmlManagerImpl(usersRepository, commentsRepository, postsRepository));
        servletContext.setAttribute("registerManager", new SimpleRegisterManager(usersRepository, cookieManager));
        servletContext.setAttribute("loginManager", new SimpleLoginManager(usersRepository));
        servletContext.setAttribute("commentRepository", commentsRepository);
        servletContext.setAttribute("objectMapper", objectMapper);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
