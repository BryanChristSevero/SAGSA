package bryan.david.sagsa.models;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.EAN;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@EqualsAndHashCode
public class Modulo {

public static final String TABLE_NAME = "modulo"; 

@ManyToOne
@JoinColumn(name = "id_modulo", nullable = false)
private Modulo modulo;

@OneToMany(mappedBy = "sapz", cascade = CascadeType.ALL)
private List<Atividade> atividades = new ArrayList<>();

//Tabela Associativa SAPZ_Capacidade
@ManyToMany
@JoinTable(
    name = "sapz_capacidade",
    joinColumns = @JoinColumn(name = "id_sapz"),
    inverseJoinColumns = @JoinColumn(name = "id_capacidade"))
    
    private List<Capacidade> capacidades = new ArrayList<>();

//Tabela Associativa SAPZ_Criterio_Avaliacao

@ManyToMany
@JoinTable(
    name = "sapz_criterio",
    joinColumns = @JoinColumn(name = "id_sapz"),
    inverseJoinColumns = @JoinColumn(name = "id_criterio"))

    private List<CriterioAvaliacao> criterios = new ArrayList<>();

//Chaves Estrangeiras 

@ManyToOne
@JoinColumn(name = "id_ppc", nullable = false)
private Ppc ppc;

@OneToMany(mappedBy = "modulo")
private List<Sapz> sapzs = new ArrayList<>();

@OneToMany(mappedBy = "modulo")
private List<Capacidade> capacidades = new ArrayList<>();

}