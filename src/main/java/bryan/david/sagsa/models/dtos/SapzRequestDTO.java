package bryan.david.sagsa.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SapzRequestDTO(
    @NotBlank(message = "O título é obrigatório")
    String titulo,

    @NotNull(message = "O ID da unidade curricular é obrigatório")
    Long unidadeCurricularId,

    @NotNull(message = "O ID do instrutor é obrigatório")
    Long instrutorId,

    Long turmaId, // Opcional (pode ser template geral)

    @NotBlank(message = "O tipo de estratégia é obrigatório")
    String tipoEstrategia,

    @NotBlank(message = "A contextualização é obrigatória")
    String contextualizacao,

    @NotBlank(message = "O desafio é obrigatório")
    String desafio,

    @NotBlank(message = "Os resultados esperados são obrigatórios")
    String resultadosEsperados,

    @NotNull(message = "A carga horária é obrigatória")
    @Positive(message = "A carga horária deve ser maior que zero")
    Integer cargaHorariaSa
) {}