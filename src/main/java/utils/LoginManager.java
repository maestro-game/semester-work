package utils;

import java.util.List;

public interface LoginManager {
    boolean isExist(String id, byte[] password, List<String> warnings);
}
