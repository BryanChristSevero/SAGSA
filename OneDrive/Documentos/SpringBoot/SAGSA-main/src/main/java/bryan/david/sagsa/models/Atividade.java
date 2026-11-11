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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@EqualsAndHashCode
public class Atividade {

public static final String TABLE_NAME = "atividade";

//Atributos

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

//Chaves Estrangeiras

@ManyToOne
@JoinColumn(name = "id_sapz", nullable = false)
private Sapz sapz;

}