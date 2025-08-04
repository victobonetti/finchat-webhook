package br.com.kod3.models.divida;

import br.com.kod3.models.transaction.Category;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.transaction.TransactionType;
import br.com.kod3.models.user.User;
import br.com.kod3.models.util.SituacaoEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "\"DEBT\"")
public class Debt extends PanacheEntityBase {

  @Id
  @UuidGenerator
  @Column(name = "id")
  private String id;

  @Column(name = "business")
  private String business;

  @Column(name = "paidValue")
  @Setter
  private BigDecimal paidValue;

  @Column(name = "totalValue")
  private BigDecimal totalValue;

  @Column(name = "category")
  private Category category;

  @Column(name = "currency")
  private String currency;

  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "situacao", columnDefinition = "public.\"situacaoenum\"")
  private SituacaoEnum situacao;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "\"userId\"", nullable = false)
  private User user;

  @OneToMany(
          mappedBy = "divida",
          cascade = CascadeType.ALL,
          orphanRemoval = true,
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
