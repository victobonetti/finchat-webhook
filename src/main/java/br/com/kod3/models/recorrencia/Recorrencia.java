package br.com.kod3.models.recorrencia;

import br.com.kod3.models.transaction.Category;
import br.com.kod3.models.transaction.TransactionType;
import br.com.kod3.models.user.User;
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

  @Column(name = "business")
  private String business;

  @Column(name = "value")
  private BigDecimal value;

  @Column(name = "category")
  private Category category;

  @Column(name = "currency")
  private String currency;

  @Column(name = "payment_day")
  private LocalDate paymentDay;

  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "type", columnDefinition = "public.\"PeriodEnum\"")
  private PeriodEnum period;

  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "type", columnDefinition = "public.\"TransactionType\"")
  private TransactionType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "\"userId\"", nullable = false)
  private User user;

  @CreationTimestamp
  @Column(name = "\"createdAt\"")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "\"updatedAt\"")
  private LocalDateTime updatedAt;
}
