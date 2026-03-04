package com.nca.usuariosapi.services;

import com.nca.usuariosapi.dtos.AutenticarUsuarioRequest;
import com.nca.usuariosapi.dtos.AutenticarUsuarioResponse;
import com.nca.usuariosapi.dtos.CriarUsuarioRequest;
import com.nca.usuariosapi.dtos.CriarUsuarioResponse;
import com.nca.usuariosapi.entities.Usuario;
import com.nca.usuariosapi.enums.Perfil;
import com.nca.usuariosapi.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public CriarUsuarioResponse criar(CriarUsuarioRequest usuarioRequest) {

        validarNome(usuarioRequest.nome());
        validarEmail(usuarioRequest.email());
        verificarEmailExistente(usuarioRequest.email());
        validarSenha(usuarioRequest.senha());

        var usuario = toUsuario(usuarioRequest);

        usuarioRepository.save(usuario);

        return toUsuarioResponse(usuario);
    }

    public AutenticarUsuarioResponse autenticar(AutenticarUsuarioRequest usuarioRequest) {
        validarEmail(usuarioRequest.email());
        validarSenha(usuarioRequest.senha());

        var usuario =  usuarioRepository.findByEmailSenha(usuarioRequest.email(),
                criptografar(usuarioRequest.senha()));

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        return toAutenticarUsuarioResponse(usuario);
    }

    private AutenticarUsuarioResponse toAutenticarUsuarioResponse(Usuario usuario) {
        return new AutenticarUsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil().toString(),
                LocalDateTime.now(),
                "token"
                );
    }

    private CriarUsuarioResponse toUsuarioResponse(Usuario usuario) {
        return new CriarUsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil().toString(),
                usuario.getDataHoraCadastro());
    }

    private Usuario toUsuario(CriarUsuarioRequest usuarioRequest) {

        var usuario = new Usuario();
        usuario.setNome(usuarioRequest.nome());
        usuario.setEmail(usuarioRequest.email());
        usuario.setSenha(criptografar(usuarioRequest.senha()));
        usuario.setPerfil(Perfil.USUARIO_COMUN);
        usuario.setDataHoraCadastro(LocalDateTime.now());

        return usuario;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().length() < 6) {
            throw new IllegalArgumentException("O nome do usuário é obrigatório e deve ter pelo menos 6 caracteres.");
        }
    }

    private void validarEmail(String email) {
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("O email do usuário é obrigatório e deve ser válido.");
        }
    }

    private void verificarEmailExistente(String email){
        if (usuarioRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("O email do usuário já está cadastrado. Tente outro.");
        }
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("A senha do usuário é obrigatória.");
        }

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        boolean senhaValida = Pattern.matches(regex, senha);

        if (!senhaValida) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 8 caracteres, incluindo letra maiúscula, minúscula, número e caractere especial.");
        }
    }

    private String criptografar(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = md.digest(senha.getBytes());

            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash SHA-256", e);
        }
    }
}
