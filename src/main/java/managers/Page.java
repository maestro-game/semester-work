package managers;

public enum Page {
    feed("feed.ftl"),
    follow("follow.ftl"),
    home("home.ftl"),
    login("login.ftl"),
    messages("messages.ftl"),
    notFound("notFound.ftl"),
    post("post.ftl"),
    profile("profile.ftl"),
    profileInfo("profileInfo.ftl"),
    register("register.ftl"),
    search("search.ftl");

    String path;

    Page(String path) {
        this.path = path;
    }
}
