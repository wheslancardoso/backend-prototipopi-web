package com.teatro.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferência de dados de sessão
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessaoDTO {

  private Long id;

  private String nome;

  @NotNull(message = "Tipo de sessão é obrigatório")
  private String tipoSessao;

  @NotNull(message = "Data da sessão é obrigatória")
  private LocalDate dataSessao;

  @NotNull(message = "Horário da sessão é obrigatório")
  private LocalTime horario;

  private Boolean ativo;

  private Long eventoId;

  private String eventoNome; // Para facilitar o frontend
}
