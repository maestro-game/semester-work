package html;

public enum Page {
    home("home.ftl"),
    login("login.ftl"),
    messages("messages.ftl"),
    notFound("notFound.ftl"),
    post("post.ftl"),
    profile("profile.ftl"),
    register("register.ftl");

    String path;

    Page(String path) {
        this.path = path;
    }
}
