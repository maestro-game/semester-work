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
    String id;
    byte[] password;
    String name;
    String surname;
    String middleName;
    String email;
    Date birth;
    String about;
    List<Category> followCats;
    List<User> followUsers;
    List<Post> likes;
}
