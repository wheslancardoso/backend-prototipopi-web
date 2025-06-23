package com.teatro.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa os pontos de fidelidade do usuário
 * 
 * Registra todas as operações de ganho, resgate e expiração de pontos
 */
@Entity
@Table(name = "pontos_fidelidade")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PontosFidelidade {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @Column(name = "pontos", nullable = false)
  private Integer pontos;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_operacao", nullable = false)
  private TipoOperacao tipoOperacao;

  @Column(name = "origem", length = 100)
  private String origem;

  @Column(name = "descricao", columnDefinition = "TEXT")
  private String descricao;

  @CreatedDate
  @Column(name = "data_operacao", nullable = false, updatable = false)
  private LocalDateTime dataOperacao;

  /**
   * Enum que define os tipos de operação com pontos
   */
  public enum TipoOperacao {
    GANHO("Ganho de Pontos"), RESGATE("Resgate de Pontos"), EXPIRACAO("Expiração de Pontos");

    private final String descricao;

    TipoOperacao(String descricao) {
      this.descricao = descricao;
    }

    public String getDescricao() {
      return descricao;
    }
  }

  /**
   * Construtor para criação de operação de pontos
   */
  public PontosFidelidade(Usuario usuario, Integer pontos, TipoOperacao tipoOperacao, String origem,
      String descricao) {
    this.usuario = usuario;
    this.pontos = pontos;
    this.tipoOperacao = tipoOperacao;
    this.origem = origem;
    this.descricao = descricao;
  }

  /**
   * Cria uma operação de ganho de pontos
   */
  public static PontosFidelidade ganharPontos(Usuario usuario, Integer pontos, String origem,
      String descricao) {
    return new PontosFidelidade(usuario, pontos, TipoOperacao.GANHO, origem, descricao);
  }

  /**
   * Cria uma operação de resgate de pontos
   */
  public static PontosFidelidade resgatarPontos(Usuario usuario, Integer pontos, String origem,
      String descricao) {
    return new PontosFidelidade(usuario, pontos, TipoOperacao.RESGATE, origem, descricao);
  }

  /**
   * Cria uma operação de expiração de pontos
   */
  public static PontosFidelidade expirarPontos(Usuario usuario, Integer pontos, String origem,
      String descricao) {
    return new PontosFidelidade(usuario, pontos, TipoOperacao.EXPIRACAO, origem, descricao);
  }

  /**
   * Verifica se é uma operação de ganho
   */
  public boolean isGanho() {
    return TipoOperacao.GANHO.equals(this.tipoOperacao);
  }

  /**
   * Verifica se é uma operação de resgate
   */
  public boolean isResgate() {
    return TipoOperacao.RESGATE.equals(this.tipoOperacao);
  }

  /**
   * Verifica se é uma operação de expiração
   */
  public boolean isExpiracao() {
    return TipoOperacao.EXPIRACAO.equals(this.tipoOperacao);
  }
}
