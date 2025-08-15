package br.com.kod3.models.verificationcode;

import br.com.kod3.models.user.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "CCTB005_VERIFICATION_CODE")
@Builder
public class VerificationCode extends PanacheEntityBase {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false)
    public String id;

    @Column(name = "code", nullable = false)
    public String code;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    public User user;

    @Column(name = "expiresAt", nullable = false)
    public LocalDateTime expiresAt;

    @Column(name = "isUsed", nullable = false)
    public Boolean isUsed = false;
}
