package com.library.service;

import com.library.dao.UserDAO;
import com.library.model.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = UserDAO.getInstance();
    }

    public void createUser(User user) {
        userDAO.save(user);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public Optional<User> findUserByName(String name) {
        return userDAO.findAll().stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public Optional<User> findUserById(UUID id) {
        return userDAO.findAll().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public void updateUser(User user) {
        userDAO.update(user);
    }

    public void deleteUser(UUID userId) {
        userDAO.delete(userId);
    }
}