package bryan.david.sagsa.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ModuloDTOs {

    public record Request(
        @NotNull(message = "ID do PPC é obrigatório")
        Long idPpc,

        @NotBlank(message = "O nome do módulo é obrigatório")
        String nomeModulo,

        @NotBlank(message = "A descrição é obrigatória")
        String descricao
    ) {}

    public record Response(
        Long id,
        String nomeModulo,
        String descricao,
        Long idPpc
    ) {}
}