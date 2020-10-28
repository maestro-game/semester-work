package models;

import lombok.Builder;
import lombok.Getter;

import java.nio.file.Path;
import java.sql.Date;
import java.util.List;

public class Admin extends User {
    public Admin(String id, String password, String name, String surname, String middleName, Path image, String email, Date birth, String about, List<Category> followCats, List<User> followUsers, List<Post> likes) {
        super(id, password, name, surname, middleName, image, email, birth, about, followCats, followUsers, likes);
    }
}
