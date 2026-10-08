package bryan.david.sagsa.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "curso")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Long id;

    @NotBlank
    @Column(name = "nome_curso", nullable = false, length = 150)
    private String nomeCurso;

    @NotBlank
    @Column(name = "eixo_tecnologico", nullable = false, length = 100)
    private String eixoTecnologico;

    @NotNull
    @Column(name = "carga_horaria_total", nullable = false)
    private Integer cargaHorariaTotal;

    @OneToMany(mappedBy = "curso")
    private List<Ppc> ppcs = new ArrayList<>();
}