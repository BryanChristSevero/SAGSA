package bryan.david.sagsa.dtos;

public record UsuarioResponseDTO(
    Long id,
    String nome,
    String email,
    String perfilNome,
    Boolean ativo
) {}