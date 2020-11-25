package listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import managers.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import repositories.*;

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

        JdbcTemplate jdbcTemplate = new JdbcTemplateImpl(dataSource);

        properties = new Properties();
        try {
            properties.load(servletContext.getResourceAsStream("/WEB-INF/properties/images.properties"));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        ImageRepository imageRepository = new ImageRepositoryImpl(properties.getProperty("images.basePath"));

        CategoryRepository categoryRepository = new CategoryRepositoryImpl(jdbcTemplate);
        UsersRepository usersRepository = new UsersRepositoryJdbcImpl(jdbcTemplate, imageRepository);
        CommentsRepository commentsRepository = new CommentsRepositoryJdbcImpl(jdbcTemplate, usersRepository);
        PostsRepository postsRepository = new PostsRepositoryJdbcImpl(jdbcTemplate, usersRepository, categoryRepository);
        CookieRepository cookieRepository = new CookieRepositoryJdbcImpl(jdbcTemplate);
        CookieManager cookieManager = new CookieManagerImpl(cookieRepository);
        LikesRepository likesRepository = new LikesRepositoryJdbcImpl(jdbcTemplate);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        FollowCatsRepository followCatsRepository = new FollowCatsRepositoryJdbcImpl(jdbcTemplate, categoryRepository);
        FollowUsersRepository followUsersRepository = new FollowUsersRepositoryJdbcImpl(jdbcTemplate, usersRepository);

        servletContext.setAttribute("cfg", cfg);
        servletContext.setAttribute("followCatsRepository", followCatsRepository);
        servletContext.setAttribute("followUsersRepository", followUsersRepository);
        servletContext.setAttribute("cookieManager", cookieManager);
        servletContext.setAttribute("templateManager", new TemplateManagerImpl());
        servletContext.setAttribute("imageRepository", imageRepository);
        servletContext.setAttribute("jdbcTemplate", jdbcTemplate);
        servletContext.setAttribute("htmlManager", new HtmlManagerImpl(usersRepository, commentsRepository, postsRepository, likesRepository, imageRepository, categoryRepository, followUsersRepository, followCatsRepository));
        servletContext.setAttribute("registerManager", new RegisterManagerImpl(usersRepository, cookieManager));
        servletContext.setAttribute("loginManager", new LoginManagerImpl(usersRepository, imageRepository, passwordEncoder));
        servletContext.setAttribute("commentRepository", commentsRepository);
        servletContext.setAttribute("objectMapper", new ObjectMapper());
        servletContext.setAttribute("passwordEncoder", passwordEncoder);
        servletContext.setAttribute("likesRepository", likesRepository);
        servletContext.setAttribute("usersRepository", usersRepository);
        servletContext.setAttribute("categoryRepository", categoryRepository);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
