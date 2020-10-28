package managers;

import java.util.List;

public interface LoginManager {
    boolean isExist(String id, String password, List<String> warnings);
}
