package bryan.david.sagsa.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = CriterioAvaliacao.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class CriterioAvaliacao {

    public static final String TABLE_NAME = "criterio";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_criterio")
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(length = 255)
    private String descricao;

    @ManyToOne 
    @JoinColumn(name = "id_capacidade", nullable = false)
    private Capacidade capacidade;
}