package managers;

import lombok.AllArgsConstructor;
import models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import repositories.UsersRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class SimpleLoginManager implements LoginManager {
    private UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean isExist(String id, String password, List<String> warnings) {
        Optional<User> user = usersRepository.findById(id);
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }
}
