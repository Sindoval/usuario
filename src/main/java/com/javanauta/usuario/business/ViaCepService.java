package com.javanauta.usuario.business;

import com.javanauta.usuario.infrastructure.clients.ViaCepClient;
import com.javanauta.usuario.infrastructure.clients.dto.ViaCepDTO;
import com.javanauta.usuario.infrastructure.exceptions.IllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViaCepService {
  private final ViaCepClient viaCepClient;

  public ViaCepDTO biscarDadosEndereco(String cep) {
    return viaCepClient.buscarUsuarioPorEmail(processarCep(cep));
  }

  private String processarCep(String cep) {
    String cepFormatado = cep.replace(" ", "")
        .replace("-", "");

    if(!cepFormatado.matches("[0-9]+") || cepFormatado.length() != 8) {
      throw new IllegalArgumentException("O cep contém caracteres inválidos, favor verificar");
    }
    return cepFormatado;
  }
}
