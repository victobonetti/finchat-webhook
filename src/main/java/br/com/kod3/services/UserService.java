package br.com.kod3.services;

import br.com.kod3.models.user.PerfilInvestidorType;
import br.com.kod3.models.user.User;
import br.com.kod3.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class UserService {
  @Inject UserRepository userRepository;

  public Optional<User> findByPhone(String phone) {
    return userRepository.find("telefone", phone).stream().findFirst();
  }

  @Transactional
  public void atualizaPerfilInvestidor(User user, PerfilInvestidorType perfil) {
    userRepository
        .getEntityManager()
        .createNativeQuery(
            "UPDATE \"user\" SET \"perfilInvestidor\" = ?1 :: \"PerfilInvestidorType\" WHERE id = ?2")
        .setParameter(1, perfil.name())
        .setParameter(2, user.getId())
        .executeUpdate();
    userRepository.flush();
  }
}
