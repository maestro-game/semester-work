package repositories;

import models.User;

import java.util.Optional;

public class AdminRepositoryJdbcImpl implements AdminRepository{

    @Override
    public Optional<User> findById(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(User entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(User entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateField(String id, String field, String data) {
        throw new UnsupportedOperationException();
    }
}
