package servlets;

import managers.HtmlManager;
import managers.Page;
import managers.TemplateManager;
import models.Category;
import models.Post;
import models.User;
import repositories.ImageRepository;
import repositories.PostsRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@MultipartConfig
public class PostServlet extends HttpServlet {
    HtmlManager htmlManager;
    PostsRepository postsRepository;
    TemplateManager templateManager;
    ImageRepository imageRepository;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        htmlManager = (HtmlManager) context.getAttribute("htmlManager");
        postsRepository = (PostsRepository) context.getAttribute("postsRepository");
        templateManager = (TemplateManager) context.getAttribute("templateManager");
        imageRepository = (ImageRepository) context.getAttribute("imageRepository");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> root = new HashMap<>();
        User user = (User) request.getAttribute("user");
        int page = Integer.parseInt(request.getParameter("page"));
        root.put("page", page);
        templateManager.write(htmlManager.render(Page.post, user, request.getRequestURI().substring(6), root), request, response, root);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = (User) request.getAttribute("user");
        if (user != null) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            try {
                Part part = request.getPart("image");
                if (part.getSize() == 0) {
                    //TODO send error
                    response.setStatus(400);
                    return;
                }
                String sfn = part.getSubmittedFileName();
                Long postId = postsRepository.saveReturningId(Post.builder()
                        .author(user)
                        .timestamp(timestamp)
                        .description(request.getParameter("description"))
                        .image(sfn.substring(sfn.lastIndexOf('.')))
                        .specie(new Category(Long.valueOf(request.getParameter("specie")), null))
                        .build());
                imageRepository.saveForPost(part, postId);
                response.getWriter().write(postId.toString());
                response.setStatus(200);
            } catch (IllegalArgumentException e) {
                if (e.getCause().getClass() != SQLException.class) {
                    throw e;
                }
                response.setStatus(400);
            }
        } else {
            response.getWriter().write("redirect");
            response.setStatus(400);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User id = (User) request.getAttribute("user");
        if (id != null) {
            Optional<Post> post = postsRepository.findById(Long.valueOf(request.getParameter("id")));
            if (post.isPresent() && post.get().getAuthor().getId().equals(id.getId())) {
                try {
                    postsRepository.updateDescription(post.get().getId(), request.getParameter("text"));
                } catch (IllegalArgumentException e) {
                    if (e.getCause().getClass() != SQLException.class) {
                        throw e;
                    }
                    response.setStatus(400);
                }
                response.setStatus(200);
            } else {
                response.setStatus(400);
            }
        } else {
            response.getWriter().write("redirect");
            response.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User id = (User) request.getAttribute("user");
        if (id != null) {
            Optional<Post> post = postsRepository.findById(Long.valueOf(request.getParameter("id")));
            if (post.isPresent() && post.get().getAuthor().getId().equals(id.getId())) {
                try {
                    postsRepository.deleteById(post.get().getId());
                    response.setStatus(200);
                } catch (IllegalArgumentException e) {
                    if (e.getCause().getClass() != SQLException.class) {
                        throw e;
                    }
                    response.setStatus(400);
                }
            } else {
                response.setStatus(400);
            }
        } else {
            response.getWriter().write("redirect");
            response.setStatus(400);
        }
    }
}
