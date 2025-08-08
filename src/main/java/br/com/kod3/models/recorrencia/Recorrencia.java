package br.com.kod3.models.recorrencia;

import br.com.kod3.models.transaction.Category;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.transaction.TransactionType;
import br.com.kod3.models.user.User;
import br.com.kod3.models.util.SituacaoEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "\"RECORRENCIA\"")
public class Recorrencia extends PanacheEntityBase {

  @Id
  @UuidGenerator
  @Column(name = "id")
  private String id;

  @Column(name = "business", nullable = false)
  private String business;

  @Column(name = "value", nullable = false)
  private BigDecimal value;

  @Column(name = "category", nullable = false)
  private Category category;

  @Column(name = "currency")
  private String currency;

  @Column(name = "payment_day", nullable = false)
  private LocalDate paymentDay;

  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "period", columnDefinition = "public.\"periodenum\"", nullable = false)
  private PeriodEnum period;

  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "type", columnDefinition = "public.\"transactiontype\"", nullable = false)
  private TransactionType type;

  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "situacao", columnDefinition = "public.\"situacaoenum\"", nullable = false)
  private SituacaoEnum situacao;

  @Column(name = "dayOfMonth")
  private Integer dayOfMonth;

  @Column(name = "dayOfWeek")
  private Integer dayOfWeek;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "\"userId\"", nullable = false)
  private User user;

  @OneToMany(
          mappedBy = "recorrencia",
          fetch = FetchType.LAZY)
  @Getter
  private List<Transaction> transactions;

  @CreationTimestamp
  @Column(name = "\"createdAt\"")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "\"updatedAt\"")
  private LocalDateTime updatedAt;
}
