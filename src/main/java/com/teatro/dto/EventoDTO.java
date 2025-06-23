package com.teatro.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teatro.model.Evento;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para transferência de dados de evento
 */
public class EventoDTO {

  private Long id;

  @NotBlank(message = "Nome do evento é obrigatório")
  @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
  private String nome;

  @NotBlank(message = "Descrição é obrigatória")
  @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
  private String descricao;

  @Size(max = 255, message = "Poster deve ter no máximo 255 caracteres")
  private String poster;

  @NotNull(message = "Duração é obrigatória")
  @Min(value = 30, message = "Duração mínima é 30 minutos")
  @Max(value = 300, message = "Duração máxima é 300 minutos")
  private Integer duracaoMinutos;

  @Size(max = 50, message = "Classificação indicativa deve ter no máximo 50 caracteres")
  private String classificacaoIndicativa;

  @Size(max = 255, message = "URL do poster deve ter no máximo 255 caracteres")
  private String urlPoster;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Boolean ativo;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dataCriacao;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dataAtualizacao;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer totalSessoes;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer totalIngressosVendidos;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Double faturamentoTotal;

  // Construtores
  public EventoDTO() {}

  public EventoDTO(Evento evento) {
    this.id = evento.getId();
    this.nome = evento.getNome();
    this.descricao = evento.getDescricao();
    this.poster = evento.getPoster();
    this.duracaoMinutos = evento.getDuracaoMinutos();
    this.classificacaoIndicativa = evento.getClassificacaoIndicativa();
    this.urlPoster = evento.getUrlPoster();
    this.ativo = evento.getAtivo();
    this.dataCriacao = evento.getDataCriacao();
    this.dataAtualizacao = evento.getDataAtualizacao();

    // Contar sessões se existirem
    if (evento.getSessoes() != null) {
      this.totalSessoes = evento.getSessoes().size();
    }
  }

  // Método para converter DTO para entidade
  public Evento toEntity() {
    Evento evento = new Evento();
    evento.setId(this.id);
    evento.setNome(this.nome);
    evento.setDescricao(this.descricao);
    evento.setPoster(this.poster);
    evento.setDuracaoMinutos(this.duracaoMinutos);
    evento.setClassificacaoIndicativa(this.classificacaoIndicativa);
    evento.setUrlPoster(this.urlPoster);
    evento.setAtivo(this.ativo != null ? this.ativo : true);
    return evento;
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

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getPoster() {
    return poster;
  }

  public void setPoster(String poster) {
    this.poster = poster;
  }

  public Integer getDuracaoMinutos() {
    return duracaoMinutos;
  }

  public void setDuracaoMinutos(Integer duracaoMinutos) {
    this.duracaoMinutos = duracaoMinutos;
  }

  public String getClassificacaoIndicativa() {
    return classificacaoIndicativa;
  }

  public void setClassificacaoIndicativa(String classificacaoIndicativa) {
    this.classificacaoIndicativa = classificacaoIndicativa;
  }

  public String getUrlPoster() {
    return urlPoster;
  }

  public void setUrlPoster(String urlPoster) {
    this.urlPoster = urlPoster;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
  }

  public LocalDateTime getDataCriacao() {
    return dataCriacao;
  }

  public void setDataCriacao(LocalDateTime dataCriacao) {
    this.dataCriacao = dataCriacao;
  }

  public LocalDateTime getDataAtualizacao() {
    return dataAtualizacao;
  }

  public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
    this.dataAtualizacao = dataAtualizacao;
  }

  public Integer getTotalSessoes() {
    return totalSessoes;
  }

  public void setTotalSessoes(Integer totalSessoes) {
    this.totalSessoes = totalSessoes;
  }

  public Integer getTotalIngressosVendidos() {
    return totalIngressosVendidos;
  }

  public void setTotalIngressosVendidos(Integer totalIngressosVendidos) {
    this.totalIngressosVendidos = totalIngressosVendidos;
  }

  public Double getFaturamentoTotal() {
    return faturamentoTotal;
  }

  public void setFaturamentoTotal(Double faturamentoTotal) {
    this.faturamentoTotal = faturamentoTotal;
  }
}
