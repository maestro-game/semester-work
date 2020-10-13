package utils;

import models.User;

import java.util.List;

public interface RegisterManager {
    boolean register(User user, List<String> warnings);
}
