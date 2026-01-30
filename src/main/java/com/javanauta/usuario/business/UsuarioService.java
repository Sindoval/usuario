package com.javanauta.usuario.business;

import com.javanauta.usuario.business.converter.UsuarioConverter;
import com.javanauta.usuario.business.dto.EnderecoDTO;
import com.javanauta.usuario.business.dto.TelefoneDTO;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.entity.Endereco;
import com.javanauta.usuario.infrastructure.entity.Telefone;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import com.javanauta.usuario.infrastructure.exceptions.ConflictException;
import com.javanauta.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.usuario.infrastructure.exceptions.UnauthorizedException;
import com.javanauta.usuario.infrastructure.repository.EnderecoRepository;
import com.javanauta.usuario.infrastructure.repository.TelefoneRepository;
import com.javanauta.usuario.infrastructure.repository.UsuarioRepository;
import com.javanauta.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
  private final UsuarioRepository usuarioRepository;
  private final EnderecoRepository enderecoRepository;
  private final TelefoneRepository telefoneRepository;
  private final UsuarioConverter usuarioConverter;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final AuthenticationManager authenticationManager;

  public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO) {
    emailExiste(usuarioDTO.getEmail());
    usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
    Usuario usuario = usuarioRepository.save(usuarioConverter.toUsuario(usuarioDTO));
    return usuarioConverter.toUsuarioDTO(usuario);
  }

  public String autenticarUsuario(UsuarioDTO usuarioDTO) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha())
      );
      return "Bearer " + jwtUtil.generateToken(authentication.getName());
    } catch (BadCredentialsException | UsernameNotFoundException | AuthorizationDeniedException e) {
      throw new UnauthorizedException("Usuário ou senha inválidos: ", e.getCause());
    }

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
    // trocar retorno por DTO
  public UsuarioDTO buscarUsuarioPorEmail(String email) {
    try {
      return usuarioConverter.toUsuarioDTO(
          (Usuario) usuarioRepository.findByEmail(email).orElseThrow(
              () -> new ResourceNotFoundException("Email não encontrado" + email)
          )
      );
    } catch (ResourceNotFoundException e) {
      throw new ResourceNotFoundException("Email não encontrado " + email);
    }

  }

  public void deletaUsuarioPorEmail(String email) {
    usuarioRepository.deleteByEmail(email);
  }

  public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO usuarioDTO) {
    String email = jwtUtil.extractUsername(token.substring(7));

    usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);

    Usuario usuarioEntity = (Usuario) usuarioRepository.findByEmail(email).orElseThrow(
        () -> new ResourceNotFoundException("Email não encontrado!"));

    Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO, usuarioEntity);

    return usuarioConverter.toUsuarioDTO(usuarioRepository.save(usuario));
  }

  public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {
    Endereco enderecoEntity = enderecoRepository.findById(idEndereco).orElseThrow(
        () -> new ResourceNotFoundException("Id não encontrado" + idEndereco)
    );
    Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, enderecoEntity);

    return usuarioConverter.toEnderecoDTO(enderecoRepository.save(endereco));
  }

  public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO) {
    Telefone telefoneEntity = telefoneRepository.findById(idTelefone).orElseThrow(
        () -> new ResourceNotFoundException("Id não encontrado " + idTelefone)
    );
    Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, telefoneEntity);
    return usuarioConverter.toTelefoneDTO(telefoneRepository.save(telefone));
  }

  public EnderecoDTO cadastroEndereco(String token, EnderecoDTO enderecoDTO) {
    String email = jwtUtil.extractUsername(token.substring(7));
    Usuario usuario = (Usuario) usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado " + email));

    Endereco enderecoEntity = usuarioConverter.toEnderecoEntity
        (enderecoDTO, usuario.getId());

    return usuarioConverter.toEnderecoDTO(enderecoRepository.save(enderecoEntity));
  }

  public TelefoneDTO cadastroTelefone(String token, TelefoneDTO telefoneDTO) {
    String email = jwtUtil.extractUsername(token.substring(7));
    Usuario usuario = (Usuario) usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado " + email));

    Telefone telefone = usuarioConverter.toTelefoneEntity(telefoneDTO, usuario.getId());

    return usuarioConverter.toTelefoneDTO(
        telefoneRepository.save(telefone)
    );
  }
}
