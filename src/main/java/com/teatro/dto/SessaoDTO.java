package com.teatro.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teatro.model.Sessao;
import com.teatro.model.Sessao.TipoSessao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para transferência de dados de sessão
 */
public class SessaoDTO {

  private Long id;

  @NotBlank(message = "Nome da sessão é obrigatório")
  @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
  private String nome;

  @NotNull(message = "Tipo de sessão é obrigatório")
  private TipoSessao tipoSessao;

  @NotNull(message = "Data da sessão é obrigatória")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dataSessao;

  @NotNull(message = "Horário da sessão é obrigatório")
  @JsonFormat(pattern = "HH:mm")
  private LocalTime horario;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long eventoId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String eventoNome;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Boolean ativa;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private java.time.LocalDateTime dataCriacao;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private java.time.LocalDateTime dataAtualizacao;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private List<AreaDTO> areas;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer totalAreas;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer totalPoltronas;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer poltronasOcupadas;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer poltronasDisponiveis;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Double percentualOcupacao;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Double faturamentoTotal;

  // Construtores
  public SessaoDTO() {}

  public SessaoDTO(Sessao sessao) {
    this.id = sessao.getId();
    this.nome = sessao.getNome();
    this.tipoSessao = sessao.getTipoSessao();
    this.dataSessao = sessao.getDataSessao();
    this.horario = sessao.getHorario();
    this.ativa = sessao.getAtiva();
    this.dataCriacao = sessao.getDataCriacao();
    this.dataAtualizacao = sessao.getDataAtualizacao();

    // Informações do evento
    if (sessao.getEvento() != null) {
      this.eventoId = sessao.getEvento().getId();
      this.eventoNome = sessao.getEvento().getNome();
    }

    // Converter áreas para DTOs se existirem
    if (sessao.getAreas() != null) {
      this.areas = sessao.getAreas().stream().map(AreaDTO::new).collect(Collectors.toList());
      this.totalAreas = this.areas.size();

      // Calcular estatísticas de poltronas
      this.totalPoltronas = this.areas.stream()
          .mapToInt(area -> area.getCapacidadeTotal() != null ? area.getCapacidadeTotal() : 0)
          .sum();

      this.poltronasOcupadas = this.areas.stream()
          .mapToInt(area -> area.getPoltronasOcupadas() != null ? area.getPoltronasOcupadas() : 0)
          .sum();

      this.poltronasDisponiveis = this.totalPoltronas - this.poltronasOcupadas;

      if (this.totalPoltronas > 0) {
        this.percentualOcupacao = (double) this.poltronasOcupadas / this.totalPoltronas * 100;
      }

      this.faturamentoTotal = this.areas.stream()
          .mapToDouble(
              area -> area.getFaturamento() != null ? area.getFaturamento().doubleValue() : 0.0)
          .sum();
    }
  }

  // Método para converter DTO para entidade
  public Sessao toEntity() {
    Sessao sessao = new Sessao();
    sessao.setId(this.id);
    sessao.setNome(this.nome);
    sessao.setTipoSessao(this.tipoSessao);
    sessao.setDataSessao(this.dataSessao);
    sessao.setHorario(this.horario);
    sessao.setAtiva(this.ativa != null ? this.ativa : true);
    return sessao;
  }

  // Getters e Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public TipoSessao getTipoSessao() {
    return tipoSessao;
  }

  public void setTipoSessao(TipoSessao tipoSessao) {
    this.tipoSessao = tipoSessao;
  }

  public LocalDate getDataSessao() {
    return dataSessao;
  }

  public void setDataSessao(LocalDate dataSessao) {
    this.dataSessao = dataSessao;
  }

  public LocalTime getHorario() {
    return horario;
  }

  public void setHorario(LocalTime horario) {
    this.horario = horario;
  }

  public Long getEventoId() {
    return eventoId;
  }

  public void setEventoId(Long eventoId) {
    this.eventoId = eventoId;
  }

  public String getEventoNome() {
    return eventoNome;
  }

  public void setEventoNome(String eventoNome) {
    this.eventoNome = eventoNome;
  }

  public Boolean getAtiva() {
    return ativa;
  }

  public void setAtiva(Boolean ativa) {
    this.ativa = ativa;
  }

  public java.time.LocalDateTime getDataCriacao() {
    return dataCriacao;
  }

  public void setDataCriacao(java.time.LocalDateTime dataCriacao) {
    this.dataCriacao = dataCriacao;
  }

  public java.time.LocalDateTime getDataAtualizacao() {
    return dataAtualizacao;
  }

  public void setDataAtualizacao(java.time.LocalDateTime dataAtualizacao) {
    this.dataAtualizacao = dataAtualizacao;
  }

  public List<AreaDTO> getAreas() {
    return areas;
  }

  public void setAreas(List<AreaDTO> areas) {
    this.areas = areas;
  }

  public Integer getTotalAreas() {
    return totalAreas;
  }

  public void setTotalAreas(Integer totalAreas) {
    this.totalAreas = totalAreas;
  }

  public Integer getTotalPoltronas() {
    return totalPoltronas;
  }

  public void setTotalPoltronas(Integer totalPoltronas) {
    this.totalPoltronas = totalPoltronas;
  }

  public Integer getPoltronasOcupadas() {
    return poltronasOcupadas;
  }

  public void setPoltronasOcupadas(Integer poltronasOcupadas) {
    this.poltronasOcupadas = poltronasOcupadas;
  }

  public Integer getPoltronasDisponiveis() {
    return poltronasDisponiveis;
  }

  public void setPoltronasDisponiveis(Integer poltronasDisponiveis) {
    this.poltronasDisponiveis = poltronasDisponiveis;
  }

  public Double getPercentualOcupacao() {
    return percentualOcupacao;
  }

  public void setPercentualOcupacao(Double percentualOcupacao) {
    this.percentualOcupacao = percentualOcupacao;
  }

  public Double getFaturamentoTotal() {
    return faturamentoTotal;
  }

  public void setFaturamentoTotal(Double faturamentoTotal) {
    this.faturamentoTotal = faturamentoTotal;
  }
}
