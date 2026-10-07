package bryan.david.sagsa.models;

import java.util.List;

import org.hibernate.validator.constraints.EAN;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

@NotNull
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@NotNull
private String nome_modulo;

@NotNull
private int carga_horaria;

// Chaves Estrangeiras 

@NotNull
@OneToMany
@JoinColumn(name = "id_sapz")
private Sapz sapz;

@NotNull
@OneToMany
@JoinColumn(name = "id_ppc")
private Ppc ppc;

}
