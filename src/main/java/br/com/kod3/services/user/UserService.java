package br.com.kod3.services.user;

import br.com.kod3.models.user.User;
import br.com.kod3.repositories.user.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class UserService {
  private final UserRepository userRepository;

  @Inject
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<User> findByPhone(String phone) {
    return userRepository.find("telefone", phone).stream().findFirst();
  }
}
