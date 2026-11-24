package bryan.david.sagsa.models;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = Capacidade.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class Capacidade {

    public static final String TABLE_NAME = "capacidade";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_capacidade")
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(length = 255)
    private String descricao;

    @NotNull
    @Column(name = "tipo_capacidade", nullable = false, length = 30)
    private String tipoCapacidade;

    @ManyToOne
    @JoinColumn(name = "id_modulo", nullable = false)
    private Modulo modulo;

    @OneToMany(mappedBy = "capacidade")
    private List<CriterioAvaliacao> criterios = new ArrayList<>();
}