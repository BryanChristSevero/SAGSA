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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = Atividade.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class Atividade {

    public static final String TABLE_NAME = "atividade";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atividade")
    private Long id;

    @NotNull
    @Column(name = "descricao_atividade", columnDefinition = "TEXT", nullable = false)
    private String descricaoAtividade;

    @NotNull
    @Column(name = "carga_horaria", nullable = false)
    private Integer cargaHoraria;

    @ManyToOne
    @JoinColumn(name = "id_sapz", nullable = false)
    private Sapz sapz;
}