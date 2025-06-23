package com.teatro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

public class CompraIngressoResponse {

  private String codigoCompra;
  private List<IngressoDTO> ingressos;
  private BigDecimal valorTotal;
  private Integer totalPoltronas;
  private String eventoNome;
  private String sessaoNome;
  private String areaNome;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private java.time.LocalDate dataSessao;

  @JsonFormat(pattern = "HH:mm")
  private java.time.LocalTime horarioSessao;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dataCompra;

  private Integer pontosGanhos;
  private String nivelFidelidade;

  // Construtores
  public CompraIngressoResponse() {}

  public CompraIngressoResponse(String codigoCompra, List<IngressoDTO> ingressos,
      BigDecimal valorTotal, Integer totalPoltronas, String eventoNome, String sessaoNome,
      String areaNome, java.time.LocalDate dataSessao, java.time.LocalTime horarioSessao,
      LocalDateTime dataCompra, Integer pontosGanhos, String nivelFidelidade) {
    this.codigoCompra = codigoCompra;
    this.ingressos = ingressos;
    this.valorTotal = valorTotal;
    this.totalPoltronas = totalPoltronas;
    this.eventoNome = eventoNome;
    this.sessaoNome = sessaoNome;
    this.areaNome = areaNome;
    this.dataSessao = dataSessao;
    this.horarioSessao = horarioSessao;
    this.dataCompra = dataCompra;
    this.pontosGanhos = pontosGanhos;
    this.nivelFidelidade = nivelFidelidade;
  }

  // Getters e Setters
  public String getCodigoCompra() {
    return codigoCompra;
  }

  public void setCodigoCompra(String codigoCompra) {
    this.codigoCompra = codigoCompra;
  }

  public List<IngressoDTO> getIngressos() {
    return ingressos;
  }

  public void setIngressos(List<IngressoDTO> ingressos) {
    this.ingressos = ingressos;
  }

  public BigDecimal getValorTotal() {
    return valorTotal;
  }

  public void setValorTotal(BigDecimal valorTotal) {
    this.valorTotal = valorTotal;
  }

  public Integer getTotalPoltronas() {
    return totalPoltronas;
  }

  public void setTotalPoltronas(Integer totalPoltronas) {
    this.totalPoltronas = totalPoltronas;
  }

  public String getEventoNome() {
    return eventoNome;
  }

  public void setEventoNome(String eventoNome) {
    this.eventoNome = eventoNome;
  }

  public String getSessaoNome() {
    return sessaoNome;
  }

  public void setSessaoNome(String sessaoNome) {
    this.sessaoNome = sessaoNome;
  }

  public String getAreaNome() {
    return areaNome;
  }

  public void setAreaNome(String areaNome) {
    this.areaNome = areaNome;
  }

  public java.time.LocalDate getDataSessao() {
    return dataSessao;
  }

  public void setDataSessao(java.time.LocalDate dataSessao) {
    this.dataSessao = dataSessao;
  }

  public java.time.LocalTime getHorarioSessao() {
    return horarioSessao;
  }

  public void setHorarioSessao(java.time.LocalTime horarioSessao) {
    this.horarioSessao = horarioSessao;
  }

  public LocalDateTime getDataCompra() {
    return dataCompra;
  }

  public void setDataCompra(LocalDateTime dataCompra) {
    this.dataCompra = dataCompra;
  }

  public Integer getPontosGanhos() {
    return pontosGanhos;
  }

  public void setPontosGanhos(Integer pontosGanhos) {
    this.pontosGanhos = pontosGanhos;
  }

  public String getNivelFidelidade() {
    return nivelFidelidade;
  }

  public void setNivelFidelidade(String nivelFidelidade) {
    this.nivelFidelidade = nivelFidelidade;
  }
}
