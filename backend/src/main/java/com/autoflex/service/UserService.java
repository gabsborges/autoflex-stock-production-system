package com.autoflex.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.mindrot.jbcrypt.BCrypt;

import com.autoflex.entity.User;

@Transactional
@ApplicationScoped
public class UserService {


    public User createUser(String username, String password) {
        User user = new User();
        user.username = username;
        user.password = hashPassword(password);
        user.role = "USER";
        user.persist();
        return user;
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public User updateRole(User user, String newRole) {
        user.role = newRole;
        user.persist();
        return user;
    }
}
