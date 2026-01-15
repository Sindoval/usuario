package com.javanauta.usuario.business;

import com.javanauta.usuario.business.converter.UsuarioConverter;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import com.javanauta.usuario.infrastructure.exceptions.ConflictException;
import com.javanauta.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
  private final UsuarioRepository usuarioRepository;
  private final UsuarioConverter usuarioConverter;
  private final PasswordEncoder passwordEncoder;

  public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO) {
    emailExiste(usuarioDTO.getEmail());
    usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
    Usuario usuario = usuarioRepository.save(usuarioConverter.toUsuario(usuarioDTO));
    return usuarioConverter.toUsuarioDTO(usuario);
  }

  public void emailExiste(String email) {
    try {
      boolean existe = verificaEmailExistente(email);
      if (existe) {
        throw new ConflictException("Email já cadastrado " + email);
      }
    } catch (ConflictException e) {
      throw new ConflictException("Email já cadastrado " + e.getCause());
    }
  }

  public boolean verificaEmailExistente(String email) {
    return usuarioRepository.existsByEmail(email);
  }
}
