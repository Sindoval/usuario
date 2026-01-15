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
        .numero(telefone.getNumero())
        .ddd(telefone.getDdd())
        .build();
  }

}
