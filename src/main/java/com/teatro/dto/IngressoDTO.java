package com.teatro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teatro.model.Ingresso;
import com.teatro.model.Ingresso.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para transferência de dados de ingresso
 */
public class IngressoDTO {

  private Long id;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long usuarioId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String usuarioNome;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String usuarioCpf;

  @NotNull(message = "ID da sessão é obrigatório")
  private Long sessaoId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String sessaoNome;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String eventoNome;

  @NotNull(message = "ID da área é obrigatório")
  private Long areaId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String areaNome;

  @NotNull(message = "Número da poltrona é obrigatório")
  @Min(value = 1, message = "Número da poltrona deve ser maior que zero")
  private Integer numeroPoltrona;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private BigDecimal valor;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dataCompra;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String codigo;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Status status;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(pattern = "yyyy-MM-dd")
  private java.time.LocalDate dataSessao;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(pattern = "HH:mm")
  private java.time.LocalTime horarioSessao;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String tipoSessao;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dataAtualizacao;

  // Construtores
  public IngressoDTO() {}

  public IngressoDTO(Ingresso ingresso) {
    this.id = ingresso.getId();
    this.sessaoId = ingresso.getSessao().getId();
    this.areaId = ingresso.getArea().getId();
    this.numeroPoltrona = ingresso.getNumeroPoltrona();
    this.valor = ingresso.getValor();
    this.dataCompra = ingresso.getDataCompra();
    this.codigo = ingresso.getCodigo();
    this.status = ingresso.getStatus();
    this.dataAtualizacao = ingresso.getDataAtualizacao();

    // Informações do usuário
    if (ingresso.getUsuario() != null) {
      this.usuarioId = ingresso.getUsuario().getId();
      this.usuarioNome = ingresso.getUsuario().getNome();
      this.usuarioCpf = ingresso.getUsuario().getCpf();
    }

    // Informações da sessão
    if (ingresso.getSessao() != null) {
      this.sessaoNome = ingresso.getSessao().getNome();
      this.dataSessao = ingresso.getSessao().getDataSessao();
      this.horarioSessao = ingresso.getSessao().getHorario();
      this.tipoSessao = ingresso.getSessao().getTipoSessao().getDescricao();

      // Informações do evento
      if (ingresso.getSessao().getEvento() != null) {
        this.eventoNome = ingresso.getSessao().getEvento().getNome();
      }
    }

    // Informações da área
    if (ingresso.getArea() != null) {
      this.areaNome = ingresso.getArea().getNome();
    }
  }

  // Método para converter DTO para entidade
  public Ingresso toEntity() {
    Ingresso ingresso = new Ingresso();
    ingresso.setId(this.id);
    ingresso.setNumeroPoltrona(this.numeroPoltrona);
    ingresso.setStatus(this.status != null ? this.status : Status.RESERVADO);
    return ingresso;
  }

  // Getters e Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public String getUsuarioNome() {
    return usuarioNome;
  }

  public void setUsuarioNome(String usuarioNome) {
    this.usuarioNome = usuarioNome;
  }

  public String getUsuarioCpf() {
    return usuarioCpf;
  }

  public void setUsuarioCpf(String usuarioCpf) {
    this.usuarioCpf = usuarioCpf;
  }

  public Long getSessaoId() {
    return sessaoId;
  }

  public void setSessaoId(Long sessaoId) {
    this.sessaoId = sessaoId;
  }

  public String getSessaoNome() {
    return sessaoNome;
  }

  public void setSessaoNome(String sessaoNome) {
    this.sessaoNome = sessaoNome;
  }

  public String getEventoNome() {
    return eventoNome;
  }

  public void setEventoNome(String eventoNome) {
    this.eventoNome = eventoNome;
  }

  public Long getAreaId() {
    return areaId;
  }

  public void setAreaId(Long areaId) {
    this.areaId = areaId;
  }

  public String getAreaNome() {
    return areaNome;
  }

  public void setAreaNome(String areaNome) {
    this.areaNome = areaNome;
  }

  public Integer getNumeroPoltrona() {
    return numeroPoltrona;
  }

  public void setNumeroPoltrona(Integer numeroPoltrona) {
    this.numeroPoltrona = numeroPoltrona;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public LocalDateTime getDataCompra() {
    return dataCompra;
  }

  public void setDataCompra(LocalDateTime dataCompra) {
    this.dataCompra = dataCompra;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
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

  public String getTipoSessao() {
    return tipoSessao;
  }

  public void setTipoSessao(String tipoSessao) {
    this.tipoSessao = tipoSessao;
  }

  public LocalDateTime getDataAtualizacao() {
    return dataAtualizacao;
  }

  public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
    this.dataAtualizacao = dataAtualizacao;
  }
}
