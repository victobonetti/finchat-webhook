package br.com.kod3.services.verificationcode;

import br.com.kod3.models.user.User;
import br.com.kod3.models.verificationcode.VerificationCode;
import br.com.kod3.repositories.verificationcode.VerificationCodeRepository;
import br.com.kod3.services.user.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class VerificationCodeService {

    @Inject
    VerificationCodeRepository verificationCodeRepository;

    @Inject
    UserService userService;

    public Boolean existsFor(String telefone) {
        Optional<VerificationCode> opt =
                verificationCodeRepository.find("where user.telefone = ?1 and expiresAt > ?2 and isUsed = false",
                                telefone, LocalDateTime.now())
                        .firstResultOptional();

        return opt.isPresent();
    }

    public Optional<String> generateFor(String phone) {
        Optional<User> userOptional = userService.findByPhone(phone);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            verificationCodeRepository.delete("user = ?1 and expiresAt > ?2 and isUsed = false",
                    user, LocalDateTime.now());

            String code = generateSecureRandom6DigitCode();

            VerificationCode verificationCode = VerificationCode.builder()
                    .code(code)
                    .isUsed(false)
                    .expiresAt(LocalDateTime.now().plusMinutes(1)) // Código válido por 1 minuto
                    .build();

            verificationCodeRepository.persist(verificationCode);

            return Optional.of(code);
        }

        return Optional.empty();
    }

    private String generateSecureRandom6DigitCode() {
        SecureRandom random = new SecureRandom();
        int codeInt = random.nextInt(1_000_000);
        return String.format("%06d", codeInt);
    }
}
