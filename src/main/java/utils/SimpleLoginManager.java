package utils;

import repositories.UsersRepository;

import java.sql.SQLException;
import java.util.List;

public class SimpleLoginManager implements LoginManager {
    private UsersRepository usersRepository;

    public SimpleLoginManager(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean isExist(String id, byte[] password, List<String> warnings) {
        return usersRepository.isExist(id, password);
    }
}
