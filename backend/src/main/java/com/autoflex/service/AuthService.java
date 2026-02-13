package com.autoflex.service;

import com.autoflex.entity.User;
import com.autoflex.repository.UserRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository userRepository;

    public String login(String username, String password) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        if (!BCrypt.checkpw(password, user.password)) {
            throw new RuntimeException("Senha inválida");
        }

        return Jwt.issuer("autoflex-api")
                .upn(user.username)
                .groups(Set.of(user.role))
                .expiresIn(Duration.ofHours(2))
                .sign();
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
