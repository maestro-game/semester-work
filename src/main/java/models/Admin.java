package models;

import lombok.Builder;
import lombok.Getter;

import java.sql.Date;
import java.util.List;

public class Admin extends User {
    public Admin(String id, byte[] password, String name, String surname, String middleName, String image, String email, Date birth, String about, List<Category> followCats, List<User> followUsers, List<Post> likes) {
        super(id, password, name, surname, middleName, image, email, birth, about, followCats, followUsers, likes);
    }
}
