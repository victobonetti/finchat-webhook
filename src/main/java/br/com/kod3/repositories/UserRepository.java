package br.com.kod3.repositories;

import br.com.kod3.models.user.User;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, String> {
    public Boolean existsByPhone(String phone) {
        return find("telefone", phone).count() == 1;
    }
}
