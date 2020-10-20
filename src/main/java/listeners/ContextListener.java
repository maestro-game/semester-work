package listeners;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import html.HtmlManagerIml;
import repositories.*;
import utils.SimpleLoginManager;
import utils.SimpleRegisterManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;

public class ContextListener implements javax.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setServletContextForTemplateLoading(servletContext, "/WEB-INF/templates");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        servletContext.setAttribute("cfg", cfg);
        DataSource dataSource;
        try {
            Context envContext = (Context) new InitialContext().lookup("java:comp/env");
            HikariConfig config = new HikariConfig();
            config.setDataSource(((javax.sql.DataSource) envContext.lookup("jdbc/social")));
            config.setMaximumPoolSize(20);
            dataSource = new HikariDataSource(config);
        } catch (NamingException e) {
            throw new IllegalArgumentException(e);
        }
        servletContext.setAttribute("dataSource", dataSource);
        UsersRepository usersRepository = new UsersRepositoryJdbcImpl(dataSource);
        CommentsRepository commentsRepository = new CommentsRepositoryJdbcImpl(dataSource);
        PostsRepository postsRepository = new PostsRepositoryJdbcImpl(dataSource, usersRepository);
        servletContext.setAttribute("htmlManager", new HtmlManagerIml(usersRepository, commentsRepository, postsRepository));
        servletContext.setAttribute("registerManager", new SimpleRegisterManager(usersRepository));
        servletContext.setAttribute("loginManager", new SimpleLoginManager(usersRepository));
        servletContext.setAttribute("commentRepository", commentsRepository);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
