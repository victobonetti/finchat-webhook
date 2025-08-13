package br.com.kod3.models.transaction;

import br.com.kod3.models.debt.Debt;
import br.com.kod3.models.recurrence.Recurrence;
import br.com.kod3.models.user.User;
import br.com.kod3.models.util.enums.Category;
import br.com.kod3.models.util.enums.TransactionType;
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
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "value", nullable = false)
  private BigDecimal value;

  @Column(name = "category", nullable = false)
  private Category category;

  @Column(name = "type", nullable = false)
  @Convert(converter = TransactionType.TransactionTypeConverter.class)
  private TransactionType type;

  @Column(name = "currency", nullable = false)
  private String currency;

  @Column(name = "blocked", nullable = false)
  private Boolean blocked;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "debtId")
  private Debt debt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recurrenceId")
  private Recurrence recurrence;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "userId", nullable = false)
  private User user;

  @CreationTimestamp
  @Column(name = "createdAt", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updatedAt", nullable = false)
  private LocalDateTime updatedAt;
}
