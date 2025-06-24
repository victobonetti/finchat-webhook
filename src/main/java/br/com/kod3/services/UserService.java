package br.com.kod3.services;

import br.com.kod3.models.user.PerfilInvestidorType;
import br.com.kod3.models.user.User;
import br.com.kod3.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    public List<User> findMany(){
        return userRepository.findAll().stream().toList();
    }

    public Optional<User> findByPhone(String phone){
        return userRepository.find("telefone", phone).stream().findFirst();
    }

    public void atualizaPerfilInvestidor(User user, PerfilInvestidorType perfil) {
        User.update("perfilInvestidor = ?1 where id = ?2", perfil, user.getId());
        User.flush();
    }
}
