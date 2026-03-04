package com.nca.usuariosapi.controllers;

import com.nca.usuariosapi.dtos.AutenticarUsuarioRequest;
import com.nca.usuariosapi.dtos.CriarUsuarioRequest;
import com.nca.usuariosapi.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criarUsuario(@RequestBody CriarUsuarioRequest usuarioRequest) {

        try {
            var response = this.usuarioService.criar(usuarioRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/autenticar")
    public ResponseEntity<?> autenticarUsuario(@RequestBody AutenticarUsuarioRequest usuarioRequest) {

        return ResponseEntity.ok("Usuário atualizado com sucesso");
    }
}