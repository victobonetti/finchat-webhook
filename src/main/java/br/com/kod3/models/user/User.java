package br.com.kod3.models.user;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "\"user\"")
public class User extends PanacheEntityBase {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "\"monthlyIncome\"")
    private Integer monthlyIncome;

    @Column(name = "\"emailVerified\"")
    private Boolean emailVerified;

    @Column(name = "image")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "\"perfilInvestidor\"", columnDefinition = "\"PerfilInvestidorType\"")
    private PerfilInvestidorType perfilInvestidor;

    @CreationTimestamp
    @Column(name = "\"createdAt\"")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"")
    private LocalDateTime updatedAt;
}

