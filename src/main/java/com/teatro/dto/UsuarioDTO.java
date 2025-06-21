package com.teatro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferência de dados de usuário
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

  private Long id;

  @NotBlank(message = "Nome é obrigatório")
  private String nome;

  @NotBlank(message = "CPF é obrigatório")
  @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
      message = "CPF deve estar no formato 000.000.000-00")
  private String cpf;

  private String endereco;

  private String telefone;

  @NotBlank(message = "Email é obrigatório")
  @Email(message = "Email deve ser válido")
  private String email;

  private String tipoUsuario;

  // Não incluir senha no DTO de resposta
  private String senha; // Apenas para cadastro/atualização
}
