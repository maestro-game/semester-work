package managers;

import lombok.AllArgsConstructor;
import models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import repositories.UsersRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class LoginManagerImpl implements LoginManager {
    private UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;

    //TODO validate by regex
    @Override
    public User login(String id, String password, List<String> warnings) {
        Optional<User> candidate = usersRepository.findById(id);
        if (candidate.isPresent() && passwordEncoder.matches(password, candidate.get().getPassword())) {
            return candidate.get();
        } else {
            warnings.add("Не верный логин или пароль.");
            return null;
        }
    }
}
