package com.teatro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta de login
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

  private String token;
  private UsuarioDTO usuario;
  private String tipoUsuario;
}
