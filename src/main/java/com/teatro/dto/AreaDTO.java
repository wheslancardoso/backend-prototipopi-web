package com.teatro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferência de dados de área do teatro
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaDTO {
  private Long id;

  @NotBlank(message = "Nome da área é obrigatório")
  private String nome;

  @NotNull(message = "Preço é obrigatório")
  private Double preco;

  @NotNull(message = "Capacidade total é obrigatória")
  private Integer capacidadeTotal;

  private Long sessaoId; // Para associação com sessão, se necessário
  private Double faturamento;
}
