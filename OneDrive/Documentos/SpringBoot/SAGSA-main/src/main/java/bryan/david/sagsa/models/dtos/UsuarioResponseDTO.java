package bryan.david.sagsa.models.dtos;

import bryan.david.sagsa.models.Usuario;

public record UsuarioResponseDTO(
    Long idUsuario,
    String nome,
    String email,
    Usuario.TipoPerfil tipoPerfil
) {}