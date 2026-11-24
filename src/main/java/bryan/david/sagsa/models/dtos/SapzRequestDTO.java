package bryan.david.sagsa.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SapzRequestDTO(
    @NotBlank(message = "O título é obrigatório")
    String titulo,

    @NotBlank(message = "A descrição é obrigatória")
    String descricao,

    @NotBlank(message = "O desafio é obrigatório")
    String desafio,

    @NotNull(message = "A carga horária total é obrigatória")
    @Positive(message = "A carga horária deve ser maior que zero")
    Integer cargaHorariaTotal,

    @NotNull(message = "O ID do módulo é obrigatório")
    Long idModulo,

    @NotNull(message = "O ID do usuário é obrigatório")
    Long usuarioId
) {}