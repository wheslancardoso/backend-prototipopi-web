package com.teatro.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferência de dados de evento
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO {

  private Long id;

  @NotBlank(message = "Nome do evento é obrigatório")
  private String nome;

  private String descricao;

  private Integer duracaoMinutos;

  private String classificacaoIndicativa;

  private String urlPoster;

  private Boolean ativo;
}
