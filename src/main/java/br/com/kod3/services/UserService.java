package br.com.kod3.services;

import br.com.kod3.models.user.User;
import br.com.kod3.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    public List<User> findMany(){
        return userRepository.findAll().stream().toList();
    }
}
