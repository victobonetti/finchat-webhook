package br.com.kod3.services;

import br.com.kod3.models.user.User;
import br.com.kod3.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class UserService {
  @Inject UserRepository userRepository;

  public Optional<User> findByPhone(String phone) {
    return userRepository.find("telefone", phone).stream().findFirst();
  }
}
