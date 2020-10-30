package managers;

import models.User;

import java.util.List;

public interface LoginManager {
    User login(String id, String password, List<String> warnings);
}
