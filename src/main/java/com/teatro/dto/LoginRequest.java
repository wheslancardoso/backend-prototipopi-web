package com.teatro.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisição de login
 */
public class LoginRequest {

  @NotBlank(message = "CPF ou Email é obrigatório")
  private String identificador; // CPF ou Email

  @NotBlank(message = "Senha é obrigatória")
  private String senha;

  // Construtores
  public LoginRequest() {}

  public LoginRequest(String identificador, String senha) {
    this.identificador = identificador;
    this.senha = senha;
  }

  // Getters e Setters
  public String getIdentificador() {
    return identificador;
  }

  public void setIdentificador(String identificador) {
    this.identificador = identificador;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }
}
