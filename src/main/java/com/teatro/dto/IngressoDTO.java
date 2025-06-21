package com.teatro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferência de dados de ingresso
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngressoDTO {
  private Long id;
  @NotNull(message = "ID do usuário é obrigatório")
  private Long usuarioId;
  @NotNull(message = "ID da sessão é obrigatório")
  private Long sessaoId;
  @NotNull(message = "ID da área é obrigatório")
  private Long areaId;
  @NotNull(message = "Número da poltrona é obrigatório")
  private Integer numeroPoltrona;
  @NotNull(message = "Valor é obrigatório")
  private BigDecimal valor;
  private LocalDateTime dataCompra;
  private String codigo;
  // Extras para facilitar frontend
  private String eventoNome;
  private String areaNome;
  private String usuarioNome;
}
