package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {
    private String id;
    private String password;
    private String name;
    private String surname;
    private String middleName;
    private String email;
    private Date birth;
    private String about;
    private String image;
    private List<Category> followCats;
    private List<User> followUsers;
    private List<Post> likes;
}
