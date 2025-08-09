package br.com.kod3.models.transaction;

import br.com.kod3.models.debt.Debt;
import br.com.kod3.models.recurrence.Recurrence;
import br.com.kod3.models.user.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "CCTB002_TRANSACTION")
public class Transaction extends PanacheEntityBase {

  @Id
  @UuidGenerator
  @Column(name = "id")
  private String id;

  @Column(name = "business")
  private String business;

  @Column(name = "value")
  private BigDecimal value;

  @Column(name = "category")
  private Category category;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private TransactionType type;

  @Column(name = "currency")
  private String currency;

  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "\"debtId\"", nullable = true)
  private Debt debt;

  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "\"recurrenceId\"", nullable = true)
  private Recurrence recurrence;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "\"userId\"", nullable = false)
  private User user;

  @CreationTimestamp
  @Column(name = "\"createdAt\"")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "\"updatedAt\"")
  private LocalDateTime updatedAt;
}
