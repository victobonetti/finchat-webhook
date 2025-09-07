package br.com.kod3.models.recurrence;

import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.user.User;
import br.com.kod3.models.util.enums.Category;
import br.com.kod3.models.util.enums.SituacaoEnum;
import br.com.kod3.models.util.enums.TransactionType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
@Table(name = "CCTB004_RECURRENCE")
public class Recurrence extends PanacheEntityBase {

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

  @Column(name = "currency")
  private String currency;

  @Column(name = "payment_day", nullable = false)
  private LocalDate paymentDay;

  @Enumerated(EnumType.STRING)
  @Column(name = "period", nullable = false)
  private PeriodEnum period;

  @Column(name = "type")
  @Convert(converter = TransactionType.TransactionTypeConverter.class)
  private TransactionType type;

  @Column(name = "situacao")
  @Convert(converter = SituacaoEnum.SituacaoConverter.class)
  private SituacaoEnum situacao;

  @Column(name = "dayOfMonth")
  private Integer dayOfMonth;

  @Column(name = "dayOfWeek")
  private Integer dayOfWeek;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", nullable = false)
  private User user;

  @OneToMany(mappedBy = "recurrence", fetch = FetchType.LAZY)
  @Getter
  private List<Transaction> transactions;

  @CreationTimestamp
  @Column(name = "createdAt")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updatedAt")
  private LocalDateTime updatedAt;
}
