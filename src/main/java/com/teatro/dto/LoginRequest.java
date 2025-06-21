package com.teatro.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de login
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

  @NotBlank(message = "CPF ou email é obrigatório")
  private String identificador; // CPF ou email

  @NotBlank(message = "Senha é obrigatória")
  private String senha;
}
