package br.com.kod3.models.debt;

import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.user.User;
import br.com.kod3.models.util.enums.Category;
import br.com.kod3.models.util.enums.SituacaoEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "CCTB003_DEBT")
public class Debt extends PanacheEntityBase {

  @Id
  @UuidGenerator
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "totalValue", nullable = false)
  private BigDecimal totalValue;

  @Column(name = "category", nullable = false)
  private Category category;

  @Column(name = "currency", nullable = false)
  private String currency;

  @Column(name = "situacao", nullable = false)
  @Convert(converter = SituacaoEnum.SituacaoConverter.class)
  private SituacaoEnum situacao;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "userId", nullable = false)
  private User user;

  @OneToMany(mappedBy = "debt", fetch = FetchType.LAZY)
  @Getter
  private List<Transaction> transactions;

  @CreationTimestamp
  @Column(name = "createdAt", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updatedAt", nullable = false)
  private LocalDateTime updatedAt;
}
