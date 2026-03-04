package com.nca.usuariosapi.dtos;

public record CriarUsuarioRequest(
        String nome,
        String email,
        String senha
) {
}
