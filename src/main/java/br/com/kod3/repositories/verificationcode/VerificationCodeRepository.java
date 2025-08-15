package br.com.kod3.repositories.verificationcode;

import br.com.kod3.models.verificationcode.VerificationCode;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VerificationCodeRepository implements PanacheRepositoryBase<VerificationCode, String> {}
