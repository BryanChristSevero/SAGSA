package bryan.david.sagsa.models;

import java.util.ArrayList;
import java.util.List;
import lombok.*;
import jakarta.*;
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
@EqualsAndHashCode
public class CriterioAvaliacao {

public static final String TABLE_NAME = "criterio";

//Atributos

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id_criterio")
private Long id;

@NotNull
@Size(min = 1, max = 100)
@Column(length = 100)
private String descricao;

//Chaves Estrangeiras

@ManyToOne 
@JoinColumn(name = "id_capacidade", nullable = false)
private Capacidade capacidade;
    
}