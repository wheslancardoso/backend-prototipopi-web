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
 * Entidade que representa uma notificação do sistema
 * 
 * Pode ser do tipo EMAIL, SMS, PUSH ou SISTEMA
 */
@Entity
@Table(name = "notificacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notificacao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo", nullable = false)
  private Tipo tipo;

  @Column(name = "titulo", nullable = false, length = 100)
  private String titulo;

  @Column(name = "mensagem", nullable = false, columnDefinition = "TEXT")
  private String mensagem;

  @Column(name = "lida", nullable = false)
  private Boolean lida = false;

  @Column(name = "dados_extras", columnDefinition = "JSON")
  private String dadosExtras;

  @CreatedDate
  @Column(name = "data_envio", nullable = false, updatable = false)
  private LocalDateTime dataEnvio;

  @Column(name = "data_leitura")
  private LocalDateTime dataLeitura;

  /**
   * Enum que define os tipos de notificação
   */
  public enum Tipo {
    EMAIL("Email"), SMS("SMS"), PUSH("Push Notification"), SISTEMA("Notificação do Sistema");

    private final String descricao;

    Tipo(String descricao) {
      this.descricao = descricao;
    }

    public String getDescricao() {
      return descricao;
    }
  }

  /**
   * Construtor para criação de notificação
   */
  public Notificacao(Usuario usuario, Tipo tipo, String titulo, String mensagem) {
    this.usuario = usuario;
    this.tipo = tipo;
    this.titulo = titulo;
    this.mensagem = mensagem;
    this.lida = false;
  }

  /**
   * Construtor para criação de notificação com dados extras
   */
  public Notificacao(Usuario usuario, Tipo tipo, String titulo, String mensagem,
      String dadosExtras) {
    this.usuario = usuario;
    this.tipo = tipo;
    this.titulo = titulo;
    this.mensagem = mensagem;
    this.dadosExtras = dadosExtras;
    this.lida = false;
  }

  /**
   * Marca a notificação como lida
   */
  public void marcarComoLida() {
    this.lida = true;
    this.dataLeitura = LocalDateTime.now();
  }

  /**
   * Verifica se a notificação foi lida
   */
  public boolean isLida() {
    return this.lida != null && this.lida;
  }

  /**
   * Verifica se a notificação não foi lida
   */
  public boolean isNaoLida() {
    return !isLida();
  }

  /**
   * Cria uma notificação de sistema
   */
  public static Notificacao notificacaoSistema(Usuario usuario, String titulo, String mensagem) {
    return new Notificacao(usuario, Tipo.SISTEMA, titulo, mensagem);
  }

  /**
   * Cria uma notificação de email
   */
  public static Notificacao notificacaoEmail(Usuario usuario, String titulo, String mensagem) {
    return new Notificacao(usuario, Tipo.EMAIL, titulo, mensagem);
  }

  /**
   * Cria uma notificação de SMS
   */
  public static Notificacao notificacaoSMS(Usuario usuario, String titulo, String mensagem) {
    return new Notificacao(usuario, Tipo.SMS, titulo, mensagem);
  }

  /**
   * Cria uma notificação push
   */
  public static Notificacao notificacaoPush(Usuario usuario, String titulo, String mensagem) {
    return new Notificacao(usuario, Tipo.PUSH, titulo, mensagem);
  }
}
