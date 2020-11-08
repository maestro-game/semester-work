package managers;

import lombok.AllArgsConstructor;
import models.User;
import repositories.UsersRepository;

import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class RegisterManagerImpl implements RegisterManager {
    private UsersRepository usersRepository;
    private CookieManager cookieManager;

    @Override
    public boolean register(User user, List<String> warnings) {
        try {
            usersRepository.save(user);
            return true;
        } catch (IllegalArgumentException e) {
            SQLException sql = (SQLException) e.getCause();
            switch (sql.getSQLState()) {
                case "23505":
                    warnings.add("Пользователь с таким логином уже существует.");
                    break;
                default:
                    throw new IllegalArgumentException(e);
            }
        }
        return false;
    }
}
