package com.nca.usuariosapi.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record CriarUsuarioResponse(
        UUID id,
        String nome,
        String email,
        String perfil,
        LocalDateTime dataHoraCadastro) {
}
