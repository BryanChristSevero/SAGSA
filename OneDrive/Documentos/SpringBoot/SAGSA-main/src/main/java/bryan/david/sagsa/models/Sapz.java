package bryan.david.sagsa.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = Sapz.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Sapz {

    public static final String TABLE_NAME = "sapz";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true, nullable = false, length = 100)
    @Size(min = 1, max = 100)
    private String titulo;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(length = 255)
    private String descricao;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(length = 255)
    private String desafio;

    @NotNull
    @Column(name = "data_geracao")
    private LocalDateTime dataGeracao;

    @NotNull
    @Column(name = "carga_horaria_total")
    private Integer cargaHorariaTotal;

    @ManyToOne
    @JoinColumn(name = "id_modulo", nullable = false)
    private Modulo modulo;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false, updatable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "sapz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atividade> atividades = new ArrayList<>();
}



/*
SAPZ
 ├── id
 ├── título
 ├── descrição
 ├── desafio
 ├── curso
 ├── módulo
 ├── unidade curricular
 ├── data de criação
 ├── usuário que criou
 */