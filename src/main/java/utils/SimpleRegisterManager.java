package utils;

import models.User;
import repositories.UsersRepository;

import java.sql.SQLException;
import java.util.List;

public class SimpleRegisterManager implements RegisterManager {
    private UsersRepository usersRepository;

    @Override
    public boolean register(User user, List<String> warnings) {
        try {
            usersRepository.save(user);
            return true;
        } catch (IllegalArgumentException e) {
            SQLException sql = (SQLException) e.getCause();
            switch (sql.getSQLState()) {
                case "23505" -> warnings.add("Пользователь с таким логином уже существует.");
                default -> throw new IllegalArgumentException(e);
            }
        }
        return false;
    }

    public SimpleRegisterManager(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
}
