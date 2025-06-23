package com.teatro.dto;

import java.util.List;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CompraIngressoRequest {

  @NotNull(message = "ID da sessão é obrigatório")
  private Long sessaoId;

  @NotNull(message = "ID da área é obrigatório")
  private Long areaId;

  @NotEmpty(message = "Lista de poltronas é obrigatória")
  @Size(min = 1, max = 10, message = "Pode comprar entre 1 e 10 poltronas por vez")
  private List<@Min(value = 1,
      message = "Número da poltrona deve ser maior que zero") Integer> poltronas;

  @NotNull(message = "ID do usuário é obrigatório")
  private Long usuarioId;

  // Construtores
  public CompraIngressoRequest() {}

  public CompraIngressoRequest(Long sessaoId, Long areaId, List<Integer> poltronas,
      Long usuarioId) {
    this.sessaoId = sessaoId;
    this.areaId = areaId;
    this.poltronas = poltronas;
    this.usuarioId = usuarioId;
  }

  // Getters e Setters
  public Long getSessaoId() {
    return sessaoId;
  }

  public void setSessaoId(Long sessaoId) {
    this.sessaoId = sessaoId;
  }

  public Long getAreaId() {
    return areaId;
  }

  public void setAreaId(Long areaId) {
    this.areaId = areaId;
  }

  public List<Integer> getPoltronas() {
    return poltronas;
  }

  public void setPoltronas(List<Integer> poltronas) {
    this.poltronas = poltronas;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }
}
