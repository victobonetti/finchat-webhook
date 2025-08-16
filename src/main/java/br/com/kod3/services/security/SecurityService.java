package br.com.kod3.services.security;

import br.com.kod3.models.security.TokenDto;
import br.com.kod3.models.user.User;
import jakarta.enterprise.context.ApplicationScoped;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;

import java.time.Duration;

@ApplicationScoped
public class SecurityService {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    public TokenDto generateTokenFor(User user) {

        String token = Jwt.issuer(issuer)
                .upn(user.getId())
                .expiresIn(Duration.ofDays(1))
                .groups("User")
                .claim(Claims.phone_number, user.getTelefone())
                .sign();

        return new TokenDto(token);
    }
}
