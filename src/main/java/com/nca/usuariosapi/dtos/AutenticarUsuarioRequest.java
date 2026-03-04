package com.nca.usuariosapi.dtos;

public record AutenticarUsuarioRequest (
        String email,
        String senha
) {
}
