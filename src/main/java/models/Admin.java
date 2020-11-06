package models;

import java.sql.Date;
import java.util.List;

public class Admin extends User {

    public Admin(String id, String password, String name, String surname, String middleName, String email, Date birth, String about, String image, List<Category> followCats, List<User> followUsers, List<Post> likes) {
        super(id, password, name, surname, middleName, email, birth, about, image, followCats, followUsers, likes);
    }
}
