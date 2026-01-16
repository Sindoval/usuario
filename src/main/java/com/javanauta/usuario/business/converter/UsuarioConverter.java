package com.javanauta.usuario.business.converter;

import com.javanauta.usuario.business.dto.EnderecoDTO;
import com.javanauta.usuario.business.dto.TelefoneDTO;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.entity.Endereco;
import com.javanauta.usuario.infrastructure.entity.Telefone;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UsuarioConverter {

  public Usuario toUsuario(UsuarioDTO usuarioDTO) {
    return Usuario.builder()
        .nome(usuarioDTO.getNome())
        .email(usuarioDTO.getEmail())
        .senha(usuarioDTO.getSenha())
        .enderecos(toListEndereco(usuarioDTO.getEnderecos()))
        .telefones(toListTelefone(usuarioDTO.getTelefones()))
        .build();
  }

  public List<Endereco> toListEndereco(List<EnderecoDTO> enderecoDTOS) {
    return enderecoDTOS.stream().map(this::toEndereco).toList();
  }

  public Endereco toEndereco(EnderecoDTO enderecoDTO) {
    return Endereco.builder()
        .rua(enderecoDTO.getRua())
        .cep(enderecoDTO.getCep())
        .cidade(enderecoDTO.getCidade())
        .complemento(enderecoDTO.getComplemento())
        .estado(enderecoDTO.getEstado())
        .numero(enderecoDTO.getNumero())
        .build()
        ;
  }

  public List<Telefone> toListTelefone(List<TelefoneDTO> telefoneDTOS) {
    return telefoneDTOS.stream().map(this::toTelefone).toList();
  }

  public Telefone toTelefone(TelefoneDTO telefoneDTO) {
    return Telefone.builder()
        .numero(telefoneDTO.getNumero())
        .ddd(telefoneDTO.getDdd())
        .build();
  }

  ////////////////////////////////////

  public UsuarioDTO toUsuarioDTO(Usuario usuario) {
    return UsuarioDTO.builder()
        .nome(usuario.getNome())
        .email(usuario.getEmail())
        .senha(usuario.getSenha())
        .enderecos(toListEnderecoDTO(usuario.getEnderecos()))
        .telefones(toListTelefoneDTO(usuario.getTelefones()))
        .build();
  }

  public List<EnderecoDTO> toListEnderecoDTO(List<Endereco> endereco) {
    return endereco.stream().map(this::toEnderecoDTO).toList();
  }

  public EnderecoDTO toEnderecoDTO(Endereco endereco) {
    return EnderecoDTO.builder()
        .id(endereco.getId())
        .rua(endereco.getRua())
        .cep(endereco.getCep())
        .cidade(endereco.getCidade())
        .complemento(endereco.getComplemento())
        .estado(endereco.getEstado())
        .numero(endereco.getNumero())
        .build()
        ;
  }


  public List<TelefoneDTO> toListTelefoneDTO(List<Telefone> telefones) {
    return telefones.stream().map(this::toTelefoneDTO).toList();
  }

  public TelefoneDTO toTelefoneDTO(Telefone telefone) {
    return TelefoneDTO.
        builder()
        .id(telefone.getId())
        .numero(telefone.getNumero())
        .ddd(telefone.getDdd())
        .build();
  }

  /////////////////////////////////////

  public Usuario updateUsuario(UsuarioDTO usuarioDTO, Usuario usuario) {
    return Usuario.builder()
        .id(usuario.getId())
        .email(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : usuario.getEmail())
        .nome(usuarioDTO.getNome() != null ? usuarioDTO.getNome() : usuario.getNome())
        .senha(usuarioDTO.getSenha() != null ? usuarioDTO.getSenha() : usuario.getSenha())
        .enderecos(usuario.getEnderecos())
        .telefones(usuario.getTelefones())
        .build();
  }

  public Endereco updateEndereco(EnderecoDTO enderecoDTO, Endereco endereco) {
    return Endereco.builder()
        .id(endereco.getId())
        .cidade(enderecoDTO.getCidade() != null ? enderecoDTO.getCidade() : endereco.getCidade())
        .rua(enderecoDTO.getRua() != null ? enderecoDTO.getRua() : endereco.getRua())
        .cep(enderecoDTO.getCep() != null ? enderecoDTO.getCep() : endereco.getCep())
        .estado(enderecoDTO.getEstado() != null ? enderecoDTO.getEstado() : endereco.getEstado())
        .numero(enderecoDTO.getNumero() != null ? enderecoDTO.getNumero() : endereco.getNumero())
        .complemento(enderecoDTO.getComplemento() != null ? enderecoDTO.getComplemento() : endereco.getComplemento())
        .build();
  }

  public Telefone updateTelefone(TelefoneDTO telefoneDTO, Telefone telefone) {
    return Telefone.builder()
        .id(telefone.getId())
        .numero(telefoneDTO.getNumero() != null ? telefoneDTO.getNumero() : telefone.getNumero())
        .ddd(telefoneDTO.getDdd() != null ? telefoneDTO.getDdd() : telefone.getDdd())
        .build();
  }

  public Endereco toEnderecoEntity(EnderecoDTO enderecoDTO, Long usuarioId) {
    return Endereco.builder()
        .rua(enderecoDTO.getRua())
        .cep(enderecoDTO.getCep())
        .cidade(enderecoDTO.getCidade())
        .complemento(enderecoDTO.getComplemento())
        .estado(enderecoDTO.getEstado())
        .numero(enderecoDTO.getNumero())
        .usuario_id(usuarioId)
        .build()
        ;
  }

  public Telefone toTelefoneEntity(TelefoneDTO telefoneDTO, Long idUsuario) {
    return Telefone.builder()
        .numero(telefoneDTO.getNumero())
        .ddd(telefoneDTO.getDdd())
        .usuario_id(idUsuario)
        .build();
  }
}
