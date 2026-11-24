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
@Table(name = Modulo.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class Modulo {

    public static final String TABLE_NAME = "modulo";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modulo")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100)
    private String nomeModulo;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(length = 255)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_ppc", nullable = false)
    private Ppc ppc;

    @OneToMany(mappedBy = "modulo")
    private List<Sapz> sapzs = new ArrayList<>();

    @OneToMany(mappedBy = "modulo")
    private List<Capacidade> capacidades = new ArrayList<>();
}