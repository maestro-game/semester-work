package managers;

import lombok.AllArgsConstructor;
import models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import repositories.ImageRepository;
import repositories.UsersRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class LoginManagerImpl implements LoginManager {
    private UsersRepository usersRepository;
    private ImageRepository imageRepository;
    private PasswordEncoder passwordEncoder;

    //TODO validate by regex
    @Override
    public User login(String id, String password, List<String> warnings) {
        Optional<User> candidate = usersRepository.findById(id);
        if (candidate.isPresent() && passwordEncoder.matches(password, candidate.get().getPassword())) {
            User user = candidate.get();
            user.setImage(imageRepository.pathForUser(user.getId(), user.getImage()));;
            return candidate.get();
        } else {
            warnings.add("Неверный логин или пароль.");
            return null;
        }
    }
}
