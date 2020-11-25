package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FilesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filename = URLDecoder.decode(req.getPathInfo().substring(1), StandardCharsets.UTF_8);
        URI uri = null;
        try {
            uri = new URI(getClass().getClassLoader().getResource("/").toString());
        } catch (URISyntaxException e) {
            System.out.println(e.getMessage());
        }
        File file = new File(uri.getPath() + filename);
        resp.setHeader("Content-Type", getServletContext().getMimeType(filename));
        resp.setHeader("Content-Length", String.valueOf(file.length()));
        resp.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        Files.copy(file.toPath(), resp.getOutputStream());
    }
}

