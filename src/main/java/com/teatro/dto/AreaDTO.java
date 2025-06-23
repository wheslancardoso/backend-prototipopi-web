package com.teatro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teatro.model.Area;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para transferência de dados de área do teatro
 */
public class AreaDTO {

  private Long id;

  @NotBlank(message = "Nome da área é obrigatório")
  @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
  private String nome;

  @NotNull(message = "Preço é obrigatório")
  @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
  @DecimalMax(value = "1000.00", message = "Preço máximo é R$ 1000,00")
  private BigDecimal preco;

  @NotNull(message = "Capacidade total é obrigatória")
  @Min(value = 1, message = "Capacidade mínima é 1 poltrona")
  @Max(value = 500, message = "Capacidade máxima é 500 poltronas")
  private Integer capacidadeTotal;

  @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
  private String descricao;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long sessaoId;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Boolean ativo;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dataCriacao;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dataAtualizacao;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer poltronasOcupadas;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer poltronasDisponiveis;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Double percentualOcupacao;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private BigDecimal faturamento;

  // Construtores
  public AreaDTO() {}

  public AreaDTO(Area area) {
    this.id = area.getId();
    this.nome = area.getNome();
    this.preco = area.getPreco();
    this.capacidadeTotal = area.getCapacidadeTotal();
    this.descricao = area.getDescricao();
    this.ativo = area.getAtivo();
    this.dataCriacao = area.getDataCriacao();
    this.dataAtualizacao = area.getDataAtualizacao();

    // Informações da sessão (se fornecida)
    if (sessaoId != null) {
      this.sessaoId = sessaoId;
      this.poltronasOcupadas = this.capacidadeTotal - area.getPoltronasDisponiveis(sessaoId);
      this.poltronasDisponiveis = area.getPoltronasDisponiveis(sessaoId);
      this.percentualOcupacao = area.getPercentualOcupacao(sessaoId);
      this.faturamento = area.getFaturamentoSessao(sessaoId);
    }
  }

  // Construtor com sessão específica
  public AreaDTO(Area area, Long sessaoId) {
    this(area);
    this.sessaoId = sessaoId;
    this.poltronasOcupadas = this.capacidadeTotal - area.getPoltronasDisponiveis(sessaoId);
    this.poltronasDisponiveis = area.getPoltronasDisponiveis(sessaoId);
    this.percentualOcupacao = area.getPercentualOcupacao(sessaoId);
    this.faturamento = area.getFaturamentoSessao(sessaoId);
  }

  // Método para converter DTO para entidade
  public Area toEntity() {
    Area area = new Area();
    area.setId(this.id);
    area.setNome(this.nome);
    area.setPreco(this.preco);
    area.setCapacidadeTotal(this.capacidadeTotal);
    area.setDescricao(this.descricao);
    area.setAtivo(this.ativo != null ? this.ativo : true);
    return area;
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

  public BigDecimal getPreco() {
    return preco;
  }

  public void setPreco(BigDecimal preco) {
    this.preco = preco;
  }

  public Integer getCapacidadeTotal() {
    return capacidadeTotal;
  }

  public void setCapacidadeTotal(Integer capacidadeTotal) {
    this.capacidadeTotal = capacidadeTotal;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public Long getSessaoId() {
    return sessaoId;
  }

  public void setSessaoId(Long sessaoId) {
    this.sessaoId = sessaoId;
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

  public BigDecimal getFaturamento() {
    return faturamento;
  }

  public void setFaturamento(BigDecimal faturamento) {
    this.faturamento = faturamento;
  }
}
